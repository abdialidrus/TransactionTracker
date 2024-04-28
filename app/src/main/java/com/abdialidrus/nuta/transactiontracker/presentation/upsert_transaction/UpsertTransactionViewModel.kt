package com.abdialidrus.nuta.transactiontracker.presentation.upsert_transaction

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdialidrus.nuta.transactiontracker.domain.model.InvalidTransactionException
import com.abdialidrus.nuta.transactiontracker.domain.model.Transaction
import com.abdialidrus.nuta.transactiontracker.domain.use_case.TransactionUseCases
import com.abdialidrus.nuta.transactiontracker.util.convertToDateString
import com.abdialidrus.nuta.transactiontracker.util.convertToTimeString
import com.abdialidrus.nuta.transactiontracker.util.toTimeMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpsertTransactionViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _transactionDate = mutableStateOf(UpsertTransactionState(
        hint = "Select transaction date"
    ))
    val transactionDate: State<UpsertTransactionState> = _transactionDate

    private val _transactionTime = mutableStateOf(UpsertTransactionState(
        hint = "Select transaction time"
    ))
    val transactionTime: State<UpsertTransactionState> = _transactionTime

    private val _transactionType = mutableStateOf(UpsertTransactionState(
        hint = "Select type of the transaction"
    ))
    val transactionType: State<UpsertTransactionState> = _transactionType

    private val _transactionAccountRef = mutableStateOf(UpsertTransactionState(
        hint = "Select Account Reference"
    ))
    val transactionAccountRef: State<UpsertTransactionState> = _transactionAccountRef

    private val _transactionAmount = mutableStateOf(UpsertTransactionState(
        hint = "Enter amount"
    ))
    val transactionAmount: State<UpsertTransactionState> = _transactionAmount

    private val _transactionSenderReceiverName = mutableStateOf(UpsertTransactionState(
        hint = "Enter sender/receiver name"
    ))
    val transactionSenderReceiverName: State<UpsertTransactionState> = _transactionSenderReceiverName

    private val _transactionDescription = mutableStateOf(UpsertTransactionState(
        hint = "Enter description"
    ))
    val transactionDescription: State<UpsertTransactionState> = _transactionDescription

    private var currentTransactionId: Int? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val accountReferences = listOf("KAS", "BCA", "BNI", "MANDIRI")
    val transactionTypes = listOf("Income", "Expense")

    sealed class UiEvent {
        data class ShowSnackBar(val message: String): UiEvent()
        data object SaveTransaction: UiEvent()
    }

    init {
        savedStateHandle.get<Int>("transactionId")?.let { transactionId ->
            if(transactionId != -1) {
                viewModelScope.launch {
                    transactionUseCases.getTransaction(transactionId)?.also { transaction ->
                        currentTransactionId = transaction.id
                        loadTransactionDate(transaction.date)
                        _transactionAmount.value = transactionAmount.value.copy(
                            text = transaction.amount.toString()
                        )
                        _transactionSenderReceiverName.value = transactionSenderReceiverName.value.copy(
                            text = transaction.senderReceiverName,
                        )
                        _transactionType.value = transactionType.value.copy(
                            text = transaction.type,
                        )
                        _transactionAccountRef.value = transactionAccountRef.value.copy(
                            text = transaction.accountReference,
                        )
                        _transactionDescription.value = transactionDescription.value.copy(
                            text = transaction.description,
                        )
                    }
                }
            }
        }
    }

    private fun loadTransactionDate(date: Long) {
        val dateText = date.convertToDateString()
        val timeText = date.convertToTimeString()
        _transactionDate.value = transactionDate.value.copy(
            text = dateText
        )
        _transactionTime.value = transactionTime.value.copy(
            text = timeText
        )
    }

    fun onEvent(event: UpsertTransactionEvent) {
        when(event) {
            is UpsertTransactionEvent.EnteredDate -> {
                _transactionDate.value = transactionDate.value.copy(
                    text = event.value
                )
            }
            is UpsertTransactionEvent.EnteredTime -> {
                _transactionTime.value = transactionTime.value.copy(
                    text = event.value
                )
            }
            is UpsertTransactionEvent.EnteredAmount -> {
                _transactionAmount.value = transactionAmount.value.copy(
                    text = event.value
                )
            }
            is UpsertTransactionEvent.EnteredAccountRef -> {
                _transactionAccountRef.value = transactionAccountRef.value.copy(
                    text = event.value
                )
            }
            is UpsertTransactionEvent.EnteredType -> {
                _transactionType.value = transactionType.value.copy(
                    text = event.value
                )
            }
            is UpsertTransactionEvent.EnteredSenderReceiverName -> {
                _transactionSenderReceiverName.value = transactionSenderReceiverName.value.copy(
                    text = event.value
                )
            }
            is UpsertTransactionEvent.EnteredDescription-> {
                _transactionDescription.value = transactionDescription.value.copy(
                    text = event.value
                )
            }
            is UpsertTransactionEvent.SaveTransaction -> {
                viewModelScope.launch {
                    try {
                        transactionUseCases.createTransaction(
                            Transaction(
                                id = currentTransactionId,
                                amount = transactionAmount.value.text.toIntOrNull() ?: 0,
                                date = formatDateAndTime(),
                                type = transactionType.value.text,
                                accountReference = transactionAccountRef.value.text,
                                senderReceiverName = transactionSenderReceiverName.value.text,
                                description = transactionDescription.value.text,
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveTransaction)
                    } catch(e: InvalidTransactionException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Couldn't save transaction"
                            )
                        )
                    }
                }
            }
        }
    }

    fun getPageTitle(): String {
        currentTransactionId?.let {
            return "Update transaction"
        }

        return "Create new transaction"
    }

    private fun formatDateAndTime(): Long {
        val date = transactionDate.value.text
        val time = transactionTime.value.text

        if (date.isEmpty() || time.isEmpty()) return 0L

        val dateAndTime = "$date $time"

        return dateAndTime.toTimeMillis("dd MMMM yyyy HH:mm")
    }
}