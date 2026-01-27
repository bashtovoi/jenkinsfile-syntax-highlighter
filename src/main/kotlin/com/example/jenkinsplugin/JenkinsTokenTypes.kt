package com.example.jenkinsplugin

import com.intellij.psi.tree.IElementType

object JenkinsTokenTypes {
    // Groovy language keywords
    val GROOVY_KEYWORD = IElementType("GROOVY_KEYWORD", JenkinsLanguage.INSTANCE)

    // Semantic categories for Jenkins keywords
    val PIPELINE_STRUCTURE = IElementType("JENKINS_PIPELINE_STRUCTURE", JenkinsLanguage.INSTANCE)
    val STAGE_KEYWORD = IElementType("JENKINS_STAGE", JenkinsLanguage.INSTANCE)
    val STEPS_KEYWORD = IElementType("JENKINS_STEPS", JenkinsLanguage.INSTANCE)
    val SECTION_KEYWORD = IElementType("JENKINS_SECTION_KEYWORD", JenkinsLanguage.INSTANCE)
    val POST_CONDITION = IElementType("JENKINS_POST_CONDITION", JenkinsLanguage.INSTANCE)
    val WHEN_CONDITION = IElementType("JENKINS_WHEN_CONDITION", JenkinsLanguage.INSTANCE)
    val BUILD_STEP = IElementType("JENKINS_BUILD_STEP", JenkinsLanguage.INSTANCE)
    val AGENT_TYPE = IElementType("JENKINS_AGENT_TYPE", JenkinsLanguage.INSTANCE)
    val OPTION_KEYWORD = IElementType("JENKINS_OPTION", JenkinsLanguage.INSTANCE)
    val PLUGIN_NAME = IElementType("JENKINS_PLUGIN", JenkinsLanguage.INSTANCE)
    val BOOLEAN_LITERAL = IElementType("JENKINS_BOOLEAN", JenkinsLanguage.INSTANCE)

    // General tokens
    val STRING = IElementType("JENKINS_STRING", JenkinsLanguage.INSTANCE)
    val REGEX = IElementType("JENKINS_REGEX", JenkinsLanguage.INSTANCE)
    val COMMENT = IElementType("JENKINS_COMMENT", JenkinsLanguage.INSTANCE)
    val NUMBER = IElementType("JENKINS_NUMBER", JenkinsLanguage.INSTANCE)
    val IDENTIFIER = IElementType("JENKINS_IDENTIFIER", JenkinsLanguage.INSTANCE)
    val LBRACE = IElementType("JENKINS_LBRACE", JenkinsLanguage.INSTANCE)
    val RBRACE = IElementType("JENKINS_RBRACE", JenkinsLanguage.INSTANCE)
    val LBRACKET = IElementType("JENKINS_LBRACKET", JenkinsLanguage.INSTANCE)
    val RBRACKET = IElementType("JENKINS_RBRACKET", JenkinsLanguage.INSTANCE)
    val LPAREN = IElementType("JENKINS_LPAREN", JenkinsLanguage.INSTANCE)
    val RPAREN = IElementType("JENKINS_RPAREN", JenkinsLanguage.INSTANCE)
    val OPERATOR = IElementType("JENKINS_OPERATOR", JenkinsLanguage.INSTANCE)
    val DOT = IElementType("JENKINS_DOT", JenkinsLanguage.INSTANCE)
    val COMMA = IElementType("JENKINS_COMMA", JenkinsLanguage.INSTANCE)
    val SEMICOLON = IElementType("JENKINS_SEMICOLON", JenkinsLanguage.INSTANCE)
    val COLON = IElementType("JENKINS_COLON", JenkinsLanguage.INSTANCE)
    val BAD_CHARACTER = IElementType("JENKINS_BAD_CHARACTER", JenkinsLanguage.INSTANCE)
    val WHITESPACE = IElementType("JENKINS_WHITESPACE", JenkinsLanguage.INSTANCE)
}
