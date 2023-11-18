package com.jdt.waltrackv2.adapters

import android.content.Context
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

fun AutoCompleteTextView.setupDropdownAdapter(
    context: Context,
    layout: Int,
    selection: Array<String>,
    itemClickListener: ((String) -> Unit)? = null,
    onItemSelectedListener: AdapterView.OnItemSelectedListener? = null
) {
    val arrayAdapter = ArrayAdapter(context, layout, selection)
    this.setAdapter(arrayAdapter)

    // Optionally handle item selection
    this.setOnItemClickListener { _, _, position, _ ->
        itemClickListener?.invoke(selection[position])
    }

    // Set the item selected listener separately
    onItemSelectedListener?.let {
        this.onItemSelectedListener = it
    }
}