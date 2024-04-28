package com.abdialidrus.nuta.transactiontracker.data.repository

import android.util.Log
import com.abdialidrus.nuta.transactiontracker.data.data_source.TransactionDao
import com.abdialidrus.nuta.transactiontracker.domain.model.Transaction
import com.abdialidrus.nuta.transactiontracker.domain.repository.TransactionRepository
import com.abdialidrus.nuta.transactiontracker.util.convertToDateString
import kotlinx.coroutines.flow.Flow

class TransactionRepositoryImpl(
    private val dao: TransactionDao
) : TransactionRepository {
    override fun getTransactionsByDate(startDate: Long, endDate: Long): Flow<List<Transaction>> {
        Log.i("TAG", "getTransactionsByDate: StartDate = ${startDate.convertToDateString()}")
        Log.i("TAG", "getTransactionsByDate: EndDate = ${endDate.convertToDateString()}")
        return dao.getTransactionsByDateRange(startDate, endDate)
    }

    override suspend fun getTransactionById(id: Int): Transaction? {
        return dao.getTransactionById(id)
    }

    override suspend fun createTransaction(transaction: Transaction) {
        dao.insertTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.deleteTransaction(transaction)
    }
}