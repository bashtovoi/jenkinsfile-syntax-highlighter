package com.example.jenkinsplugin

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType

class JenkinsLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var startOffset: Int = 0
    private var endOffset: Int = 0
    private var currentOffset: Int = 0
    private var tokenType: IElementType? = null
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0

    companion object {
        // Category 0: Groovy Language Keywords
        private val GROOVY_KEYWORDS = setOf(
            "def", "var", "return", "if", "else", "for", "while", "try", "catch",
            "finally", "throw", "import", "package", "class", "interface", "extends",
            "implements", "new", "this", "super", "instanceof", "in", "as", "break",
            "continue", "switch", "case", "default"
        )

        // Category 1: Pipeline Structure (main blocks)
        private val PIPELINE_STRUCTURE_KEYWORDS = setOf(
            "pipeline", "stages", "parallel", "matrix", "sequential"
        )

        // Separate highlighting for stage and steps
        private val STAGE_KEYWORDS = setOf("stage")
        private val STEPS_KEYWORDS = setOf("steps")

        // Category 2: Section Keywords (configuration sections)
        private val SECTION_KEYWORDS = setOf(
            "agent", "environment", "options", "parameters", "triggers", "post", "tools",
            "input", "script", "libraries", "axes", "axis", "exclude"
        )

        // Category 3: Post Conditions
        private val POST_CONDITIONS = setOf(
            "always", "changed", "fixed", "regression", "aborted", "failure",
            "success", "unstable", "unsuccessful", "cleanup"
        )

        // Category 4: When Conditions
        private val WHEN_CONDITIONS = setOf(
            "when", "branch", "buildingTag", "changelog", "changeset", "changeRequest",
            "equals", "expression", "tag", "not", "allOf", "anyOf", "triggeredBy",
            "beforeAgent", "beforeInput", "beforeOptions"
        )

        // Category 5: Build Steps & Commands
        private val BUILD_STEPS = setOf(
            // Shell commands
            "sh", "bat", "powershell", "pwsh",

            // Output and logging
            "echo", "println", "print",

            // Source control
            "checkout", "scm", "git",

            // Jenkins actions
            "sleep", "waitUntil", "build", "input",

            // Error handling
            "catchError", "error", "warnError",

            // Artifacts
            "unstash", "stash", "archiveArtifacts",

            // Testing
            "junit", "publishHTML",

            // File operations
            "deleteDir", "fileExists", "readFile", "writeFile", "dir", "pwd",

            // Notifications
            "mail", "emailext", "slackSend",

            // Wrappers
            "withEnv", "withCredentials", "withAWS", "withKubeConfig",

            // Docker
            "docker", "dockerNode", "dockerFingerprintFrom",

            // Other
            "node", "ws", "load", "library", "timestamp", "isUnix"
        )

        // Category 6: Agent Types
        private val AGENT_TYPES = setOf(
            "any", "none", "label", "dockerfile", "kubernetes", "yaml",
            "containerTemplate", "podTemplate", "container"
        )

        // Category 7: Options & Parameters
        private val OPTION_KEYWORDS = setOf(
            // Pipeline options
            "buildDiscarder", "checkoutToSubdirectory", "disableConcurrentBuilds",
            "disableResume", "newContainerPerStage", "overrideIndexTriggers",
            "preserveStashes", "quietPeriod", "skipDefaultCheckout",
            "skipStagesAfterUnstable", "timestamps", "parallelsAlwaysFailFast",
            "disableRestartFromStage", "timeout", "retry", "ansiColor",
            "skipStagesAfterUnstable", "durabilityHint", "rateLimitBuilds",

            // Parameter types
            "string", "booleanParam", "choice", "password", "file", "text",

            // Credentials
            "credentials", "usernamePassword", "sshUserPrivateKey",
            "usernameColonPassword", "certificate", "secretFile", "secretText",

            // Triggers
            "cron", "pollSCM", "upstream", "githubPush",

            // Tools
            "maven", "jdk", "gradle", "nodejs", "git", "ant", "dockerTool"
        )

        // Category: Jenkins Plugins
        private val PLUGIN_NAMES = setOf(
            // Popular plugins
            "ansiColor", "timestamps", "buildDiscarder", "disableConcurrentBuilds",
            "dockerNode", "dockerTool", "dockerFingerprintFrom",
            "slack", "slackSend", "emailext",
            "withCredentials", "withEnv", "withAWS", "withKubeConfig",
            "kubernetes", "podTemplate", "containerTemplate",
            "junit", "jacoco", "cobertura", "publishHTML",
            "artifactoryPublish", "nexusArtifactUploader",
            "sonarqube", "withSonarQubeEnv",
            "githubNotify", "githubStatus", "gitlabCommitStatus",
            "office365ConnectorSend", "mattermostSend", "hipchatSend",
            "lockableResources", "milestone", "waitUntil",
            "parallel", "matrix", "failFast"
        )


        // Category 9: Boolean and Null literals
        private val BOOLEAN_LITERALS = setOf("true", "false", "null")
    }

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.currentOffset = startOffset
        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        advance()
    }

    override fun getState(): Int = 0

    override fun getTokenType(): IElementType? = tokenType

    override fun getTokenStart(): Int = tokenStart

    override fun getTokenEnd(): Int = tokenEnd

    override fun advance() {
        if (currentOffset >= endOffset) {
            tokenType = null
            return
        }

        tokenStart = currentOffset
        val char = buffer[currentOffset]

        when {
            char.isWhitespace() -> {
                while (currentOffset < endOffset && buffer[currentOffset].isWhitespace()) {
                    currentOffset++
                }
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.WHITESPACE
            }
            char == '/' && currentOffset + 1 < endOffset && buffer[currentOffset + 1] == '/' -> {
                while (currentOffset < endOffset && buffer[currentOffset] != '\n') {
                    currentOffset++
                }
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.COMMENT
            }
            char == '/' && currentOffset + 1 < endOffset && buffer[currentOffset + 1] == '*' -> {
                currentOffset += 2
                while (currentOffset < endOffset - 1) {
                    if (buffer[currentOffset] == '*' && buffer[currentOffset + 1] == '/') {
                        currentOffset += 2
                        break
                    }
                    currentOffset++
                }
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.COMMENT
            }
            char == '\'' -> {
                // Check for triple-quoted string '''
                if (currentOffset + 2 < endOffset &&
                    buffer[currentOffset + 1] == '\'' &&
                    buffer[currentOffset + 2] == '\'') {
                    // Triple-quoted string
                    currentOffset += 3
                    while (currentOffset + 2 < endOffset) {
                        if (buffer[currentOffset] == '\'' &&
                            buffer[currentOffset + 1] == '\'' &&
                            buffer[currentOffset + 2] == '\'') {
                            currentOffset += 3
                            break
                        }
                        currentOffset++
                    }
                    // If we didn't find closing triple quotes, consume to end
                    if (currentOffset + 2 >= endOffset) {
                        currentOffset = endOffset
                    }
                } else {
                    // Single-quoted string
                    currentOffset++
                    while (currentOffset < endOffset) {
                        if (buffer[currentOffset] == '\'' && (currentOffset == 0 || buffer[currentOffset - 1] != '\\')) {
                            currentOffset++
                            break
                        }
                        currentOffset++
                    }
                }
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.STRING
            }
            char == '"' -> {
                // Check for triple-quoted string """
                if (currentOffset + 2 < endOffset &&
                    buffer[currentOffset + 1] == '"' &&
                    buffer[currentOffset + 2] == '"') {
                    // Triple-quoted string
                    currentOffset += 3
                    while (currentOffset + 2 < endOffset) {
                        if (buffer[currentOffset] == '"' &&
                            buffer[currentOffset + 1] == '"' &&
                            buffer[currentOffset + 2] == '"') {
                            currentOffset += 3
                            break
                        }
                        currentOffset++
                    }
                    // If we didn't find closing triple quotes, consume to end
                    if (currentOffset + 2 >= endOffset) {
                        currentOffset = endOffset
                    }
                } else {
                    // Double-quoted string
                    currentOffset++
                    while (currentOffset < endOffset) {
                        if (buffer[currentOffset] == '"' && (currentOffset == 0 || buffer[currentOffset - 1] != '\\')) {
                            currentOffset++
                            break
                        }
                        currentOffset++
                    }
                }
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.STRING
            }
            char == '/' && currentOffset + 1 < endOffset && !buffer[currentOffset + 1].isWhitespace() &&
            buffer[currentOffset + 1] != '/' && buffer[currentOffset + 1] != '*' -> {
                // Regex pattern /pattern/ or slashy string
                // Check if this might be a regex (not division operator)
                // Look behind to see if this is likely a regex context
                val beforeChar = if (tokenStart > 0) buffer[tokenStart - 1] else ' '
                val isLikelyRegex = beforeChar in setOf('=', '(', ',', '[', '~', '!', ' ', '\t', '\n')

                if (isLikelyRegex) {
                    currentOffset++
                    while (currentOffset < endOffset) {
                        if (buffer[currentOffset] == '/' && buffer[currentOffset - 1] != '\\') {
                            currentOffset++
                            break
                        }
                        currentOffset++
                    }
                    tokenEnd = currentOffset
                    tokenType = JenkinsTokenTypes.REGEX
                } else {
                    // It's a division operator, handle in operator section
                    currentOffset++
                    tokenEnd = currentOffset
                    tokenType = JenkinsTokenTypes.OPERATOR
                }
            }
            char == '~' && currentOffset + 1 < endOffset && buffer[currentOffset + 1] == '/' -> {
                // Groovy regex pattern ~/pattern/
                currentOffset += 2
                while (currentOffset < endOffset) {
                    if (buffer[currentOffset] == '/' && buffer[currentOffset - 1] != '\\') {
                        currentOffset++
                        break
                    }
                    currentOffset++
                }
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.REGEX
            }
            char.isDigit() -> {
                while (currentOffset < endOffset && (buffer[currentOffset].isDigit() || buffer[currentOffset] == '.')) {
                    currentOffset++
                }
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.NUMBER
            }
            char.isLetter() || char == '_' -> {
                while (currentOffset < endOffset && (buffer[currentOffset].isLetterOrDigit() || buffer[currentOffset] == '_')) {
                    currentOffset++
                }
                tokenEnd = currentOffset
                val text = buffer.substring(tokenStart, tokenEnd)

                tokenType = when {
                    text in GROOVY_KEYWORDS -> JenkinsTokenTypes.GROOVY_KEYWORD
                    text in PIPELINE_STRUCTURE_KEYWORDS -> JenkinsTokenTypes.PIPELINE_STRUCTURE
                    text in STAGE_KEYWORDS -> JenkinsTokenTypes.STAGE_KEYWORD
                    text in STEPS_KEYWORDS -> JenkinsTokenTypes.STEPS_KEYWORD
                    text in SECTION_KEYWORDS -> JenkinsTokenTypes.SECTION_KEYWORD
                    text in POST_CONDITIONS -> JenkinsTokenTypes.POST_CONDITION
                    text in WHEN_CONDITIONS -> JenkinsTokenTypes.WHEN_CONDITION
                    text in BUILD_STEPS -> JenkinsTokenTypes.BUILD_STEP
                    text in AGENT_TYPES -> JenkinsTokenTypes.AGENT_TYPE
                    text in OPTION_KEYWORDS || text in PLUGIN_NAMES -> JenkinsTokenTypes.OPTION_KEYWORD
                    text in BOOLEAN_LITERALS -> JenkinsTokenTypes.BOOLEAN_LITERAL
                    else -> JenkinsTokenTypes.IDENTIFIER
                }
            }
            char == '{' -> {
                currentOffset++
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.LBRACE
            }
            char == '}' -> {
                currentOffset++
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.RBRACE
            }
            char == '[' -> {
                currentOffset++
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.LBRACKET
            }
            char == ']' -> {
                currentOffset++
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.RBRACKET
            }
            char == '(' -> {
                currentOffset++
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.LPAREN
            }
            char == ')' -> {
                currentOffset++
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.RPAREN
            }
            char == '.' -> {
                currentOffset++
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.DOT
            }
            char == ',' -> {
                currentOffset++
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.COMMA
            }
            char == ';' -> {
                currentOffset++
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.SEMICOLON
            }
            char == ':' -> {
                currentOffset++
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.COLON
            }
            char == '=' || char == '+' || char == '-' || char == '*' || char == '/' ||
            char == '%' || char == '!' || char == '<' || char == '>' || char == '&' ||
            char == '|' || char == '^' || char == '~' || char == '?' -> {
                currentOffset++
                // Handle multi-character operators like ==, !=, <=, >=, &&, ||, etc.
                while (currentOffset < endOffset) {
                    val nextChar = buffer[currentOffset]
                    if (nextChar == '=' || nextChar == '&' || nextChar == '|' ||
                        nextChar == '+' || nextChar == '-' || nextChar == '>' || nextChar == '<') {
                        currentOffset++
                    } else {
                        break
                    }
                }
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.OPERATOR
            }
            else -> {
                currentOffset++
                tokenEnd = currentOffset
                tokenType = JenkinsTokenTypes.IDENTIFIER
            }
        }
    }

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = endOffset
}
