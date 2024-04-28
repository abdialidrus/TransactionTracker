package com.abdialidrus.nuta.transactiontracker.presentation.transactions

import com.abdialidrus.nuta.transactiontracker.domain.model.Transaction

sealed class TransactionsEvent {
    data object GetTransactions: TransactionsEvent()
    data object ToggleOrder: TransactionsEvent()
    data class DeleteTransaction(val transaction: Transaction): TransactionsEvent()
    data class UpdateFilterStartDate(val startDate: Long): TransactionsEvent()
    data class UpdateFilterEndDate(val endDate: Long): TransactionsEvent()
    data object RestoreTransaction: TransactionsEvent()
    data object ToggleOrderSection: TransactionsEvent()
}