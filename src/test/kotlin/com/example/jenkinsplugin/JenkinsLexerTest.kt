package com.example.jenkinsplugin

import com.intellij.psi.tree.IElementType
import org.junit.Assert.assertEquals
import org.junit.Test

class JenkinsLexerTest {

    private fun tokenize(input: String): List<Pair<String, IElementType?>> {
        val lexer = JenkinsLexer()
        lexer.start(input, 0, input.length, 0)

        val tokens = mutableListOf<Pair<String, IElementType?>>()
        while (lexer.tokenType != null) {
            val tokenText = input.substring(lexer.tokenStart, lexer.tokenEnd)
            tokens.add(Pair(tokenText, lexer.tokenType))
            lexer.advance()
        }
        return tokens
    }

    @Test
    fun testPipelineStructureKeywords() {
        val input = "pipeline stages parallel matrix sequential"
        val tokens = tokenize(input)

        val keywordTokens = tokens.filter { it.second == JenkinsTokenTypes.PIPELINE_STRUCTURE }
        assertEquals(5, keywordTokens.size)
        assertEquals("pipeline", keywordTokens[0].first)
        assertEquals("stages", keywordTokens[1].first)
        assertEquals("parallel", keywordTokens[2].first)
        assertEquals("matrix", keywordTokens[3].first)
        assertEquals("sequential", keywordTokens[4].first)
    }

    @Test
    fun testStageAndStepsKeywords() {
        val input = "stage steps"
        val tokens = tokenize(input)

        val stageTokens = tokens.filter { it.second == JenkinsTokenTypes.STAGE_KEYWORD }
        assertEquals(1, stageTokens.size)
        assertEquals("stage", stageTokens[0].first)

        val stepsTokens = tokens.filter { it.second == JenkinsTokenTypes.STEPS_KEYWORD }
        assertEquals(1, stepsTokens.size)
        assertEquals("steps", stepsTokens[0].first)
    }

    @Test
    fun testSectionKeywords() {
        val input = "agent environment options parameters triggers post tools input script"
        val tokens = tokenize(input)

        val sectionTokens = tokens.filter { it.second == JenkinsTokenTypes.SECTION_KEYWORD }
        assertEquals(9, sectionTokens.size)
        assertEquals("agent", sectionTokens[0].first)
        assertEquals("environment", sectionTokens[1].first)
        assertEquals("options", sectionTokens[2].first)
        assertEquals("parameters", sectionTokens[3].first)
        assertEquals("triggers", sectionTokens[4].first)
        assertEquals("post", sectionTokens[5].first)
        assertEquals("tools", sectionTokens[6].first)
        assertEquals("input", sectionTokens[7].first)
        assertEquals("script", sectionTokens[8].first)
    }

    @Test
    fun testPostConditions() {
        val input = "always changed fixed regression aborted failure success unstable unsuccessful cleanup"
        val tokens = tokenize(input)

        val postTokens = tokens.filter { it.second == JenkinsTokenTypes.POST_CONDITION }
        assertEquals(10, postTokens.size)
        assertEquals("always", postTokens[0].first)
        assertEquals("changed", postTokens[1].first)
        assertEquals("fixed", postTokens[2].first)
        assertEquals("regression", postTokens[3].first)
        assertEquals("aborted", postTokens[4].first)
        assertEquals("failure", postTokens[5].first)
        assertEquals("success", postTokens[6].first)
        assertEquals("unstable", postTokens[7].first)
        assertEquals("unsuccessful", postTokens[8].first)
        assertEquals("cleanup", postTokens[9].first)
    }

    @Test
    fun testWhenConditions() {
        val input = "when branch buildingTag changelog changeset expression equals not allOf anyOf"
        val tokens = tokenize(input)

        val whenTokens = tokens.filter { it.second == JenkinsTokenTypes.WHEN_CONDITION }
        assertEquals(10, whenTokens.size)
        assertEquals("when", whenTokens[0].first)
        assertEquals("branch", whenTokens[1].first)
        assertEquals("buildingTag", whenTokens[2].first)
        assertEquals("changelog", whenTokens[3].first)
        assertEquals("changeset", whenTokens[4].first)
        assertEquals("expression", whenTokens[5].first)
        assertEquals("equals", whenTokens[6].first)
        assertEquals("not", whenTokens[7].first)
        assertEquals("allOf", whenTokens[8].first)
        assertEquals("anyOf", whenTokens[9].first)
    }

