package com.jdt.waltrackv2.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jdt.waltrackv2.data.dao.WalletDao
import com.jdt.waltrackv2.data.model.TransactionTable
import com.jdt.waltrackv2.data.model.WalletTable

@Database(entities = [WalletTable::class, TransactionTable::class], version = 2, exportSchema = false)
abstract class WaltrackDb : RoomDatabase(){
    abstract fun walletDao(): WalletDao

    companion object{
        @Volatile
        private var INSTANCE: WaltrackDb? = null

        val migration1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE UNIQUE INDEX index_wallet_walletName ON wallet(walletName)")
            }
        }

        fun getDatabase(context: Context): WaltrackDb? {
            val temp = INSTANCE
            if (temp != null){
                return temp
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WaltrackDb::class.java,
                    name = "WalTrackDb"
                ).addMigrations(migration1_2).build()
                INSTANCE = instance
                return instance
            }

        }
    }
}