package com.jdt.waltrackv2.utils

class RenderElementHandler {
    companion object {
        fun initPlaceholders(cb: () -> Unit) {
            cb.invoke()
        }
        fun removePlaceholders(cb: () -> Unit) {
            cb.invoke()
        }
    }
}