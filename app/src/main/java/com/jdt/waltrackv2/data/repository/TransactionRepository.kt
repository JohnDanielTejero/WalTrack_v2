package com.jdt.waltrackv2.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.jdt.waltrackv2.data.dao.TransactionDao
import com.jdt.waltrackv2.data.model.TransactionTable


class TransactionRepository(private val transactionDao: TransactionDao) {

    fun getAllTransactions(type: String?=null, limit: Int?=null): LiveData<List<TransactionTable>> {
        if (limit== null){
            return transactionDao.getAllTransactions(type)
        }
        return transactionDao.getAllTransactionsWithLimit(type, limit)
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

    fun getTotal(type: String, wallet: Int?): LiveData<Double>{
        return transactionDao.getTotal(type, wallet)
    }

    suspend fun deleteTransaction(transaction: TransactionTable){
        transactionDao.deleteTransaction(transaction)
    }

    fun updateTransaction(transaction: TransactionTable) {
        transactionDao.updateTransaction(transaction)
    }
}