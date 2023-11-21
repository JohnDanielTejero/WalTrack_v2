package com.jdt.waltrackv2.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.data.model.TransactionTable
import com.jdt.waltrackv2.data.model.WalletTable
import com.jdt.waltrackv2.data.view_model.TransactionViewModel
import com.jdt.waltrackv2.data.view_model.WalletViewModel
import com.jdt.waltrackv2.view.viewholder.ExpenseViewHolder
import com.jdt.waltrackv2.view.viewholder.WalletViewHolder

class ExpenseAdapter(
    private val context: Context,
    private val transactionViewModel: TransactionViewModel,
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
        //TODO: ADD implementations
    }
    fun setData(expense: List<TransactionTable>){
        this.expenseList = expense
        notifyDataSetChanged()
    }
}