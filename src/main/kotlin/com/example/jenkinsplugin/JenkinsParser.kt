package com.example.jenkinsplugin

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

class JenkinsParser : PsiParser {

    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val marker = builder.mark()

        // Simple parsing: just consume all tokens
        while (!builder.eof()) {
            builder.advanceLexer()
        }

        marker.done(root)
        return builder.treeBuilt
    }
}
