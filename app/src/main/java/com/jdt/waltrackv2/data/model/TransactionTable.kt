package com.jdt.waltrackv2.data.model

class TransactionTable {
   companion object {
       const val TABLE_NAME:String = "transactions"

       //primary key
       const val COLUMN_TRANSACTION_ID:String = "transaction_id"

       //expense or income
       const val COLUMN_TYPE:String = "type"

       //details
       const val COLUMN_TAG: String = "category"
       const val COLUMN_AMOUNT: String = "amount"
       const val COLUMN_NOTE: String = "note"

       //for date handling
       const val COLUMN_DAY: String = "day"
       const val COLUMN_MONTH: String = "month"
       const val COLUMN_YEAR: String = "year"

       //dependence
       const val COLUMN_WALLET_ID: String = "wallet_id"

       //create table
       val CREATE_TABLE: String = ("CREATE TABLE ${TABLE_NAME} (" +
               "${COLUMN_TRANSACTION_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
               "${COLUMN_TAG} TEXT NOT NULL, " +
               "${COLUMN_TYPE} TEXT NOT NULL, " +
               "${COLUMN_AMOUNT}REAL NOT NULL, " +
               "${COLUMN_NOTE}TEXT NOT NULL, " +
               "${COLUMN_DAY}INTEGER NOT NULL, " +
               "${COLUMN_MONTH}TEXT NOT NULL, " +
               "${COLUMN_YEAR}INTEGER NOT NULL, " +
               "${COLUMN_WALLET_ID}INTEGER NOT NULL, " +
               "FOREIGN KEY ($COLUMN_WALLET_ID) " +
               "REFERENCES ${WalletTable.TABLE_NAME}( ${WalletTable.COLUMN_WALLET_ID}))")

       val DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME

   }



}