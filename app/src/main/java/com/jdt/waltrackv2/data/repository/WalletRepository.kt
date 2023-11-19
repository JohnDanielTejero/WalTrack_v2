package com.jdt.waltrackv2.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdt.waltrackv2.data.dao.WalletDao
import com.jdt.waltrackv2.data.model.WalletTable

class WalletRepository(private val walletDao: WalletDao) {
    val wallets: LiveData<List<WalletTable>> = walletDao.getAllWallet()
    suspend fun addWallet(walletTable: WalletTable){
        walletDao.insertWallet(walletTable)
    }
    fun isWalletNameUnique(walletName: String): Boolean {
        return walletDao.getWalletByName(walletName) == null
    }

    fun getFilteredWallets(year: Int?, month: String?, day:Int?): LiveData<List<WalletTable>> {
        return walletDao.getWalletsWithFilter(year, month, day)
    }
}