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
import com.jdt.waltrackv2.view.viewholder.ExpenseViewHolder
import com.jdt.waltrackv2.view.viewholder.IncomeViewHolder

class TransactionAdapter  (
    private val context: Context,
    private val handleEvents: ActivityResultLauncher<Intent>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val EXPENSE_VIEW_TYPE = 0
        private const val INCOME_VIEW_TYPE = 1
    }

    private var transactionList = emptyList<TransactionTable>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("transaction", viewType.toString())
        return when (viewType) {

            EXPENSE_VIEW_TYPE -> {
                val expenseView = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
                ExpenseViewHolder(expenseView)
            }
            INCOME_VIEW_TYPE -> {
                val incomeView = LayoutInflater.from(parent.context).inflate(R.layout.item_income, parent, false)
                IncomeViewHolder(incomeView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }
    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = transactionList[position]
        return if (currentItem.transactionType == "Expense") {
            EXPENSE_VIEW_TYPE
        } else {
            INCOME_VIEW_TYPE
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = transactionList[position]

        when (holder){
            is ExpenseViewHolder -> {
                holder.expenseId.text = currentItem.transactionId.toString()
                holder.expenseTitle.text = currentItem.transactionName
                holder.expenseCategory.text = currentItem.transactionTag
                "${currentItem.transactionMonth} ${currentItem.transactionDay}, ${currentItem.transactionYear}".also { holder.expenseDate.text = it }
                holder.expenseAmount.text = "-Php ${currentItem.transactionAmount}"
                holder.expenseRoot.rootView.setOnClickListener{_ ->
                    val intent = Intent(context, ViewTransactionActivity::class.java)
                    intent.putExtra("selectedTransaction", currentItem.transactionId)
                    handleEvents.launch(intent)
                }
            }

            is IncomeViewHolder -> {
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
        }
    }

    fun setData(transaction: List<TransactionTable>){
        this.transactionList = transaction
        notifyDataSetChanged()
    }

}