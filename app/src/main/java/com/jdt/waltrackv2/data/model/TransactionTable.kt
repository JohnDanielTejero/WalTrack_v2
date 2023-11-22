package com.jdt.waltrackv2.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = WalletTable::class,
            parentColumns = ["walletId"],
            childColumns = ["walletId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TransactionTable (
    @PrimaryKey(autoGenerate = true)
    val transactionId: Int,
    val transactionName: String,
    val transactionType: String,
    val transactionAmount: Double,
    val transactionNote: String,
    val transactionTag: String,

    val transactionDay: Int,
    val transactionMonth: String,
    val transactionYear: String,

    val walletId: Int
)