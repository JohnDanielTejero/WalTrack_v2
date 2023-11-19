package com.jdt.waltrackv2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.data.model.WalletTable
import com.jdt.waltrackv2.view.viewholder.WalletViewHolder

class WalletAdapter: RecyclerView.Adapter<WalletViewHolder>() {
    private var walletList = emptyList<WalletTable>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        return WalletViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wallet, parent, false))
    }

    override fun getItemCount(): Int {
        return walletList.size
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        val currentItem  = walletList[position]
        holder.walletName.text = currentItem.walletName
        holder.walletDesc.text = currentItem.walletDesc
        holder.walletDate.text = ("${currentItem.walletMonth} ${currentItem.walletDay}, ${currentItem.walletYear}")
    }

    fun setData(wallets: List<WalletTable>){
        this.walletList = wallets
        notifyDataSetChanged()
    }
}