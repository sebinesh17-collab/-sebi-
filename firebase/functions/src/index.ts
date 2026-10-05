import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

admin.initializeApp();
const db = admin.firestore();

export const onTournamentCreated = functions.firestore
  .document("tournaments/{tournamentId}")
  .onCreate(async (snap, context) => {
    const data = snap.data();
    const tournamentId = context.params.tournamentId;

    // Send FCM push notification to topic matching district
    const districtTopic = `district_${data.district?.toLowerCase().replace(/\s+/g, "_")}`;
    const payload = {
      notification: {
        title: `கபடி போட்டி: ${data.name}`,
        body: `${data.district} - ${data.venueName} | பரிசு: ₹${data.prizeAmount || 0}`,
      },
      data: {
        tournamentId: tournamentId,
        click_action: "FLUTTER_NOTIFICATION_CLICK",
        type: "TOURNAMENT_NEW",
      },
      topic: districtTopic,
    };

    try {
      await admin.messaging().send(payload);
    } catch (e) {
      console.error("Failed to dispatch push notification", e);
    }

    // Write audit log
    await db.collection("auditLogs").add({
      action: "TOURNAMENT_CREATED",
      targetId: tournamentId,
      actorId: data.organizerId || "SYSTEM",
      timestamp: admin.firestore.FieldValue.serverTimestamp(),
      details: { name: data.name, district: data.district },
    });
  });

export const onMatchScoreUpdated = functions.firestore
  .document("matches/{matchId}")
  .onUpdate(async (change, context) => {
    const before = change.before.data();
    const after = change.after.data();
    const matchId = context.params.matchId;

    // Notify users following this match if score changed significantly
    if (after.status === "LIVE" && (before.scoreTeamA !== after.scoreTeamA || before.scoreTeamB !== after.scoreTeamB)) {
      const matchTopic = `match_${matchId}`;
      await admin.messaging().send({
        notification: {
          title: `லைவ் ஸ்கோர்: ${after.teamAName} vs ${after.teamBName}`,
          body: `${after.teamAName} ${after.scoreTeamA} - ${after.scoreTeamB} ${after.teamBName}`,
        },
        data: {
          matchId: matchId,
          type: "MATCH_SCORE_UPDATE",
        },
        topic: matchTopic,
      });
    }
  });