    @Test
    fun testBuildSteps() {
        val input = "sh bat echo checkout git sleep build stash unstash junit"
        val tokens = tokenize(input)

        val buildStepTokens = tokens.filter { it.second == JenkinsTokenTypes.BUILD_STEP }
        assertEquals(10, buildStepTokens.size)
        assertEquals("sh", buildStepTokens[0].first)
        assertEquals("bat", buildStepTokens[1].first)
        assertEquals("echo", buildStepTokens[2].first)
        assertEquals("checkout", buildStepTokens[3].first)
        assertEquals("git", buildStepTokens[4].first)
        assertEquals("sleep", buildStepTokens[5].first)
        assertEquals("build", buildStepTokens[6].first)
        assertEquals("stash", buildStepTokens[7].first)
        assertEquals("unstash", buildStepTokens[8].first)
        assertEquals("junit", buildStepTokens[9].first)
    }

    @Test
    fun testGroovyKeywords() {
        val input = "def var return if else for while try catch finally"
        val tokens = tokenize(input)

        val groovyTokens = tokens.filter { it.second == JenkinsTokenTypes.GROOVY_KEYWORD }
        assertEquals(10, groovyTokens.size)
        assertEquals("def", groovyTokens[0].first)
        assertEquals("var", groovyTokens[1].first)
        assertEquals("return", groovyTokens[2].first)
        assertEquals("if", groovyTokens[3].first)
        assertEquals("else", groovyTokens[4].first)
        assertEquals("for", groovyTokens[5].first)
        assertEquals("while", groovyTokens[6].first)
        assertEquals("try", groovyTokens[7].first)
        assertEquals("catch", groovyTokens[8].first)
        assertEquals("finally", groovyTokens[9].first)
    }

    @Test
    fun testAgentTypes() {
        val input = "any none label dockerfile kubernetes"
        val tokens = tokenize(input)

        val agentTokens = tokens.filter { it.second == JenkinsTokenTypes.AGENT_TYPE }
        assertEquals(5, agentTokens.size)
        assertEquals("any", agentTokens[0].first)
        assertEquals("none", agentTokens[1].first)
        assertEquals("label", agentTokens[2].first)
        assertEquals("dockerfile", agentTokens[3].first)
        assertEquals("kubernetes", agentTokens[4].first)
    }

    @Test
    fun testBooleanLiterals() {
        val input = "true false null"
        val tokens = tokenize(input)

        val booleanTokens = tokens.filter { it.second == JenkinsTokenTypes.BOOLEAN_LITERAL }
        assertEquals(3, booleanTokens.size)
        assertEquals("true", booleanTokens[0].first)
        assertEquals("false", booleanTokens[1].first)
        assertEquals("null", booleanTokens[2].first)
    }

    @Test
    fun testComplexJenkinsfile() {
        val input = """
            pipeline {
                agent any
                stages {
                    stage('Build') {
                        steps {
                            sh 'make'
                            echo 'Building...'
                        }
                    }
                }
                post {
                    always {
                        junit 'reports/**/*.xml'
                    }
                    failure {
                        echo 'Build failed'
                    }
                }
            }
        """.trimIndent()

        val tokens = tokenize(input)

        // Verify key tokens are present
        val pipelineTokens = tokens.filter { it.first == "pipeline" && it.second == JenkinsTokenTypes.PIPELINE_STRUCTURE }
        assertEquals(1, pipelineTokens.size)

        val agentTokens = tokens.filter { it.first == "agent" && it.second == JenkinsTokenTypes.SECTION_KEYWORD }
        assertEquals(1, agentTokens.size)

        val stagesTokens = tokens.filter { it.first == "stages" && it.second == JenkinsTokenTypes.PIPELINE_STRUCTURE }
        assertEquals(1, stagesTokens.size)

        val stageTokens = tokens.filter { it.first == "stage" && it.second == JenkinsTokenTypes.STAGE_KEYWORD }
        assertEquals(1, stageTokens.size)

        val stepsTokens = tokens.filter { it.first == "steps" && it.second == JenkinsTokenTypes.STEPS_KEYWORD }
        assertEquals(1, stepsTokens.size)

        val shTokens = tokens.filter { it.first == "sh" && it.second == JenkinsTokenTypes.BUILD_STEP }
        assertEquals(1, shTokens.size)

        val echoTokens = tokens.filter { it.first == "echo" && it.second == JenkinsTokenTypes.BUILD_STEP }
        assertEquals(2, echoTokens.size)

        val postTokens = tokens.filter { it.first == "post" && it.second == JenkinsTokenTypes.SECTION_KEYWORD }
        assertEquals(1, postTokens.size)

        val alwaysTokens = tokens.filter { it.first == "always" && it.second == JenkinsTokenTypes.POST_CONDITION }
        assertEquals(1, alwaysTokens.size)

        val failureTokens = tokens.filter { it.first == "failure" && it.second == JenkinsTokenTypes.POST_CONDITION }
        assertEquals(1, failureTokens.size)

        val junitTokens = tokens.filter { it.first == "junit" && it.second == JenkinsTokenTypes.BUILD_STEP }
        assertEquals(1, junitTokens.size)
    }

