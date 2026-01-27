package com.example.jenkinsplugin

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

class JenkinsBraceMatcher : PairedBraceMatcher {

    override fun getPairs(): Array<BracePair> {
        return arrayOf(
            BracePair(JenkinsTokenTypes.LBRACE, JenkinsTokenTypes.RBRACE, true),
            BracePair(JenkinsTokenTypes.LPAREN, JenkinsTokenTypes.RPAREN, false),
            BracePair(JenkinsTokenTypes.LBRACKET, JenkinsTokenTypes.RBRACKET, false)
        )
    }

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        return true
    }

    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int {
        return openingBraceOffset
    }
}
