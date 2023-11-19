package com.jdt.waltrackv2.utils

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AddItemHandler(
    button: FloatingActionButton,
    intent: Intent,
    private val launcher: ActivityResultLauncher<Intent>
) {
    init {
        button.setOnClickListener {
            launcher.launch(intent)
        }
    }
}