    @Test
    fun testStringsAndComments() {
        val input = """
            // This is a comment
            def name = "Jenkins"
            def script = 'build.sh'
            /* Multi-line comment */
        """.trimIndent()

        val tokens = tokenize(input)

        val commentTokens = tokens.filter { it.second == JenkinsTokenTypes.COMMENT }
        assertEquals(2, commentTokens.size)

        val stringTokens = tokens.filter { it.second == JenkinsTokenTypes.STRING }
        assertEquals(2, stringTokens.size)
        assertEquals("\"Jenkins\"", stringTokens[0].first)
        assertEquals("'build.sh'", stringTokens[1].first)
    }

    @Test
    fun testBracketsAndBraces() {
        val input = "{ } [ ] ( )"
        val tokens = tokenize(input)

        assertEquals(JenkinsTokenTypes.LBRACE, tokens.filter { it.first == "{" }[0].second)
        assertEquals(JenkinsTokenTypes.RBRACE, tokens.filter { it.first == "}" }[0].second)
        assertEquals(JenkinsTokenTypes.LBRACKET, tokens.filter { it.first == "[" }[0].second)
        assertEquals(JenkinsTokenTypes.RBRACKET, tokens.filter { it.first == "]" }[0].second)
        assertEquals(JenkinsTokenTypes.LPAREN, tokens.filter { it.first == "(" }[0].second)
        assertEquals(JenkinsTokenTypes.RPAREN, tokens.filter { it.first == ")" }[0].second)
    }

    @Test
    fun testNumbers() {
        val input = "123 456.789"
        val tokens = tokenize(input)

        val numberTokens = tokens.filter { it.second == JenkinsTokenTypes.NUMBER }
        assertEquals(2, numberTokens.size)
        assertEquals("123", numberTokens[0].first)
        assertEquals("456.789", numberTokens[1].first)
    }

    @Test
    fun testRegexPatterns() {
        val input = "def pattern = /test.*/ def pattern2 = ~/test.*/"
        val tokens = tokenize(input)

        val regexTokens = tokens.filter { it.second == JenkinsTokenTypes.REGEX }
        assertEquals(2, regexTokens.size)
        assertEquals("/test.*/", regexTokens[0].first)
        assertEquals("~/test.*/", regexTokens[1].first)
    }

    @Test
    fun testDockerBuildSteps() {
        val input = "docker dockerNode withEnv withCredentials"
        val tokens = tokenize(input)

        val dockerTokens = tokens.filter { it.first == "docker" && it.second == JenkinsTokenTypes.BUILD_STEP }
        assertEquals(1, dockerTokens.size)

        val dockerNodeTokens = tokens.filter { it.first == "dockerNode" && it.second == JenkinsTokenTypes.BUILD_STEP }
        assertEquals(1, dockerNodeTokens.size)

        val withEnvTokens = tokens.filter { it.first == "withEnv" && it.second == JenkinsTokenTypes.BUILD_STEP }
        assertEquals(1, withEnvTokens.size)

        val withCredsTokens = tokens.filter { it.first == "withCredentials" && it.second == JenkinsTokenTypes.BUILD_STEP }
        assertEquals(1, withCredsTokens.size)
    }
}
