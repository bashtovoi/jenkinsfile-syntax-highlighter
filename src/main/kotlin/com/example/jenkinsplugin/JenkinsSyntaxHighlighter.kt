package com.example.jenkinsplugin

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.intellij.openapi.editor.HighlighterColors

class JenkinsSyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        // Category 0: Groovy Language Keywords - Purple/Keyword (def, if, else, for, etc.)
        val GROOVY_KEYWORD = TextAttributesKey.createTextAttributesKey(
            "GROOVY_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )

        // Category 1: Pipeline Structure - Bold Purple (main blocks like pipeline, stages)
        val PIPELINE_STRUCTURE = TextAttributesKey.createTextAttributesKey(
            "JENKINS_PIPELINE_STRUCTURE",
            DefaultLanguageHighlighterColors.KEYWORD
        )

        // Separate highlighting for stage - Yellow/Orange (Instance Method)
        val STAGE_KEYWORD = TextAttributesKey.createTextAttributesKey(
            "JENKINS_STAGE",
            DefaultLanguageHighlighterColors.INSTANCE_METHOD
        )

        // Separate highlighting for steps - Light Blue/Cyan
        val STEPS_KEYWORD = TextAttributesKey.createTextAttributesKey(
            "JENKINS_STEPS",
            DefaultLanguageHighlighterColors.STATIC_FIELD
        )

        // Category 2: Section Keywords - Blue (agent, environment, options, etc.)
        val SECTION_KEYWORD = TextAttributesKey.createTextAttributesKey(
            "JENKINS_SECTION_KEYWORD",
            DefaultLanguageHighlighterColors.INSTANCE_FIELD
        )

        // Category 3: Post Conditions - Orange/Yellow (success, failure, always, etc.)
        val POST_CONDITION = TextAttributesKey.createTextAttributesKey(
            "JENKINS_POST_CONDITION",
            DefaultLanguageHighlighterColors.CONSTANT
        )

        // Category 4: When Conditions - Cyan (when, branch, expression, etc.)
        val WHEN_CONDITION = TextAttributesKey.createTextAttributesKey(
            "JENKINS_WHEN_CONDITION",
            DefaultLanguageHighlighterColors.STATIC_METHOD
        )

        // Category 5: Build Steps - Using KEYWORD for testing (sh, echo, git, build, etc.)
        val BUILD_STEP = TextAttributesKey.createTextAttributesKey(
            "JENKINS_BUILD_STEP",
            DefaultLanguageHighlighterColors.KEYWORD  // Same as pipeline, should be purple/bold
        )

        // Category 6: Agent Types - Light Purple (any, none, label, docker, etc.)
        val AGENT_TYPE = TextAttributesKey.createTextAttributesKey(
            "JENKINS_AGENT_TYPE",
            DefaultLanguageHighlighterColors.CLASS_REFERENCE
        )

        // Category 7: Options & Parameters - Brown/Gold (buildDiscarder, string, choice, etc.)
        val OPTION_KEYWORD = TextAttributesKey.createTextAttributesKey(
            "JENKINS_OPTION",
            DefaultLanguageHighlighterColors.METADATA
        )

        // Category 9: Boolean Literals - Constant Color (true, false, null)
        val BOOLEAN_LITERAL = TextAttributesKey.createTextAttributesKey(
            "JENKINS_BOOLEAN",
            DefaultLanguageHighlighterColors.CONSTANT
        )

        val STRING = TextAttributesKey.createTextAttributesKey(
            "JENKINS_STRING",
            DefaultLanguageHighlighterColors.STRING
        )

        val REGEX = TextAttributesKey.createTextAttributesKey(
            "JENKINS_REGEX",
            DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE
        )

        val COMMENT = TextAttributesKey.createTextAttributesKey(
            "JENKINS_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT
        )

        val NUMBER = TextAttributesKey.createTextAttributesKey(
            "JENKINS_NUMBER",
            DefaultLanguageHighlighterColors.NUMBER
        )

        val BRACES = TextAttributesKey.createTextAttributesKey(
            "JENKINS_BRACES",
            DefaultLanguageHighlighterColors.BRACES
        )

        val BRACKETS = TextAttributesKey.createTextAttributesKey(
            "JENKINS_BRACKETS",
            DefaultLanguageHighlighterColors.BRACKETS
        )

        val PARENTHESES = TextAttributesKey.createTextAttributesKey(
            "JENKINS_PARENTHESES",
            DefaultLanguageHighlighterColors.PARENTHESES
        )

        val OPERATOR = TextAttributesKey.createTextAttributesKey(
            "JENKINS_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )

        val DOT = TextAttributesKey.createTextAttributesKey(
            "JENKINS_DOT",
            DefaultLanguageHighlighterColors.DOT
        )

        val COMMA = TextAttributesKey.createTextAttributesKey(
            "JENKINS_COMMA",
            DefaultLanguageHighlighterColors.COMMA
        )

        val SEMICOLON = TextAttributesKey.createTextAttributesKey(
            "JENKINS_SEMICOLON",
            DefaultLanguageHighlighterColors.SEMICOLON
        )

        val BAD_CHARACTER = TextAttributesKey.createTextAttributesKey(
            "JENKINS_BAD_CHARACTER",
            HighlighterColors.BAD_CHARACTER
        )

        private val GROOVY_KEYWORD_KEYS = arrayOf(GROOVY_KEYWORD)
        private val PIPELINE_STRUCTURE_KEYS = arrayOf(PIPELINE_STRUCTURE)
        private val STAGE_KEYWORD_KEYS = arrayOf(STAGE_KEYWORD)
        private val STEPS_KEYWORD_KEYS = arrayOf(STEPS_KEYWORD)
        private val SECTION_KEYWORD_KEYS = arrayOf(SECTION_KEYWORD)
        private val POST_CONDITION_KEYS = arrayOf(POST_CONDITION)
        private val WHEN_CONDITION_KEYS = arrayOf(WHEN_CONDITION)
        private val BUILD_STEP_KEYS = arrayOf(BUILD_STEP)
        private val AGENT_TYPE_KEYS = arrayOf(AGENT_TYPE)
        private val OPTION_KEYWORD_KEYS = arrayOf(OPTION_KEYWORD)
        private val BOOLEAN_LITERAL_KEYS = arrayOf(BOOLEAN_LITERAL)
        private val STRING_KEYS = arrayOf(STRING)
        private val REGEX_KEYS = arrayOf(REGEX)
        private val COMMENT_KEYS = arrayOf(COMMENT)
        private val NUMBER_KEYS = arrayOf(NUMBER)
        private val BRACES_KEYS = arrayOf(BRACES)
        private val BRACKETS_KEYS = arrayOf(BRACKETS)
        private val PARENTHESES_KEYS = arrayOf(PARENTHESES)
        private val OPERATOR_KEYS = arrayOf(OPERATOR)
        private val DOT_KEYS = arrayOf(DOT)
        private val COMMA_KEYS = arrayOf(COMMA)
        private val SEMICOLON_KEYS = arrayOf(SEMICOLON)
        private val BAD_CHAR_KEYS = arrayOf(BAD_CHARACTER)
        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }

    override fun getHighlightingLexer(): Lexer {
        return JenkinsLexer()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when (tokenType) {
            JenkinsTokenTypes.GROOVY_KEYWORD -> GROOVY_KEYWORD_KEYS
            JenkinsTokenTypes.PIPELINE_STRUCTURE -> PIPELINE_STRUCTURE_KEYS
            JenkinsTokenTypes.STAGE_KEYWORD -> STAGE_KEYWORD_KEYS
            JenkinsTokenTypes.STEPS_KEYWORD -> STEPS_KEYWORD_KEYS
            JenkinsTokenTypes.SECTION_KEYWORD -> SECTION_KEYWORD_KEYS
            JenkinsTokenTypes.POST_CONDITION -> POST_CONDITION_KEYS
            JenkinsTokenTypes.WHEN_CONDITION -> WHEN_CONDITION_KEYS
            JenkinsTokenTypes.BUILD_STEP -> BUILD_STEP_KEYS
            JenkinsTokenTypes.AGENT_TYPE -> AGENT_TYPE_KEYS
            JenkinsTokenTypes.OPTION_KEYWORD -> OPTION_KEYWORD_KEYS
            JenkinsTokenTypes.BOOLEAN_LITERAL -> BOOLEAN_LITERAL_KEYS
            JenkinsTokenTypes.STRING -> STRING_KEYS
            JenkinsTokenTypes.REGEX -> REGEX_KEYS
            JenkinsTokenTypes.COMMENT -> COMMENT_KEYS
            JenkinsTokenTypes.NUMBER -> NUMBER_KEYS
            JenkinsTokenTypes.LBRACE, JenkinsTokenTypes.RBRACE -> BRACES_KEYS
            JenkinsTokenTypes.LBRACKET, JenkinsTokenTypes.RBRACKET -> BRACKETS_KEYS
            JenkinsTokenTypes.LPAREN, JenkinsTokenTypes.RPAREN -> PARENTHESES_KEYS
            JenkinsTokenTypes.OPERATOR -> OPERATOR_KEYS
            JenkinsTokenTypes.DOT -> DOT_KEYS
            JenkinsTokenTypes.COMMA -> COMMA_KEYS
            JenkinsTokenTypes.SEMICOLON -> SEMICOLON_KEYS
            JenkinsTokenTypes.COLON -> SEMICOLON_KEYS
            JenkinsTokenTypes.BAD_CHARACTER -> BAD_CHAR_KEYS
            else -> EMPTY_KEYS
        }
    }
}
