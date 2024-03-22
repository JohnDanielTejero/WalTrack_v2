package com.jdt.waltrackv2.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.TypedValue
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.data.model.WalletTable
import com.jdt.waltrackv2.data.view_model.WalletViewModel
import com.jdt.waltrackv2.databinding.ActivityWalletEditBinding
import com.jdt.waltrackv2.utils.ActionButtonHandler
import com.jdt.waltrackv2.utils.ValidateField
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WalletEditActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWalletEditBinding
    private lateinit var walletViewModel: WalletViewModel

    private var selectedWallet: WalletTable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletEditBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)

        val actionbar = supportActionBar
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

        walletViewModel = ViewModelProvider(this)[WalletViewModel::class.java]
        binding.toolbar.setNavigationOnClickListener{
            finish()
        }

        walletViewModel.getWalletById(intent.getIntExtra("selectedWallet", -1)).observe(this){
            selectedWallet = it
            binding.walletName.setText(it.walletName)
            binding.walletDescription.setText(it.walletDesc)
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

                val isNameChanged = walletName != selectedWallet?.walletName

                if (isNameChanged) {
                    val isUniqueLiveData = walletViewModel.isWalletNameUnique(walletName)

                    isUniqueLiveData.observe(this) { isUnique ->
                        if (isUnique == true) {
                            selectedWallet?.walletName = walletName
                            selectedWallet?.walletDesc = walletDesc

                            walletViewModel.updateWallet(selectedWallet!!)
                            Toast.makeText(this, "Wallet Successfully Updated!", Toast.LENGTH_SHORT).show()

                            val intent = Intent()
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        } else {

                            binding.walletName.error = "Wallet currently exists! Please enter a new name."
                            Toast.makeText(this, "Wallet currently exists! Please enter a new name.", Toast.LENGTH_SHORT).show()
                            binding.walletSubmit.isEnabled = true
                        }

                        isUniqueLiveData.removeObservers(this)
                    }
                }else{
                    selectedWallet?.walletDesc = walletDesc

                    walletViewModel.updateWallet(selectedWallet!!)
                    Toast.makeText(this, "Wallet Successfully Updated!", Toast.LENGTH_SHORT).show()

                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }

            } else {
                Toast.makeText(this, "Fields need to be filled properly.", Toast.LENGTH_SHORT).show()
                binding.walletSubmit.isEnabled = true
            }
        }
    }


}