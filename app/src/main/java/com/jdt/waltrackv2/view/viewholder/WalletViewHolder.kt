package com.jdt.waltrackv2.view.viewholder

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jdt.waltrackv2.R

class WalletViewHolder (itemView :  View) : RecyclerView.ViewHolder(itemView){

    val walletId: TextView = itemView.findViewById(R.id.wallet_primary_key)
    val walletName: TextView = itemView.findViewById(R.id.wallet_title)
    val walletDesc: TextView = itemView.findViewById(R.id.wallet_details)
    val walletDate: TextView = itemView.findViewById(R.id.wallet_transaction_date)
    val walletRoot: ConstraintLayout = itemView as ConstraintLayout
}