package com.example.jenkinsplugin

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeRegistry
import com.intellij.openapi.util.io.ByteSequence
import com.intellij.openapi.vfs.VirtualFile

class JenkinsFileTypeDetector : FileTypeRegistry.FileTypeDetector {
    override fun detect(file: VirtualFile, firstBytes: ByteSequence, firstCharsIfText: CharSequence?): FileType? {
        val name = file.name

        // Detect Jenkinsfile or any file containing "jenkinsfile" (case insensitive)
        if (name == "Jenkinsfile" ||
            name.contains("jenkinsfile", ignoreCase = true) ||
            name.contains("Jenkinsfile")) {
            return JenkinsFileType.INSTANCE
        }

        return null
    }
}
