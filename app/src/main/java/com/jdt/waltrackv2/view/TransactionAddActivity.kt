package com.jdt.waltrackv2.view

import android.app.Activity
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.databinding.ActivityTransactionAddBinding

class TransactionAddActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTransactionAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val actionbar = supportActionBar
        actionbar?.title = "Add Transaction"
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayShowTitleEnabled(true)

        val navigationIcon = binding.toolbar.navigationIcon

        navigationIcon?.let {
            val color = ContextCompat.getColor(this, R.color.white)
            DrawableCompat.setTint(it, color)
            binding.toolbar.navigationIcon = it
        }
        binding.toolbar.setNavigationOnClickListener{
            finish()
        }

        removeKeyboardDisplayForCustomSpinner(binding.transactionWallet)
        removeKeyboardDisplayForCustomSpinner(binding.transactionType)
        removeKeyboardDisplayForCustomSpinner(binding.transactionCategory)

    }

    private fun removeKeyboardDisplayForCustomSpinner(v: AutoCompleteTextView){
        v.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}