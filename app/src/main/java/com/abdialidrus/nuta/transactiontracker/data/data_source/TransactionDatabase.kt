package com.abdialidrus.nuta.transactiontracker.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abdialidrus.nuta.transactiontracker.domain.model.Transaction

@Database(
    entities = [Transaction::class],
    version = 1
)
abstract class TransactionDatabase: RoomDatabase() {

    abstract val transactionDao: TransactionDao

    companion object {
        const val DATABASE_NAME = "transaction_db"
    }
}