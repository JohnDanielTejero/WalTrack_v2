package com.jdt.waltrackv2.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.data.model.WalletTable
import com.jdt.waltrackv2.data.view_model.TransactionViewModel
import com.jdt.waltrackv2.data.view_model.WalletViewModel
import com.jdt.waltrackv2.databinding.DashboardBalanceDisplayBinding
import com.jdt.waltrackv2.databinding.DashboardExpsenseDisplayBinding
import com.jdt.waltrackv2.databinding.DashboardIncomeDisplayBinding
import com.jdt.waltrackv2.view.WalletEditActivity
import com.jdt.waltrackv2.view.fragments.ItemActionsDialog
import com.jdt.waltrackv2.view.viewholder.WalletViewHolder

class WalletAdapter(
    private val context: Context,
    private val walletViewModel: WalletViewModel,
    private val transactionViewModel: TransactionViewModel,
    private val handleWalletEvents: ActivityResultLauncher<Intent>,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<WalletViewHolder>() {
    private var walletList = emptyList<WalletTable>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        return WalletViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wallet, parent, false))
    }

    override fun getItemCount(): Int {
        return walletList.size
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        val currentItem  = walletList[position]
        holder.walletId.text = currentItem.walletId.toString()
        holder.walletName.text = currentItem.walletName
        holder.walletDesc.text = currentItem.walletDesc
        "${currentItem.walletMonth} ${currentItem.walletDay}, ${currentItem.walletYear}".also { holder.walletDate.text = it }

        holder.walletRoot.rootView.setOnClickListener{
            val itemActionsDialog = ItemActionsDialog()
            itemActionsDialog.show((context as FragmentActivity).supportFragmentManager, "ItemActionsDialog")

            itemActionsDialog.setDeleteClickListener {
                Toast.makeText(context, "wallet ${currentItem.walletName} deleted!", Toast.LENGTH_SHORT).show()
                walletViewModel.deleteWallet(currentItem)
            }

            itemActionsDialog.setEditClickListener {
                val intent = Intent(context, WalletEditActivity::class.java)
                intent.putExtra("selectedWallet", currentItem.walletId)
                handleWalletEvents.launch(intent)
            }
        }

        transactionViewModel.getTotal("Expense", currentItem.walletId).observe(viewLifecycleOwner){ expense ->

            val expenseData = expense ?: 0.0

            transactionViewModel.getTotal("Income", currentItem.walletId).observe(viewLifecycleOwner){income ->
                val incomeData = income?: 0.0
                holder.walletBalance.text = "Php ${incomeData- expenseData}"
            }

        }
    }

    fun setData(wallets: List<WalletTable>){
        this.walletList = wallets
        notifyDataSetChanged()
    }
}