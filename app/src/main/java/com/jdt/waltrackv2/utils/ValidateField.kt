package com.jdt.waltrackv2.utils

import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText

class ValidateField {
    companion object{
        private const val regex = "[-+]?\\d+\\.?\\d*([eE][-+]?\\d+)?"

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

        fun checkNumFormat(inputText: TextInputEditText): Boolean {
            if (inputText.text.toString().isBlank() || inputText.text.toString() == ".") {
                inputText.error = "Field should be in correct format."
                return false
            }

            if (!inputText.text.toString().matches(regex.toRegex())) {
                inputText.error = "Field should be in correct format."
                return false
            }

            return true
        }
    }
}