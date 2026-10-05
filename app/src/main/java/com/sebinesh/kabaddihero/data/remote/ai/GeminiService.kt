package com.sebinesh.kabaddihero.data.remote.ai

import android.graphics.Bitmap
import android.util.Base64
import com.sebinesh.kabaddihero.BuildConfig
import com.sebinesh.kabaddihero.core.constants.AppConstants
import com.sebinesh.kabaddihero.core.logging.Logger
import com.sebinesh.kabaddihero.core.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class GeminiService {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun generateContent(
        prompt: String,
        modelName: String = AppConstants.MODEL_GEMINI_FLASH_GENERAL,
        systemInstruction: String? = null,
        enableHighThinking: Boolean = false,
        jsonResponse: Boolean = false,
        imageBitmap: Bitmap? = null,
        conversationHistory: List<Pair<String, String>> = emptyList() // role ("user" or "model") to text
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = run {
            val fromBuildConfig = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }
            if (fromBuildConfig.isNotBlank() && fromBuildConfig != "MY_GEMINI_API_KEY") {
                fromBuildConfig
            } else {
                System.getenv("GEMINI_API_KEY") ?: ""
            }
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Logger.w("GeminiService", "GEMINI_API_KEY not configured or placeholder.")
            return@withContext Result.Error(
                Exception("GEMINI_API_KEY not configured"),
                "Google AI Studio API key is not configured. Please add GEMINI_API_KEY in the Secrets panel."
            )
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$apiKey"

        try {
            val root = JSONObject()

            // Contents list
            val contentsArray = JSONArray()

            // Prior turns if any
            for ((role, text) in conversationHistory) {
                val turnObj = JSONObject()
                turnObj.put("role", role)
                val partsArr = JSONArray()
                val part = JSONObject()
                part.put("text", text)
                partsArr.put(part)
                turnObj.put("parts", partsArr)
                contentsArray.put(turnObj)
            }

            // Current turn
            val currentTurn = JSONObject()
            currentTurn.put("role", "user")
            val currentParts = JSONArray()

            val textPart = JSONObject()
            textPart.put("text", prompt)
            currentParts.put(textPart)

            // Inline image if present
            if (imageBitmap != null) {
                val baos = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
                val base64 = Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)

                val imagePart = JSONObject()
                val inlineData = JSONObject()
                inlineData.put("mimeType", "image/jpeg")
                inlineData.put("data", base64)
                imagePart.put("inlineData", inlineData)
                currentParts.put(imagePart)
            }

            currentTurn.put("parts", currentParts)
            contentsArray.put(currentTurn)
            root.put("contents", contentsArray)

            // System instruction
            if (!systemInstruction.isNullOrBlank()) {
                val sysObj = JSONObject()
                val sysParts = JSONArray()
                val sp = JSONObject()
                sp.put("text", systemInstruction)
                sysParts.put(sp)
                sysObj.put("parts", sysParts)
                root.put("systemInstruction", sysObj)
            }

            // Generation config
            val genConfig = JSONObject()
            if (jsonResponse) {
                genConfig.put("responseMimeType", "application/json")
            }
            if (enableHighThinking) {
                val thinkingConfig = JSONObject()
                thinkingConfig.put("thinkingLevel", "HIGH")
                genConfig.put("thinkingConfig", thinkingConfig)
            }
            root.put("generationConfig", genConfig)

            val body = root.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                Logger.e("GeminiService", "API error: HTTP ${response.code} $responseBody")
                val detailedMsg = try {
                    JSONObject(responseBody).optJSONObject("error")?.optString("message")
                } catch (e: Exception) {
                    null
                }
                val userMsg = if (!detailedMsg.isNullOrBlank()) {
                    "Google AI Studio: $detailedMsg"
                } else {
                    "AI service temporarily unavailable (Code ${response.code})."
                }
                return@withContext Result.Error(
                    Exception("Gemini API Error: ${response.code}"),
                    userMsg
                )
            }

            val respJson = JSONObject(responseBody)
            val candidates = respJson.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    val text = parts.getJSONObject(0).optString("text")
                    return@withContext Result.Success(text)
                }
            }

            Result.Error(Exception("Empty candidate response"), "No response generated by AI.")
        } catch (e: Exception) {
            Logger.e("GeminiService", "Gemini call exception", e)
            Result.Error(e, "AI temporarily unavailable: ${e.localizedMessage}")
        }
    }
}
