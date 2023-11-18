package com.jdt.waltrackv2.data.model

class WalletTable {
    companion object {
        //table
        val TABLE_NAME: String = "wallets"

        //primary key
        val COLUMN_WALLET_ID: String = "wallet_id"

        //wallet name
        val COLUMN_WALLET_NAME: String = "name"

        //wallet description
        val COLUMN_WALLET_DESC: String = "desc"

        //for date handling
        val COLUMN_DAY: String = "day"
        val COLUMN_MONTH: String = "month"
        val COLUMN_YEAR: String = "year"

        //create table
        val CREATE_TABLE: String = ("CREATE TABLE ${TABLE_NAME} (" +
                "${COLUMN_WALLET_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${COLUMN_WALLET_NAME} TEXT NOT NULL UNIQUE, " +
                "${COLUMN_WALLET_DESC} TEXT NOT NULL, " +
                "${COLUMN_DAY}INTEGER NOT NULL, " +
                "${COLUMN_MONTH}TEXT NOT NULL, " +
                "${COLUMN_YEAR}INTEGER NOT NULL)")

        val DELETE_TABLE = "DROP TABLE IF EXISTS " + TransactionTable.TABLE_NAME

    }

}