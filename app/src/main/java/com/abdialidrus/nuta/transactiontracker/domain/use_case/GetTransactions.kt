package com.abdialidrus.nuta.transactiontracker.domain.use_case

import com.abdialidrus.nuta.transactiontracker.domain.model.Transaction
import com.abdialidrus.nuta.transactiontracker.domain.repository.TransactionRepository
import com.abdialidrus.nuta.transactiontracker.domain.util.OrderType
import com.abdialidrus.nuta.transactiontracker.domain.util.TransactionOrder
import com.abdialidrus.nuta.transactiontracker.util.convertToDateString
import com.abdialidrus.nuta.transactiontracker.util.toTimeMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTransactions(private val repository: TransactionRepository) {
    operator fun invoke(
        startDate: Long,
        endDate: Long,
        transactionOrder: TransactionOrder = TransactionOrder.Date(OrderType.Descending)
    ): Flow<List<Transaction>> {
        val currentDate = System.currentTimeMillis().convertToDateString()
        var finalStartDate = currentDate.toTimeMillis("dd MMMM yyyy")
        var finalEndDate = currentDate.toTimeMillis("dd MMMM yyyy")

        if (startDate > 0L) {
            finalStartDate = startDate
        }
        if (endDate > 0L) {
            finalEndDate = endDate
        }

        if (finalStartDate == finalEndDate) {
            finalEndDate = finalEndDate
        }

        return repository.getTransactionsByDate(finalStartDate, finalEndDate).map { transactions ->
            when (transactionOrder.orderType) {
                is OrderType.Ascending -> {
                    when (transactionOrder) {
                        is TransactionOrder.Date -> transactions.sortedBy { it.date }
                    }
                }

                is OrderType.Descending -> {
                    when(transactionOrder) {
                        is TransactionOrder.Date -> transactions.sortedByDescending { it.date }
                    }
                }
            }
        }
    }
}