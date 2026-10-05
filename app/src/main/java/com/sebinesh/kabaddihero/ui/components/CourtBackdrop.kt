package com.sebinesh.kabaddihero.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun CourtBackdrop(
    modifier: Modifier = Modifier,
    alpha: Float = 0.25f
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Court boundary
        val courtPadding = 24f
        val courtWidth = w - (courtPadding * 2)
        val courtHeight = h - (courtPadding * 2)

        val chalkColor = Color.White.copy(alpha = alpha)
        val bonusColor = Color(0xFFF2B41B).copy(alpha = alpha * 1.2f)
        val stroke = Stroke(width = 3f)
        val dashedStroke = Stroke(
            width = 2.5f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
        )

        // Outer boundary
        drawRect(
            color = chalkColor,
            topLeft = Offset(courtPadding, courtPadding),
            size = Size(courtWidth, courtHeight),
            style = stroke
        )

        // Lobbies on left and right (1m equivalent)
        val lobbyWidth = courtWidth * 0.08f
        // Left lobby line
        drawLine(
            color = chalkColor,
            start = Offset(courtPadding + lobbyWidth, courtPadding),
            end = Offset(courtPadding + lobbyWidth, courtPadding + courtHeight),
            strokeWidth = 2f
        )
        // Right lobby line
        drawLine(
            color = chalkColor,
            start = Offset(courtPadding + courtWidth - lobbyWidth, courtPadding),
            end = Offset(courtPadding + courtWidth - lobbyWidth, courtPadding + courtHeight),
            strokeWidth = 2f
        )

        // Midline (centre of court)
        val midY = courtPadding + (courtHeight / 2)
        drawLine(
            color = chalkColor,
            start = Offset(courtPadding, midY),
            end = Offset(courtPadding + courtWidth, midY),
            strokeWidth = 4f
        )

        // Baulk lines (top and bottom half)
        val baulkDistance = courtHeight * 0.22f
        val topBaulkY = midY - baulkDistance
        val bottomBaulkY = midY + baulkDistance

        drawLine(
            color = chalkColor,
            start = Offset(courtPadding + lobbyWidth, topBaulkY),
            end = Offset(courtPadding + courtWidth - lobbyWidth, topBaulkY),
            strokeWidth = 3f
        )
        drawLine(
            color = chalkColor,
            start = Offset(courtPadding + lobbyWidth, bottomBaulkY),
            end = Offset(courtPadding + courtWidth - lobbyWidth, bottomBaulkY),
            strokeWidth = 3f
        )

        // Bonus lines (closer to endline)
        val bonusDistance = courtHeight * 0.30f
        val topBonusY = midY - bonusDistance
        val bottomBonusY = midY + bonusDistance

        drawLine(
            color = bonusColor,
            start = Offset(courtPadding + lobbyWidth, topBonusY),
            end = Offset(courtPadding + courtWidth - lobbyWidth, topBonusY),
            strokeWidth = 3f
        )
        drawLine(
            color = bonusColor,
            start = Offset(courtPadding + lobbyWidth, bottomBonusY),
            end = Offset(courtPadding + courtWidth - lobbyWidth, bottomBonusY),
            strokeWidth = 3f
        )
    }
}
