package com.jdt.waltrackv2.data.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope

import com.jdt.waltrackv2.data.database.WaltrackDb
import com.jdt.waltrackv2.data.model.TransactionTable
import com.jdt.waltrackv2.data.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: TransactionRepository
    init {
        val transactionDao = WaltrackDb.getDatabase(application)?.transactionDao()
        repository = TransactionRepository(transactionDao!!)
    }

    fun getAllTransactions(type: String?=null, limit:Int?=null) : LiveData<List<TransactionTable>>{
        return repository.getAllTransactions(type, limit)
    }

    fun getFilteredTransactions(type: String, walletId: Int?, year: Int?, month: String?, day: Int?): LiveData<List<TransactionTable>> {
        return repository.getFilteredTransactions(type, walletId, year, month, day)
    }

    fun addTransaction(transactionTable: TransactionTable){
        viewModelScope.launch(Dispatchers.IO){
            repository.addTransaction(transactionTable)
        }
    }

    fun getTransactionById(id: Int) : LiveData<TransactionTable>{
        return repository.getTransactionById(id)
    }

    fun getTotal(type: String) : LiveData<Double>{
        return repository.getTotal(type)
    }

    fun deleteTransaction(transactionTable: TransactionTable) {
        viewModelScope.launch {
            repository.deleteTransaction(transactionTable)
        }
    }

    fun updateTransaction(transactionTable: TransactionTable) {
        viewModelScope.launch(Dispatchers.IO){
            repository.updateTransaction(transactionTable)
        }
    }
}