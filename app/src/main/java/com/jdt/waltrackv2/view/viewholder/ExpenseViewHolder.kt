package com.jdt.waltrackv2.view.viewholder

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.jdt.waltrackv2.R

class ExpenseViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    val expenseId: TextView = itemView.findViewById(R.id.expense_id)
    val expenseTitle: TextView = itemView.findViewById(R.id.expense_title)
    val expenseCategory: TextView = itemView.findViewById(R.id.expense_details)
    val expenseDate: TextView = itemView.findViewById(R.id.expense_transaction_date)
    val expenseAmount: TextView = itemView.findViewById(R.id.expense_amount)
    val expenseRoot: ConstraintLayout = itemView as ConstraintLayout
}