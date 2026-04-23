package com.example.jenkinsplugin

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.editor.Editor
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext

class JenkinsCompletionContributor : CompletionContributor() {

    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(),
            JenkinsKeywordCompletionProvider()
        )
    }

    private class JenkinsKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            val text = parameters.editor.document.text
            val offset = parameters.offset

            if (isAfterEnvDot(text, offset)) {
                addEnvVariables(result, text)
                return
            }

            addKeywords(result)
        }

        // Returns true when the caret is right after "env." (possibly with partial input)
        private fun isAfterEnvDot(text: String, offset: Int): Boolean {
            // Walk back past any identifier chars the user may have already typed
            var pos = offset - 1
            while (pos >= 0 && (text[pos].isLetterOrDigit() || text[pos] == '_')) pos--
            return pos >= 3 &&
                text[pos] == '.' &&
                text[pos - 1] == 'v' &&
                text[pos - 2] == 'n' &&
                text[pos - 3] == 'e'
        }

        private fun addEnvVariables(result: CompletionResultSet, fileText: String) {
            // Built-in Jenkins environment variables from the official docs
            JENKINS_ENV_VARS.forEach { (name, description) ->
                result.addElement(
                    LookupElementBuilder.create(name)
                        .withTypeText(description)
                        .withBoldness(false)
                )
            }

            // Variables declared in environment { } blocks in the current file
            collectLocalEnvVars(fileText).forEach { name ->
                if (JENKINS_ENV_VARS.none { it.first == name }) {
                    result.addElement(
                        LookupElementBuilder.create(name)
                            .withTypeText("local environment variable")
                            .withBoldness(true)
                    )
                }
            }
        }

        // Extracts VARNAME from lines like `VARNAME = "..."` inside environment { } blocks
        private fun collectLocalEnvVars(text: String): List<String> {
            val result = mutableListOf<String>()
            var inEnvBlock = false
            var braceDepth = 0

            for (line in text.lines()) {
                val trimmed = line.trim()
                if (!inEnvBlock) {
                    if (trimmed.startsWith("environment") && trimmed.contains("{")) {
                        inEnvBlock = true
                        braceDepth = 1
                    }
                } else {
                    braceDepth += trimmed.count { it == '{' }
                    braceDepth -= trimmed.count { it == '}' }
                    if (braceDepth <= 0) {
                        inEnvBlock = false
                        continue
                    }
                    // Match lines like: SOME_VAR = "value" or SOME_VAR = credentials('...')
                    val match = ENV_VAR_DECL.find(trimmed)
                    if (match != null) result.add(match.groupValues[1])
                }
            }
            return result
        }

        private fun addKeywords(result: CompletionResultSet) {
            // Pipeline Structure Keywords
            addKeyword(result, "pipeline", "Pipeline definition block", "pipeline {\n    \n}")
            addKeyword(result, "agent", "Define where pipeline executes", "agent any")
            addKeyword(result, "stages", "Container for all stages", "stages {\n    \n}")
            addKeyword(result, "stage", "Define a stage", "stage('') {\n    \n}")
            addKeyword(result, "steps", "Steps to execute", "steps {\n    \n}")
            addKeyword(result, "script", "Scripted pipeline block", "script {\n    \n}")
            addKeyword(result, "parallel", "Parallel execution", "parallel {\n    \n}")
            addKeyword(result, "matrix", "Matrix execution", "matrix {\n    \n}")

            // Section Keywords
            addKeyword(result, "environment", "Environment variables", "environment {\n    \n}")
            addKeyword(result, "options", "Pipeline options", "options {\n    \n}")
            addKeyword(result, "parameters", "Build parameters", "parameters {\n    \n}")
            addKeyword(result, "triggers", "Build triggers", "triggers {\n    \n}")
            addKeyword(result, "post", "Post-build actions", "post {\n    \n}")
            addKeyword(result, "tools", "Tool installations", "tools {\n    \n}")
            addKeyword(result, "input", "User input", "input {\n    \n}")

            // Build Steps
            addKeyword(result, "echo", "Print message", "echo ''")
            addKeyword(result, "git", "Git checkout", "git url: ''")
            addKeyword(result, "checkout", "Checkout SCM", "checkout scm")
            addKeyword(result, "timeout", "Timeout wrapper", "timeout(time: 1, unit: 'HOURS') {\n    \n}")
            addKeyword(result, "retry", "Retry on failure", "retry(3) {\n    \n}")
            addKeyword(result, "sleep", "Sleep/wait", "sleep 10")
            addKeyword(result, "build", "Trigger another job", "build job: ''")
            addKeyword(result, "archiveArtifacts", "Archive artifacts", "archiveArtifacts artifacts: '**/*.jar'")
            addKeyword(result, "junit", "Publish test results", "junit '**/target/test-*.xml'")
            addKeyword(result, "stash", "Stash files", "stash name: '', includes: ''")
            addKeyword(result, "unstash", "Unstash files", "unstash ''")

            // Credentials
            addKeyword(result, "withCredentials", "Use credentials", "withCredentials([]) {\n    \n}")
            addKeyword(result, "withEnv", "Set environment variables", "withEnv([]) {\n    \n}")
            addKeyword(result, "usernamePassword", "Username/password credential", "usernamePassword(credentialsId: '', usernameVariable: '', passwordVariable: '')")
            addKeyword(result, "sshUserPrivateKey", "SSH private key credential", "sshUserPrivateKey(credentialsId: '', keyFileVariable: '')")

            // Options
            addKeyword(result, "timestamps", "Add timestamps to output", "timestamps()")
            addKeyword(result, "ansiColor", "Enable ANSI color", "ansiColor('xterm')")
            addKeyword(result, "buildDiscarder", "Discard old builds", "buildDiscarder(logRotator(numToKeepStr: '10'))")
            addKeyword(result, "disableConcurrentBuilds", "Disable concurrent builds", "disableConcurrentBuilds()")
            addKeyword(result, "skipDefaultCheckout", "Skip default SCM checkout", "skipDefaultCheckout()")

            // Parameters
            addKeyword(result, "string", "String parameter", "string(name: '', defaultValue: '', description: '')")
            addKeyword(result, "booleanParam", "Boolean parameter", "booleanParam(name: '', defaultValue: true, description: '')")
            addKeyword(result, "choice", "Choice parameter", "choice(name: '', choices: [], description: '')")
            addKeyword(result, "password", "Password parameter", "password(name: '', description: '')")

            // Agent Types
            addKeyword(result, "any", "Any available agent", "any")
            addKeyword(result, "none", "No global agent", "none")
            addKeyword(result, "label", "Agent with label", "label ''")
            addKeyword(result, "docker", "Docker agent", "docker {\n    image ''\n}")
            addKeyword(result, "kubernetes", "Kubernetes agent", "kubernetes {\n    \n}")

            // Post Conditions
            addKeyword(result, "always", "Always execute", "always {\n    \n}")
            addKeyword(result, "success", "On success", "success {\n    \n}")
            addKeyword(result, "failure", "On failure", "failure {\n    \n}")
            addKeyword(result, "unstable", "On unstable", "unstable {\n    \n}")
            addKeyword(result, "changed", "On status change", "changed {\n    \n}")
            addKeyword(result, "fixed", "On fix", "fixed {\n    \n}")
            addKeyword(result, "regression", "On regression", "regression {\n    \n}")
            addKeyword(result, "aborted", "On abort", "aborted {\n    \n}")
            addKeyword(result, "cleanup", "Final cleanup", "cleanup {\n    \n}")

            // When Conditions
            addKeyword(result, "when", "Conditional execution", "when {\n    \n}")
            addKeyword(result, "branch", "Branch condition", "branch ''")
            addKeyword(result, "expression", "Expression condition", "expression { }")
            addKeyword(result, "allOf", "All conditions must match", "allOf {\n    \n}")
            addKeyword(result, "anyOf", "Any condition matches", "anyOf {\n    \n}")

            // Common values
            addKeyword(result, "true", "Boolean true", "true")
            addKeyword(result, "false", "Boolean false", "false")
            addKeyword(result, "null", "Null value", "null")
        }

        private fun addKeyword(result: CompletionResultSet, keyword: String, description: String, insertText: String? = null) {
            val builder = LookupElementBuilder.create(keyword)
                .withTypeText(description)
                .withBoldness(true)

            val finalBuilder = if (insertText != null && insertText != keyword) {
                builder.withInsertHandler { context, _ ->
                    val document = context.document
                    val startOffset = context.startOffset
                    document.replaceString(startOffset, context.tailOffset, insertText)
                    context.editor.caretModel.moveToOffset(startOffset + insertText.length)
                }
            } else {
                builder
            }

            result.addElement(finalBuilder)
        }

        companion object {
            private val ENV_VAR_DECL = Regex("""^([A-Z][A-Z0-9_]*)\s*=\s*""")

            // Jenkins built-in environment variables
            // Source: https://www.jenkins.io/doc/book/pipeline/jenkinsfile/#using-environment-variables
            val JENKINS_ENV_VARS = listOf(
                // Core build info
                "BUILD_ID" to "Current build ID (same as BUILD_NUMBER for most purposes)",
                "BUILD_NUMBER" to "Current build number, e.g. '153'",
                "BUILD_DISPLAY_NAME" to "Display name of the build, e.g. '#153'",
                "BUILD_TAG" to "String of 'jenkins-<JOB_NAME>-<BUILD_NUMBER>'",
                "BUILD_URL" to "URL to the current build results page",

                // Job info
                "JOB_NAME" to "Name of the project of this build, e.g. 'foo' or 'foo/bar'",
                "JOB_BASE_NAME" to "Short name of the project, stripping folder paths",
                "JOB_URL" to "URL to the Jenkins job page",

                // Executor & node
                "EXECUTOR_NUMBER" to "Unique number identifying this executor on this agent",
                "NODE_NAME" to "Name of the node the build is running on (master or agent name)",
                "NODE_LABELS" to "Space-separated list of labels assigned to the node",
                "WORKSPACE" to "Absolute path of the workspace directory",
                "WORKSPACE_TMP" to "Temporary directory near the workspace for ephemeral files",

                // Jenkins server
                "JENKINS_HOME" to "Absolute path on the controller where Jenkins stores data",
                "JENKINS_URL" to "Full URL of the Jenkins server",

                // SCM / Git (populated by SCM checkout step)
                "GIT_COMMIT" to "SHA of the current commit",
                "GIT_PREVIOUS_COMMIT" to "SHA of the previous commit on this branch",
                "GIT_PREVIOUS_SUCCESSFUL_COMMIT" to "SHA of the last successful commit on this branch",
                "GIT_BRANCH" to "Name of the branch being built, e.g. 'origin/main'",
                "GIT_LOCAL_BRANCH" to "Local branch name without the remote prefix",
                "GIT_CHECKOUT_DIR" to "Directory into which the repository was checked out",
                "GIT_URL" to "Remote URL of the repository",
                "GIT_URL_N" to "Remote URL of the Nth repository (for multi-SCM)",
                "GIT_AUTHOR_NAME" to "Author name of the current commit",
                "GIT_AUTHOR_EMAIL" to "Author email of the current commit",
                "GIT_COMMITTER_NAME" to "Committer name of the current commit",
                "GIT_COMMITTER_EMAIL" to "Committer email of the current commit",

                // Change / PR info (multibranch / GitHub branch source)
                "BRANCH_NAME" to "Name of the branch (multibranch pipelines)",
                "BRANCH_IS_PRIMARY" to "'true' when building the primary branch",
                "CHANGE_ID" to "Pull request / change request identifier",
                "CHANGE_URL" to "URL to the pull request page",
                "CHANGE_TITLE" to "Title of the pull request",
                "CHANGE_AUTHOR" to "Username of the pull request author",
                "CHANGE_AUTHOR_DISPLAY_NAME" to "Display name of the pull request author",
                "CHANGE_AUTHOR_EMAIL" to "Email of the pull request author",
                "CHANGE_TARGET" to "Target branch of the pull request",
                "CHANGE_BRANCH" to "Source branch of the pull request",
                "CHANGE_FORK" to "Fork name if the PR comes from a fork",

                // Tag builds
                "TAG_NAME" to "Tag name if building a tag",
                "TAG_TIMESTAMP" to "Timestamp of the tag",
                "TAG_UNIXTIME" to "Unix timestamp of the tag",
                "TAG_DATE" to "Date of the tag in 'yyyy-MM-dd' format",

                // Build cause
                "BUILD_CAUSE" to "Comma-separated list of causes that triggered this build",
                "BUILD_CAUSE_USERIDCAUSE" to "'true' if build was triggered by a user",
                "BUILD_CAUSE_REMOTECAUSE" to "'true' if build was triggered remotely",
                "BUILD_CAUSE_SCMTRIGGER" to "'true' if build was triggered by SCM polling",
                "BUILD_CAUSE_TIMERTRIGGER" to "'true' if build was triggered by a timer",

                // Miscellaneous
                "VERSION" to "Version string (set by project or release plugin)",
                "JAVA_HOME" to "Path to the JDK (when configured via tools {})"
            )
        }
    }
}
