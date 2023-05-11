package com.example.myplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileDocumentManager
import javax.swing.JFrame

class MyAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val selectedFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY) ?: return

        val selectedFile = selectedFiles[0]
        val document = FileDocumentManager.getInstance().getDocument(selectedFile)

        document?.let {
            val fileContent = it.text
            val toolWindowComponent = MyToolWindow.create(project, fileContent)

            val frame = JFrame("My Custom Tool Window")
            frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
            frame.contentPane = toolWindowComponent
            frame.setSize(800, 600)
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }
    }
}