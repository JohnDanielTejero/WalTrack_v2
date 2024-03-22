package com.jdt.waltrackv2.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import com.jdt.waltrackv2.adapters.setupDropdownAdapter
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.data.model.TransactionTable
import com.jdt.waltrackv2.data.view_model.TransactionViewModel
import com.jdt.waltrackv2.data.view_model.WalletViewModel
import com.jdt.waltrackv2.databinding.ActivityTransactionAddBinding
import com.jdt.waltrackv2.utils.ValidateField

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TransactionAddActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTransactionAddBinding
    private lateinit var walletViewModel: WalletViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        walletViewModel = ViewModelProvider(this)[WalletViewModel::class.java]
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        setSupportActionBar(binding.toolbar)
        val actionbar = supportActionBar
        actionbar?.title = "Add Transaction"
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayShowTitleEnabled(true)

        val navigationIcon = binding.toolbar.navigationIcon

        navigationIcon?.let {
            val typedValue = TypedValue()
            theme.resolveAttribute(R.attr.customTitleColor, typedValue, true)
            val color = typedValue.data
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

        walletViewModel.wallets.observe(this){
            binding.transactionWallet.setupDropdownAdapter(
                this,
                R.layout.dropdown_item,
                it.map { i -> i.walletName }.toTypedArray()
            )
        }

        initDatePicker()

        binding.datePickerButton.text = getTodayDate()
        binding.datePickerButton.setOnClickListener{
            datePickerDialog.show()
        }

        binding.transactionSubmit.setOnClickListener{
            binding.transactionSubmit.isEnabled = false
            var isTransactionNameValid = true
            var isTransactionTagValid = true
            var isTransactionNoteValid = true
            var isTransactionTypeValid = true
            var isWalletValid = true
            var isTransactionAmountValid = true

            // Check if empty
            isWalletValid = ValidateField.notEmptyText(binding.transactionWallet)
            isTransactionTypeValid = ValidateField.notEmptyText(binding.transactionType)
            isTransactionNameValid = ValidateField.notEmptyText(binding.transactionTitle)
            isTransactionNoteValid = ValidateField.notEmptyText(binding.transactionNote)
            isTransactionTagValid = ValidateField.notEmptyText(binding.transactionCategory)
            isTransactionAmountValid = ValidateField.notEmptyText(binding.transactionAmount)



            //check if empty
            if (isWalletValid && isTransactionTypeValid && isTransactionNameValid && isTransactionNoteValid && isTransactionTagValid && isTransactionAmountValid) {

                //check amount format
                if (ValidateField.checkNumFormat(binding.transactionAmount)){
                    if (ValidateField.validAmount(binding.transactionAmount)){
                        Toast.makeText(this, "Transaction Successfully Added!", Toast.LENGTH_SHORT).show()
                        walletViewModel.getWalletByName(binding.transactionWallet.text.toString()).observe(this){
                            val date = binding.datePickerButton.text.toString().split(" ")
                            val month = date[0]
                            val day = date[1].toInt()
                            val year = date[2]

                            val transaction = TransactionTable(0,
                                binding.transactionTitle.text.toString(),
                                binding.transactionType.text.toString(),
                                binding.transactionAmount.text.toString().toDouble(),
                                binding.transactionNote.text.toString(),
                                binding.transactionCategory.text.toString(),
                                day,
                                month,
                                year,
                                it.walletId)
                            transactionViewModel.addTransaction(transaction)
                            val intent = Intent()
                            setResult(Activity.RESULT_OK, intent)
                            finish()

                        }
                    }
                }





            } else {
                Toast.makeText(this, "Fields need to be filled properly.", Toast.LENGTH_SHORT).show()
            }
            binding.transactionSubmit.isEnabled = true
        }
    }

    private fun getTodayDate() : String{
        val cal: Calendar = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DATE)

        return makeDateString(day, month, year)
    }

    private fun initDatePicker(){
        val dateSetListener: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { _, year: Int, month: Int, day: Int ->
            var dateString  = makeDateString(day, month, year)
            binding.datePickerButton.text = dateString
        }

        val cal: Calendar = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DATE)

        val style = R.style.CustomDatePickerDialog
        datePickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)

    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return getMonthFormat(month) + " " + day + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
        return monthFormat.format(calendar.time)
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