package com.example.jenkinsplugin

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class JenkinsFoldingBuilder : FoldingBuilderEx() {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        val text = document.text

        // Find all matching brace pairs and create folding regions
        findFoldableBlocks(text, document, root.node, descriptors)

        return descriptors.toTypedArray()
    }

    private fun findFoldableBlocks(
        text: String,
        document: Document,
        rootNode: ASTNode,
        descriptors: MutableList<FoldingDescriptor>
    ) {
        val braceStack = mutableListOf<Int>()
        var i = 0

        while (i < text.length) {
            when {
                // Skip single-line comments
                text[i] == '/' && i + 1 < text.length && text[i + 1] == '/' -> {
                    while (i < text.length && text[i] != '\n') i++
                }
                // Skip multi-line comments
                text[i] == '/' && i + 1 < text.length && text[i + 1] == '*' -> {
                    i += 2
                    while (i + 1 < text.length) {
                        if (text[i] == '*' && text[i + 1] == '/') {
                            i += 2
                            break
                        }
                        i++
                    }
                }
                // Skip single-quoted strings
                text[i] == '\'' -> {
                    i++
                    while (i < text.length) {
                        if (text[i] == '\\' && i + 1 < text.length) {
                            i += 2
                        } else if (text[i] == '\'') {
                            i++
                            break
                        } else {
                            i++
                        }
                    }
                }
                // Skip double-quoted strings
                text[i] == '"' -> {
                    i++
                    while (i < text.length) {
                        if (text[i] == '\\' && i + 1 < text.length) {
                            i += 2
                        } else if (text[i] == '"') {
                            i++
                            break
                        } else {
                            i++
                        }
                    }
                }
                // Handle opening brace
                text[i] == '{' -> {
                    braceStack.add(i)
                    i++
                }
                // Handle closing brace
                text[i] == '}' -> {
                    if (braceStack.isNotEmpty()) {
                        val openPos = braceStack.removeAt(braceStack.size - 1)
                        val closePos = i

                        // Check if this is a multi-line block
                        val startLine = document.getLineNumber(openPos)
                        val endLine = document.getLineNumber(closePos)

                        if (endLine > startLine) {
                            val range = TextRange(openPos, closePos + 1)
                            val descriptor = FoldingDescriptor(
                                rootNode,
                                range,
                                null,
                                extractPlaceholderText(text, openPos, closePos)
                            )
                            descriptors.add(descriptor)
                        }
                    }
                    i++
                }
                else -> i++
            }
        }
    }

    private fun extractPlaceholderText(text: String, openPos: Int, closePos: Int): String {
        // Look backwards from opening brace to find the block type
        var pos = openPos - 1

        // Skip whitespace before brace
        while (pos >= 0 && text[pos].isWhitespace()) {
            pos--
        }

        if (pos < 0) return "{...}"

        // Look for closing parenthesis (for blocks like stage('name'))
        if (text[pos] == ')') {
            val parenStart = findMatchingOpenParen(text, pos)
            if (parenStart >= 0) {
                pos = parenStart - 1
                // Skip whitespace before paren
                while (pos >= 0 && text[pos].isWhitespace()) {
                    pos--
                }
            }
        }

        // Extract the keyword before the brace/paren
        val keywordEnd = pos + 1
        var keywordStart = pos
        while (keywordStart >= 0 && (text[keywordStart].isLetterOrDigit() || text[keywordStart] == '_')) {
            keywordStart--
        }
        keywordStart++

        if (keywordStart >= keywordEnd) return "{...}"

        val keyword = text.substring(keywordStart, keywordEnd).trim()

        // Handle different block types
        return when (keyword) {
            "pipeline" -> "pipeline {...}"
            "stages" -> "stages {...}"
            "stage" -> {
                // Extract stage name if present
                val stageName = extractStageName(text, openPos)
                if (stageName != null) "stage('$stageName') {...}" else "stage {...}"
            }
            "steps" -> "steps {...}"
            "script" -> "script {...}"
            "parallel" -> "parallel {...}"
            "post" -> "post {...}"
            "environment" -> "environment {...}"
            "options" -> "options {...}"
            "parameters" -> "parameters {...}"
            "when" -> "when {...}"
            "agent" -> "agent {...}"
            "triggers" -> "triggers {...}"
            "tools" -> "tools {...}"
            else -> if (keyword.isNotEmpty()) "$keyword {...}" else "{...}"
        }
    }

    private fun findMatchingOpenParen(text: String, closePos: Int): Int {
        var depth = 1
        var pos = closePos - 1

        while (pos >= 0 && depth > 0) {
            when (text[pos]) {
                ')' -> depth++
                '(' -> depth--
            }
            if (depth == 0) return pos
            pos--
        }
        return -1
    }

    private fun extractStageName(text: String, openPos: Int): String? {
        // Look backwards to find stage name in stage('name') or stage("name")
        val searchText = text.substring(maxOf(0, openPos - 100), openPos)

        val singleQuote = Regex("""stage\s*\(\s*'([^']+)'\s*\)""")
        val doubleQuote = Regex("""stage\s*\(\s*"([^"]+)"\s*\)""")

        singleQuote.find(searchText)?.groupValues?.get(1)?.let { return it }
        doubleQuote.find(searchText)?.groupValues?.get(1)?.let { return it }

        return null
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return "{...}"
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return false
    }
}
