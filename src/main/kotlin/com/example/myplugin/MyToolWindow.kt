package com.example.myplugin

import javax.swing.*
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.editor.Document
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.google.gson.Gson

class MyToolWindow {
    companion object {
        fun create(project: Project, fileContent: String): JComponent {
            val apiKey = "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" // Replace with your OpenAI API key
            val apiClient = OpenAIApiClient(apiKey)


            val updatedCode: String
            val errorMessage: String
            val gson = Gson()


            val rightTextArea = JTextArea()
            try {
                apiClient.generateCode(fileContent) { result, error ->
                    if (error != null) {
                        // Handle the error case
                        println("An error occurred: $error")
                    } else {
                        // Use the result
                        val openAIResponse = gson.fromJson(result, OpenAIResponse::class.java)
                        rightTextArea.text = openAIResponse.choices[0].text
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Error occurred while generating updated code: ${e.message}"
            }

            val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)

            val leftTextArea = JTextArea(fileContent)
            leftTextArea.isEditable = false

            splitPane.leftComponent = JScrollPane(leftTextArea)
            splitPane.rightComponent = JScrollPane(rightTextArea)

            splitPane.resizeWeight = 0.5 // Set the divider position to the middle

            return splitPane
        }
    }
}
