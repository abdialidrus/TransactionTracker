package com.abdialidrus.nuta.transactiontracker.domain.repository

import com.abdialidrus.nuta.transactiontracker.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactionsByDate(startDate: Long, endDate: Long): Flow<List<Transaction>>

    suspend fun getTransactionById(id: Int): Transaction?

    suspend fun createTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)
}