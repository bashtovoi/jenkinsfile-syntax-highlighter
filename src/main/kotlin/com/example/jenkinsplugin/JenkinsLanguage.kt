package com.example.jenkinsplugin

import com.intellij.lang.Language

class JenkinsLanguage : Language("Jenkins") {
    companion object {
        val INSTANCE = JenkinsLanguage()
    }
}
