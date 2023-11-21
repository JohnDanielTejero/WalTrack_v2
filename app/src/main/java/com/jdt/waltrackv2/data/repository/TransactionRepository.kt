package com.jdt.waltrackv2.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.jdt.waltrackv2.data.dao.TransactionDao
import com.jdt.waltrackv2.data.model.TransactionTable


class TransactionRepository(private val transactionDao: TransactionDao) {

    fun getAllTransactions(type: String?=null): LiveData<List<TransactionTable>> {
        return transactionDao.getAllTransactions(type)
    }
    suspend fun addTransaction(transaction: TransactionTable){
        transactionDao.insertTransaction(transaction)
    }


    fun getFilteredTransactions(type:String, walletId:Int?, year: Int?, month: String?, day:Int?): LiveData<List<TransactionTable>> {
        return transactionDao.getTransactionsWithFilter(type, walletId, year, month, day)
    }

    fun getTransactionById(id: Int): LiveData<TransactionTable> {
        return transactionDao.getTransactionById(id)
    }

    suspend fun deleteTransaction(transaction: TransactionTable){
        transactionDao.deleteTransaction(transaction)
    }

    fun updateTransaction(transaction: TransactionTable) {
        transactionDao.updateTransaction(transaction)
    }
}