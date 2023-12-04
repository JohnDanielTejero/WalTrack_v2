package com.jdt.waltrackv2.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.data.view_model.WalletViewModel
import com.jdt.waltrackv2.data.model.WalletTable
import com.jdt.waltrackv2.databinding.ActivityAddWalletBinding
import com.jdt.waltrackv2.utils.ValidateField
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddWalletActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddWalletBinding
    private lateinit var walletViewModel: WalletViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        walletViewModel = ViewModelProvider(this)[WalletViewModel::class.java]

        setSupportActionBar(binding.toolbar)
        val actionbar = supportActionBar
        actionbar?.title = "Add Wallet"
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

        binding.walletSubmit.setOnClickListener {
            binding.walletSubmit.isEnabled = false
            var isWalletNameValid: Boolean = true
            var isWalletDescValid: Boolean = true

            // Check if empty
            isWalletNameValid = ValidateField.notEmptyText(binding.walletName)
            isWalletDescValid = ValidateField.notEmptyText(binding.walletDescription)

            if (isWalletNameValid && isWalletDescValid) {
                val walletName = binding.walletName.text.toString()
                val walletDesc = binding.walletDescription.text.toString()

                // Perform uniqueness check
                val isUniqueLiveData = walletViewModel.isWalletNameUnique(walletName)

                isUniqueLiveData.observe(this) { isUnique ->
                    if (isUnique == true) {
                        // Wallet name is unique, proceed with insertion
                        val calendar = Calendar.getInstance()
                        val year = calendar.get(Calendar.YEAR)
                        val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
                        val month = monthFormat.format(calendar.time)
                        val day = calendar.get(Calendar.DAY_OF_MONTH)

                        val wallet = WalletTable(0, walletName, walletDesc, day, month, year)
                        walletViewModel.addWallet(wallet)
                        Toast.makeText(this, "Wallet Successfully Added!", Toast.LENGTH_SHORT).show()

                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        // Wallet name already exists, show error
                        binding.walletName.error = "Wallet currently exists! Please enter a new name."
                        Toast.makeText(this, "Wallet currently exists! Please enter a new name.", Toast.LENGTH_SHORT).show()
                        binding.walletSubmit.isEnabled = true
                    }

                    // Remove the observer to prevent multiple observations
                    isUniqueLiveData.removeObservers(this)
                }
            } else {
                Toast.makeText(this, "Fields need to be filled properly.", Toast.LENGTH_SHORT).show()
                binding.walletSubmit.isEnabled = true
            }
        }
    }
}