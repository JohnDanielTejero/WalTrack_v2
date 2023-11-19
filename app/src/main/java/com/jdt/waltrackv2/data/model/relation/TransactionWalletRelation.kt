package com.jdt.waltrackv2.data.model.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.jdt.waltrackv2.data.model.TransactionTable
import com.jdt.waltrackv2.data.model.WalletTable

data class TransactionWalletRelation(
    @Embedded val wallet: WalletTable,

    @Relation(
        parentColumn = "walletId",
        entityColumn = "walletId"
    )
    val transactions: List<TransactionTable>
)