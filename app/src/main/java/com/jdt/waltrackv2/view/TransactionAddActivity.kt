package com.jdt.waltrackv2.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.jdt.waltrackv2.adapters.setupDropdownAdapter
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.databinding.ActivityTransactionAddBinding
import com.jdt.waltrackv2.databinding.DropdownItemBinding

class TransactionAddActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTransactionAddBinding
    private lateinit var dropdownItemBinding: DropdownItemBinding
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

        binding.transactionType.setupDropdownAdapter(
            this, R.layout.dropdown_item,
            resources.getStringArray(R.array.transaction_type)
        )

        binding.transactionCategory.setupDropdownAdapter(
            this,
            R.layout.dropdown_item,
            resources.getStringArray(R.array.transaction_tags)
        )
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