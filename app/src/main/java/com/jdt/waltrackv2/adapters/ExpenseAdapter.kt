package com.jdt.waltrackv2.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.data.model.TransactionTable
import com.jdt.waltrackv2.data.model.WalletTable
import com.jdt.waltrackv2.data.view_model.TransactionViewModel
import com.jdt.waltrackv2.data.view_model.WalletViewModel
import com.jdt.waltrackv2.view.AddWalletActivity
import com.jdt.waltrackv2.view.WalletEditActivity
import com.jdt.waltrackv2.view.fragments.ItemActionsDialog
import com.jdt.waltrackv2.view.viewholder.ExpenseViewHolder
import com.jdt.waltrackv2.view.viewholder.WalletViewHolder

class ExpenseAdapter(
    private val context: Context,
    private val handleEvents: ActivityResultLauncher<Intent>
): RecyclerView.Adapter<ExpenseViewHolder>() {
    private var expenseList = emptyList<TransactionTable>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        return ExpenseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false))
    }

    override fun getItemCount(): Int {
       return expenseList.size
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val currentItem  = expenseList[position]
        holder.expenseId.text = currentItem.transactionId.toString()
        holder.expenseTitle.text = currentItem.transactionName
        holder.expenseCategory.text = currentItem.transactionTag
        "${currentItem.transactionMonth} ${currentItem.transactionDay}, ${currentItem.transactionYear}".also { holder.expenseDate.text = it }
        holder.expenseAmount.text = "-Php ${currentItem.transactionAmount}"
        holder.itemView.rootView.setOnClickListener{
            //TODO: add proper view for wallet activity
            val intent = Intent(context, AddWalletActivity::class.java)
            intent.putExtra("selectedTransaction", currentItem.transactionId)
            handleEvents.launch(intent)
        }

    }

    fun setData(expense: List<TransactionTable>){
        this.expenseList = expense
        notifyDataSetChanged()
    }
}