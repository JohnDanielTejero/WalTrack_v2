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

    @Query("SELECT * FROM wallet ORDER BY walletYear DESC, CASE walletMonth" +
            " WHEN 'Jan' THEN 1" +
            " WHEN 'Feb' THEN 2" +
            " WHEN 'Mar' THEN 3" +
            " WHEN 'Apr' THEN 4" +
            " WHEN 'May' THEN 5" +
            " WHEN 'Jun' THEN 6" +
            " WHEN 'Jul' THEN 7" +
            " WHEN 'Aug' THEN 8" +
            " WHEN 'Sep' THEN 9" +
            " WHEN 'Oct' THEN 10" +
            " WHEN 'Nov' THEN 11" +
            " WHEN 'Dec' THEN 12" +
            " ELSE 12" +
            " END DESC, walletDay DESC")
    fun getAllWallet() :LiveData<List<WalletTable>>
    @Query("SELECT * FROM wallet WHERE walletName = :name LIMIT 1")
    fun getWalletByName(name: String): LiveData<WalletTable>
    @Query("SELECT * FROM wallet WHERE (:year IS NULL OR walletYear = :year) AND (:month IS NULL OR walletMonth = :month) AND (:day IS NULL OR walletDay = :day) ORDER BY walletYear DESC," +
            "CASE walletMonth" +
            "  WHEN 'Jan' THEN 1" +
            " WHEN 'Feb' THEN 2" +
            " WHEN 'Mar' THEN 3" +
            " WHEN 'Apr' THEN 4" +
            " WHEN 'May' THEN 5" +
            " WHEN 'Jun' THEN 6" +
            " WHEN 'Jul' THEN 7" +
            " WHEN 'Aug' THEN 8" +
            " WHEN 'Sep' THEN 9" +
            " WHEN 'Oct' THEN 10" +
            " WHEN 'Nov' THEN 11" +
            " WHEN 'Dec' THEN 12" +
            " ELSE 12" +
            " END DESC, walletDay DESC")
    fun getWalletsWithFilter(year: Int?, month: String?, day: Int?): LiveData<List<WalletTable>>
    @Query("SELECT * FROM wallet WHERE walletId = :id LIMIT 1")
    fun getWalletById(id: Int): LiveData<WalletTable>
    @Delete
    suspend fun deleteWallet(wallet: WalletTable)

    @Update
    fun updateWallet(wallet: WalletTable)
}