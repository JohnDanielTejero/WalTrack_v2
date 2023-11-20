package com.jdt.waltrackv2.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.jdt.waltrackv2.data.dao.WalletDao
import com.jdt.waltrackv2.data.model.WalletTable

class WalletRepository(private val walletDao: WalletDao) {
    val wallets: LiveData<List<WalletTable>> = walletDao.getAllWallet()
    suspend fun addWallet(walletTable: WalletTable){
        walletDao.insertWallet(walletTable)
    }
    fun isWalletNameUnique(walletName: String): LiveData<Boolean> {
        val resultLiveData = MediatorLiveData<Boolean>()
        resultLiveData.addSource(walletDao.getWalletByName(walletName)) { wallet ->
            resultLiveData.value = wallet == null
            resultLiveData.removeSource(resultLiveData)
        }

        return resultLiveData
    }


    fun getWalletByName(walletName: String): LiveData<WalletTable>{
        return walletDao.getWalletByName(walletName)
    }

    fun getFilteredWallets(year: Int?, month: String?, day:Int?): LiveData<List<WalletTable>> {
        return walletDao.getWalletsWithFilter(year, month, day)
    }

    fun getWalletById(id: Int): LiveData<WalletTable> {
        return walletDao.getWalletById(id)
    }

    suspend fun deleteWallet(wallet: WalletTable){
        walletDao.deleteWallet(wallet)
    }

    fun updateWallet(wallet: WalletTable) {
        walletDao.updateWallet(wallet)
    }
}