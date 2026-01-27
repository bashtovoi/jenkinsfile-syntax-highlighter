package com.example.jenkinsplugin

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext

class JenkinsCompletionContributor : CompletionContributor() {

    init {
        // Complete anywhere in Jenkins files
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
    }
}
