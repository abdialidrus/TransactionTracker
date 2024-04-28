package com.abdialidrus.nuta.transactiontracker.domain.use_case

import com.abdialidrus.nuta.transactiontracker.domain.model.Transaction
import com.abdialidrus.nuta.transactiontracker.domain.repository.TransactionRepository

class GetTransaction(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(id: Int): Transaction? {
        return repository.getTransactionById(id)
    }
}