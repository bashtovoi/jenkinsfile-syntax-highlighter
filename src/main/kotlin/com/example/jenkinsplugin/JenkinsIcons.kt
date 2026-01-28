package com.example.jenkinsplugin

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object JenkinsIcons {
    @JvmField
    val FILE: Icon = IconLoader.getIcon("/icons/jenkins.svg", JenkinsIcons::class.java)
}