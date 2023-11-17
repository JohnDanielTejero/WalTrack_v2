package com.jdt.waltrackv2.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.jdt.waltrackv2.R

class FilterHandler(private val filterButton: MaterialButton, private val filterDisplay: ConstraintLayout, context: Context) {
    private var isFilterToggled: Boolean = false

    init {
        filterButton.setOnClickListener {
            Log.d("FilterHandler", "filter is clicked")
            isFilterToggled = !isFilterToggled
            updateFilter(context)
        }
    }

    private fun updateFilter(context: Context) {
        if (isFilterToggled) {
            filterDisplay.visibility = View.VISIBLE
            filterButton.setBackgroundResource(R.drawable.filter_bg)
            filterButton.icon = ContextCompat.getDrawable(filterButton.context, R.drawable.ic_filter_hide)
        } else {
            filterDisplay.visibility = View.GONE
            filterButton.setBackgroundResource(R.drawable.filter_bg)
            filterButton.icon = ContextCompat.getDrawable(filterButton.context, R.drawable.ic_filter_show)
        }
    }
}
