package com.jdt.waltrackv2.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.jdt.waltrackv2.data.model.TransactionTable
import com.jdt.waltrackv2.data.model.WalletTable

class DbConnection private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {
    companion object {
        private var instance : DbConnection? = null;
        private const val DATABASE_NAME : String ="WalTrackDbv2"
        private const val DATABASE_VER: Int = 1

        @Synchronized
        fun getInstance(context: Context) : DbConnection{
            if (instance == null) {
                instance = DbConnection(context.applicationContext)
            }
            return instance!!
        }

    }
    override fun onCreate(db: SQLiteDatabase?) {
       db?.execSQL(WalletTable.CREATE_TABLE)
       db?.execSQL(TransactionTable.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(TransactionTable.DELETE_TABLE)
        db?.execSQL(WalletTable.DELETE_TABLE)
        onCreate(db)
    }
}