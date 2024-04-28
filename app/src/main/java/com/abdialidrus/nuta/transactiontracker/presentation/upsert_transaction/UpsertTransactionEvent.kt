package com.abdialidrus.nuta.transactiontracker.presentation.upsert_transaction

sealed class UpsertTransactionEvent {
    data class EnteredAmount(val value: String) : UpsertTransactionEvent()
    data class EnteredDate(val value: String) : UpsertTransactionEvent()
    data class EnteredTime(val value: String) : UpsertTransactionEvent()
    data class EnteredAccountRef(val value: String) : UpsertTransactionEvent()
    data class EnteredType(val value: String) : UpsertTransactionEvent()
    data class EnteredSenderReceiverName(val value: String) : UpsertTransactionEvent()
    data class EnteredDescription(val value: String) : UpsertTransactionEvent()
    data object SaveTransaction : UpsertTransactionEvent()
}