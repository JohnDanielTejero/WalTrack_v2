package com.jdt.waltrackv2.view.viewholder

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.jdt.waltrackv2.R

class IncomeViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
    val incomeId: TextView = itemView.findViewById(R.id.income_id)
    val incomeTitle: TextView = itemView.findViewById(R.id.income_title)
    val incomeCategory: TextView = itemView.findViewById(R.id.income_details)
    val incomeDate: TextView = itemView.findViewById(R.id.income_transaction_date)
    val incomeAmount: TextView = itemView.findViewById(R.id.income_amount)
    val incomeRoot: ConstraintLayout = itemView as ConstraintLayout
}