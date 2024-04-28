package com.abdialidrus.nuta.transactiontracker.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    @PrimaryKey val id: Int? = null,
    val amount: Int,
    val date: Long,
    val type: String, // expense or income
    val accountReference: String, // KAS, BCA, BNI, etc
    val senderReceiverName: String,
    val description: String,
)

class InvalidTransactionException(message: String): Exception(message)