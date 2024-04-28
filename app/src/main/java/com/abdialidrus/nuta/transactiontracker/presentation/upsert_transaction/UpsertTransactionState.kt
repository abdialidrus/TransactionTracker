package com.abdialidrus.nuta.transactiontracker.presentation.upsert_transaction

data class UpsertTransactionState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)