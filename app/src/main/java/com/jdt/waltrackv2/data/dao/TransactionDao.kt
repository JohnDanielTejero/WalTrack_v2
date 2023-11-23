package com.jdt.waltrackv2.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jdt.waltrackv2.data.model.TransactionTable
@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTransaction(transaction: TransactionTable)
    @Query("SELECT * FROM transactions WHERE (:transactionType IS NULL OR transactionType = :transactionType) ORDER BY transactionYear DESC, " +
            "CASE transactionMonth " +
            "        WHEN 'Jan' THEN 1" +
            "        WHEN 'Feb' THEN 2" +
            "        WHEN 'Mar' THEN 3" +
            "        WHEN 'Apr' THEN 4" +
            "        WHEN 'May' THEN 5" +
            "        WHEN 'Jun' THEN 6" +
            "        WHEN 'Jul' THEN 7" +
            "        WHEN 'Aug' THEN 8" +
            "        WHEN 'Sep' THEN 9" +
            "        WHEN 'Oct' THEN 10" +
            "        WHEN 'Nov' THEN 11" +
            "        WHEN 'Dec' THEN 12" +
            "        ELSE 12" +
            " END DESC, transactionDay DESC, transactionId DESC")
    fun getAllTransactions(transactionType: String?) : LiveData<List<TransactionTable>>
    @Query("SELECT * FROM transactions WHERE (:transactionType IS NULL OR transactionType = :transactionType) ORDER BY transactionYear DESC, " +
            "CASE transactionMonth" +
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
            " END DESC, transactionDay DESC, transactionId DESC LIMIT :limit")
    fun getAllTransactionsWithLimit(transactionType: String?, limit: Int) : LiveData<List<TransactionTable>>
    @Query("SELECT * FROM transactions WHERE transactionType = :transactionType AND (:walletId IS NULL OR walletId = :walletId) AND (:year IS NULL OR transactionYear = :year) AND (:month IS NULL OR transactionMonth = :month) AND (:day IS NULL OR transactionDay = :day) ORDER BY transactionYear DESC, " +
            "CASE transactionMonth" +
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
            " END DESC, transactionDay DESC")
    fun getTransactionsWithFilter(transactionType: String, walletId: Int?, year: Int?, month: String?, day: Int?): LiveData<List<TransactionTable>>
    @Query("SELECT * FROM transactions WHERE transactionId = :id LIMIT 1")
    fun getTransactionById(id: Int): LiveData<TransactionTable>
    @Delete
    suspend fun deleteTransaction(transaction: TransactionTable)

    @Update
    fun updateTransaction(transaction: TransactionTable)
}