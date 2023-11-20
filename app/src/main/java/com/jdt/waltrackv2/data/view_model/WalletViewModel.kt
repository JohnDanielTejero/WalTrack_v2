package com.jdt.waltrackv2.data.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jdt.waltrackv2.data.database.WaltrackDb
import com.jdt.waltrackv2.data.model.WalletTable
import com.jdt.waltrackv2.data.repository.WalletRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WalletViewModel (application: Application) : AndroidViewModel(application) {

    val wallets: LiveData<List<WalletTable>>
    private val repository: WalletRepository

    init {
        val walletDao = WaltrackDb.getDatabase(application)?.walletDao()
        repository = WalletRepository(walletDao!!)
        wallets = repository.wallets
    }

    fun getFilteredWallets(year: Int?, month: String?, day: Int?): LiveData<List<WalletTable>> {
        return repository.getFilteredWallets(year, month, day)
    }
    fun addWallet(walletTable: WalletTable){
        viewModelScope.launch(Dispatchers.IO){
            repository.addWallet(walletTable)
        }
    }

    fun isWalletNameUnique(walletName: String):  LiveData<Boolean>  {
        return repository.isWalletNameUnique(walletName)
    }

    fun getWalletByName(walletName : String): LiveData<WalletTable> {
        return repository.getWalletByName(walletName)
    }

    fun getWalletById(id: Int) : LiveData<WalletTable>{
        return repository.getWalletById(id)
    }

    fun deleteWallet(wallet: WalletTable) {
        viewModelScope.launch {
            repository.deleteWallet(wallet)
        }
    }

    fun updateWallet(wallet: WalletTable) {
        viewModelScope.launch(Dispatchers.IO){
            repository.updateWallet(wallet)
        }
    }

}