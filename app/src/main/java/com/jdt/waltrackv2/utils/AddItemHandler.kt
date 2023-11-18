package com.jdt.waltrackv2.utils

import android.app.Activity
import android.content.Intent
import com.google.android.material.floatingactionbutton.FloatingActionButton


//TODO: Add implementations
class AddItemHandler (button: FloatingActionButton, intent: Intent, activity : Activity) {

    init {
        button.setOnClickListener{
            activity.startActivity(intent)
        }
    }
}