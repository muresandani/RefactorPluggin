package com.example.myplugin

data class OpenAIResponse(
        val id: String,
        val res_object: String,
        val created: Int,
        val model: String,
        val choices: List<Choice>,
        val usage: Usage
)

data class Choice(
        val text: String,
        val index: Int,
        val logprobs: String?,
        val finish_reason: String
)

data class Usage(
        val prompt_tokens: Int,
        val completion_tokens: Int,
        val total_tokens: Int
)
