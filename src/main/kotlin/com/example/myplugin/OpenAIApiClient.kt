package com.example.myplugin

import net.minidev.json.JSONObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import org.json.JSONObject as quoter


class OpenAIApiClient(private val apiKey: String) {

    private val client = OkHttpClient()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    fun generateCode(input: String, callback: (result: String?, error: String?) -> Unit) {
        val escapedInput = quoter.quote(input).replace("\"", "")

//        val prompt = "Remove the constructor function. $escapedInput".replace("\"", "")
        val prompt = "Input: php 5 and symfony 2; Expected output php 7 and symfony 5"

        val requestBodyStr = """
        {
          "model": "code-davinci-edit-001",
          "input": "$escapedInput",
          "instruction": "$prompt"
        }
        """.trimIndent()
        println(requestBodyStr)

        val requestBody = requestBodyStr.toRequestBody(jsonMediaType)
        val request = Request.Builder()
                .url("https://api.openai.com/v1/edits")
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
