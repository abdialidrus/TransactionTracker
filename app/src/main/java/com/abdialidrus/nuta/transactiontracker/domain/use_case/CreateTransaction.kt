package com.abdialidrus.nuta.transactiontracker.domain.use_case

import com.abdialidrus.nuta.transactiontracker.domain.model.InvalidTransactionException
import com.abdialidrus.nuta.transactiontracker.domain.model.Transaction
import com.abdialidrus.nuta.transactiontracker.domain.repository.TransactionRepository

class CreateTransaction(
    private val repository: TransactionRepository
) {
    @Throws(InvalidTransactionException::class)
    suspend operator fun invoke(transaction: Transaction) {
        if (transaction.date <= 0) {
            throw InvalidTransactionException("Please select transaction date and time")
        }
        if (transaction.amount <= 0) {
            throw InvalidTransactionException("The amount must be greater than 0")
        }
        if (transaction.accountReference.isBlank()) {
            throw InvalidTransactionException("The Account Reference can't be empty")
        }
        if (transaction.senderReceiverName.isBlank()) {
            throw InvalidTransactionException("The Sender/Receiver name can't be empty")
        }

        repository.createTransaction(transaction)
    }
}