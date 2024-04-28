package com.abdialidrus.nuta.transactiontracker.presentation.util

sealed class Screen(val route: String) {
    data object TransactionsScreen: Screen("transactions_screen")
    data object UpsertTransactionScreen: Screen("upsert_transaction_screen")
}