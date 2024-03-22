package com.jdt.waltrackv2.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.data.view_model.TransactionViewModel
import com.jdt.waltrackv2.data.view_model.WalletViewModel
import com.jdt.waltrackv2.databinding.ActivityViewTransactionBinding
import com.jdt.waltrackv2.databinding.EditItemLayoutBinding
import com.jdt.waltrackv2.utils.ActionButtonHandler
import java.util.zip.Inflater

class ViewTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewTransactionBinding
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var walletViewModel: WalletViewModel

    private lateinit var activityResult: ActivityResultLauncher<Intent>

    private lateinit var editItemLayoutBinding: EditItemLayoutBinding
    private lateinit var actionButtonHandler: ActionButtonHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        walletViewModel = ViewModelProvider(this)[WalletViewModel::class.java]

        loadData()
        editItemLayoutBinding = EditItemLayoutBinding.inflate(layoutInflater, binding.editItemContainer, true)
        activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                loadData()
            }
        }

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

        binding.toolbar.setNavigationOnClickListener{
            finish()
        }
    }

    private fun loadData(){
        transactionViewModel.getTransactionById(intent.getIntExtra("selectedTransaction", -1)).observe(this){ transaction ->

            transaction?.let{
                supportActionBar?.title = transaction.transactionName

                binding.transactionAmount.text = ("Php ${transaction.transactionAmount}")
                binding.transactionCategory.text = transaction.transactionTag
                "${transaction.transactionMonth} ${transaction.transactionDay}, ${transaction.transactionYear}".also { binding.transactionDate.text = it }
                binding.transactionNote.text = transaction.transactionNote
                binding.transactionType.text = transaction.transactionType

                walletViewModel.getWalletById(transaction.walletId).observe(this){ wallet ->
                    binding.transactionWallet.text = wallet.walletName
                }

                binding.deleteTransaction.setOnClickListener{
                    Toast.makeText(this, "Transaction ${transaction.transactionId} : ${transaction.transactionName} successfully deleted!", Toast.LENGTH_SHORT).show()
                    finish()
                    transactionViewModel.deleteTransaction(transaction)
                }

                val intent  = Intent(this, EditTransactionActivity::class.java)
                intent.putExtra("selectedTransaction", transaction.transactionId)
                actionButtonHandler = ActionButtonHandler(editItemLayoutBinding.root, intent, activityResult)
            }

        }
    }


}