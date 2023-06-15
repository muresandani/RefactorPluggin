package com.example.myplugin

import net.minidev.json.JSONObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import org.json.JSONObject as quoter
import com.google.gson.Gson



class OpenAIApiClient(private val apiKey: String) {

    private val client = OkHttpClient()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    fun generateCode(input: String, callback: (result: String?, error: String?) -> Unit) {
        val escapedInput = quoter.quote(input)
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
        val prompt = "Refactor this code from symfony 4 & php 7 to symfony 5 and php 8: $escapedInput"


        val requestBodyStr = """
        {
          "model": "gpt-3.5-turbo",
          "messages": [{"role": "user", "content": "$prompt"}]
        }
        """.trimIndent()

        val requestBody = requestBodyStr.toRequestBody(jsonMediaType)
        val request = Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer $apiKey")
                .post(requestBody)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val generatedCode = response.body?.string()
                    callback(generatedCode, null)
                } else {
                    callback(null, "Request failed with status code ${response.code}")
                }
            }
        })
    }
}
