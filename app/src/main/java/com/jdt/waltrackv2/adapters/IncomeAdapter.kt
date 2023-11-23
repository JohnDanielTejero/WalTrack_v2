package com.jdt.waltrackv2.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.data.model.TransactionTable
import com.jdt.waltrackv2.view.ViewTransactionActivity
import com.jdt.waltrackv2.view.viewholder.IncomeViewHolder

class IncomeAdapter (
    private val context: Context,
    private val handleEvents: ActivityResultLauncher<Intent>
): RecyclerView.Adapter<IncomeViewHolder>() {
    private var incomeList = emptyList<TransactionTable>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder {
        return IncomeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_income, parent, false))
    }

    override fun getItemCount(): Int {
        return incomeList.size
    }

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int) {
        val currentItem  = incomeList[position]
        holder.incomeId.text = currentItem.transactionId.toString()
        holder.incomeTitle.text = currentItem.transactionName
        holder.incomeCategory.text = currentItem.transactionTag
        "${currentItem.transactionMonth} ${currentItem.transactionDay}, ${currentItem.transactionYear}".also { holder.incomeDate.text = it }
        holder.incomeAmount.text = "+Php ${currentItem.transactionAmount}"
        holder.incomeRoot.rootView.setOnClickListener{_ ->
            val intent = Intent(context, ViewTransactionActivity::class.java)
            intent.putExtra("selectedTransaction", currentItem.transactionId)
            handleEvents.launch(intent)
        }

    }

    fun setData(expense: List<TransactionTable>){
        this.incomeList = expense
        notifyDataSetChanged()
    }
}