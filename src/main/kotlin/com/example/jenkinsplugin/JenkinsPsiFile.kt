package com.example.jenkinsplugin

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class JenkinsPsiFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, JenkinsLanguage.INSTANCE) {

    override fun getFileType(): FileType {
        return JenkinsFileType.INSTANCE
    }

    override fun toString(): String {
        return "Jenkins File"
    }
}
