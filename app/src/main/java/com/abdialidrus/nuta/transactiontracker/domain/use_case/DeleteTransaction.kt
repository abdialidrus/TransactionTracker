package com.abdialidrus.nuta.transactiontracker.domain.use_case

import com.abdialidrus.nuta.transactiontracker.domain.model.Transaction
import com.abdialidrus.nuta.transactiontracker.domain.repository.TransactionRepository

class DeleteTransaction(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.deleteTransaction(transaction)
    }
}