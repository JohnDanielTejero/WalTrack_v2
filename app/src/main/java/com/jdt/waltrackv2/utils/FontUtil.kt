package com.jdt.waltrackv2.utils

import android.text.SpannableString
import android.text.style.UnderlineSpan

class FontUtil {
    companion object{
        fun getUnderlinedText(text: String): SpannableString {
            val content = SpannableString(text)
            content.setSpan(UnderlineSpan(), 0, content.length, 0)
            return content
        }
    }

}