package com.abdialidrus.nuta.transactiontracker.presentation.transactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdialidrus.nuta.transactiontracker.domain.model.Transaction
import com.abdialidrus.nuta.transactiontracker.domain.use_case.TransactionUseCases
import com.abdialidrus.nuta.transactiontracker.domain.util.OrderType
import com.abdialidrus.nuta.transactiontracker.domain.util.TransactionOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionsUseCases: TransactionUseCases
) : ViewModel() {

    private val _state = mutableStateOf(TransactionsState())
    val state: State<TransactionsState> = _state

    private var recentlyDeleteTransaction: Transaction? = null

    private var getTransactionsJob: Job? = null

    init {
        onEvent(TransactionsEvent.UpdateFilterStartDate(System.currentTimeMillis()))
        onEvent(TransactionsEvent.UpdateFilterEndDate(System.currentTimeMillis()))
        getTransactions()
    }

    fun onEvent(event: TransactionsEvent) {
        when (event) {
            is TransactionsEvent.GetTransactions -> {
                getTransactions()
            }
            is TransactionsEvent.ToggleOrder -> {
                val currentOrder = state.value.transactionOrder
                _state.value = state.value.copy(
                    transactionOrder = TransactionOrder.Date(if (currentOrder.orderType == OrderType.Ascending) OrderType.Descending else OrderType.Ascending),
                )
                getTransactions()
            }

            is TransactionsEvent.DeleteTransaction -> {
                viewModelScope.launch {
                    transactionsUseCases.deleteTransaction(event.transaction)
                    recentlyDeleteTransaction = event.transaction
                }
            }

            is TransactionsEvent.RestoreTransaction -> {
                viewModelScope.launch {
                    transactionsUseCases.createTransaction(
                        recentlyDeleteTransaction ?: return@launch
                    )
                    recentlyDeleteTransaction = null
                }
            }

            is TransactionsEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }

            is TransactionsEvent.UpdateFilterStartDate -> {
                _state.value = state.value.copy(
                    filterStartDate = event.startDate
                )
            }

            is TransactionsEvent.UpdateFilterEndDate -> {
                _state.value = state.value.copy(
                    filterEndDate = event.endDate
                )
            }
        }
    }

    private fun getTransactions() {
        getTransactionsJob?.cancel()
        val startDate = state.value.filterStartDate
        val endDate = state.value.filterEndDate
        val transactionOrder = state.value.transactionOrder
        getTransactionsJob =
            transactionsUseCases.getTransactions(startDate, endDate, transactionOrder)
                .onEach { transactions ->
                    _state.value = state.value.copy(
                        transactions = transactions, transactionOrder = transactionOrder
                    )
                }.launchIn(viewModelScope)
    }

}