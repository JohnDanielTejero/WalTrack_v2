package com.jdt.waltrackv2.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jdt.waltrackv2.data.model.WalletTable

@Dao
interface WalletDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWallet(wallet: WalletTable)

    @Query("SELECT * FROM wallet ORDER BY 1 DESC")
    fun getAllWallet() :LiveData<List<WalletTable>>
    @Query("SELECT * FROM wallet WHERE walletName = :name LIMIT 1")
    fun getWalletByName(name: String): LiveData<WalletTable>
    @Query("SELECT * FROM wallet WHERE (:year IS NULL OR walletYear = :year) AND (:month IS NULL OR walletMonth = :month) AND (:day IS NULL OR walletDay = :day) ORDER BY 1 DESC")
    fun getWalletsWithFilter(year: Int?, month: String?, day: Int?): LiveData<List<WalletTable>>
    @Query("SELECT * FROM wallet WHERE walletId = :id LIMIT 1")
    fun getWalletById(id: Int): LiveData<WalletTable>
    @Delete
    suspend fun deleteWallet(wallet: WalletTable)

    @Update
    fun updateWallet(wallet: WalletTable)
}