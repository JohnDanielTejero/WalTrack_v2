package com.jdt.waltrackv2.utils

import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText

class ValidateField {
    companion object{
        const val regex = "[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?"
        fun notEmptyText(textInputEditText: TextInputEditText): Boolean {
            if (textInputEditText.text?.isEmpty()!!){
                textInputEditText.error = "field is required!"
                return false
            }
            return true
        }
        fun hasSelected(textInputEditText: AutoCompleteTextView) : Boolean{
            if (textInputEditText.text?.equals("Please Select")!!){
                textInputEditText.error = "field is required!"
                return false
            }
            return true
        }
        fun notEmptyText(textInputEditText: AutoCompleteTextView): Boolean {
            if (textInputEditText.text?.isEmpty()!!){
                textInputEditText.error = "field is required!"
                return false
            }
            return true
        }
        fun validAmount(textInputEditText: TextInputEditText): Boolean {
            if (textInputEditText.text.toString().toDouble() <= 0){
                textInputEditText.error = "amount should be greater than 0!"
                return false
            }
            return true
        }

        fun checkNumFormat(textInputEditText: TextInputEditText): Boolean {
            if (!textInputEditText.toString().matches(regex.toRegex())) {
                textInputEditText.error = "Field should be in correct format."
                return false
            }
            return true
        }
    }
}