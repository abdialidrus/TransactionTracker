package com.abdialidrus.nuta.transactiontracker.domain.util

sealed class OrderType {
    data object Ascending : OrderType()
    data object Descending : OrderType()
}