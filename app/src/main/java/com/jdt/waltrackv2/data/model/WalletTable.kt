package com.jdt.waltrackv2.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "wallet",
    indices = [Index(value = ["walletName"], unique = true)])
data class WalletTable(
    @PrimaryKey(autoGenerate = true)
    val walletId: Int,

    val walletName: String,
    val walletDesc: String,
    val walletDay: Int,
    val walletMonth: String,
    val walletYear: Int
)