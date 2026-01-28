package com.example.jenkinsplugin

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class JenkinsFileType : LanguageFileType(JenkinsLanguage.INSTANCE) {

    companion object {
        val INSTANCE = JenkinsFileType()
    }

    override fun getName(): String = "Jenkins"

    override fun getDescription(): String = "Jenkins pipeline file"

    override fun getDefaultExtension(): String = "jenkinsfile"

    override fun getIcon(): Icon = JenkinsIcons.FILE
}
