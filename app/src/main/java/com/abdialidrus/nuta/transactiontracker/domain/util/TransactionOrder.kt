package com.abdialidrus.nuta.transactiontracker.domain.util

sealed class TransactionOrder(val orderType: OrderType) {
    class Date(orderType: OrderType): TransactionOrder(orderType)

    fun copy(orderType: OrderType): TransactionOrder {
        return when(this) {
            is Date -> Date(orderType)
        }
    }
}