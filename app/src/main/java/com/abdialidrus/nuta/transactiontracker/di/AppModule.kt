package com.abdialidrus.nuta.transactiontracker.di

import android.app.Application
import androidx.room.Room
import com.abdialidrus.nuta.transactiontracker.data.data_source.TransactionDatabase
import com.abdialidrus.nuta.transactiontracker.data.repository.TransactionRepositoryImpl
import com.abdialidrus.nuta.transactiontracker.domain.repository.TransactionRepository
import com.abdialidrus.nuta.transactiontracker.domain.use_case.CreateTransaction
import com.abdialidrus.nuta.transactiontracker.domain.use_case.DeleteTransaction
import com.abdialidrus.nuta.transactiontracker.domain.use_case.GetTransaction
import com.abdialidrus.nuta.transactiontracker.domain.use_case.GetTransactions
import com.abdialidrus.nuta.transactiontracker.domain.use_case.TransactionUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTransactionDatabase(app: Application): TransactionDatabase {
        return Room
            .databaseBuilder(
                app,
                TransactionDatabase::class.java,
                TransactionDatabase.DATABASE_NAME
            ).build()
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(db: TransactionDatabase): TransactionRepository {
        return TransactionRepositoryImpl(db.transactionDao)
    }

    @Provides
    @Singleton
    fun provideTransactionUseCases(repository: TransactionRepository): TransactionUseCases {
        return TransactionUseCases(
            getTransactions = GetTransactions(repository),
            getTransaction = GetTransaction(repository),
            createTransaction = CreateTransaction(repository),
            deleteTransaction = DeleteTransaction(repository)
        )
    }

}