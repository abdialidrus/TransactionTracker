package com.abdialidrus.nuta.transactiontracker.presentation.transactions

import com.abdialidrus.nuta.transactiontracker.domain.model.Transaction
import com.abdialidrus.nuta.transactiontracker.domain.util.OrderType
import com.abdialidrus.nuta.transactiontracker.domain.util.TransactionOrder

data class TransactionsState(
    val transactions: List<Transaction> = emptyList(),
    val transactionOrder: TransactionOrder = TransactionOrder.Date(OrderType.Descending),
    val filterStartDate: Long = 0L,
    val filterEndDate: Long = 0L,
    val isOrderSectionVisible: Boolean = true
)