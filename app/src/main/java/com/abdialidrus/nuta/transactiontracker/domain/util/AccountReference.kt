package com.abdialidrus.nuta.transactiontracker.domain.util

sealed class AccountReference(val name: String) {
    data object CASH : AccountReference("Kas")
    data object BCA : AccountReference("BCA")
    data object BNI : AccountReference("BNI")
}