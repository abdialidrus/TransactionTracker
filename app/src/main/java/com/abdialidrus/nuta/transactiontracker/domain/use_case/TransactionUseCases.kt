package com.abdialidrus.nuta.transactiontracker.domain.use_case

data class TransactionUseCases(
    val getTransactions: GetTransactions,
    val getTransaction: GetTransaction,
    val createTransaction: CreateTransaction,
    val deleteTransaction: DeleteTransaction
)