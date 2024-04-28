package com.abdialidrus.nuta.transactiontracker.presentation.upsert_transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abdialidrus.nuta.transactiontracker.presentation.upsert_transaction.components.ReadonlyTextField
import com.abdialidrus.nuta.transactiontracker.util.convertToDateString
import com.abdialidrus.nuta.transactiontracker.util.formatWithDefaultDelimiter
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertTransactionScreen(
    navController: NavController,
    viewModel: UpsertTransactionViewModel = hiltViewModel()
) {
    val dateState = viewModel.transactionDate.value
    val timeState = viewModel.transactionTime.value
    val amountState = viewModel.transactionAmount.value
    val typeState = viewModel.transactionType.value
    val accountRefState = viewModel.transactionAccountRef.value
    val senderReceiverNameState = viewModel.transactionSenderReceiverName.value
    val descriptionState = viewModel.transactionDescription.value

    val snackBarHostState = remember { SnackbarHostState() }

    val datePickerState = rememberDatePickerState()
    val showDateDialog = remember { mutableStateOf(false) }
    val showTimeDialog = remember { mutableStateOf(false) }
    val showAccountRefOptions = remember { mutableStateOf(false) }
    val showTypeOptions = remember { mutableStateOf(false) }

    if (showDateDialog.value) {
        DatePickerDialog(
            onDismissRequest = {
                showDateDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val formattedDate = datePickerState.selectedDateMillis?.convertToDateString()
                        formattedDate?.let {
                            viewModel.onEvent(UpsertTransactionEvent.EnteredDate(it))
                        }

                        showDateDialog.value = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDateDialog.value = false
                    }
                ) {
                    Text("CANCEL")
                }
            },
            colors = DatePickerDefaults.colors()
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }

    if (showTimeDialog.value) {
        CustomTimePickerDialog(setShowDialog = {
            showTimeDialog.value = it
        }) {
            viewModel.onEvent(UpsertTransactionEvent.EnteredTime(it))
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UpsertTransactionViewModel.UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UpsertTransactionViewModel.UiEvent.SaveTransaction -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(viewModel.getPageTitle())
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(UpsertTransactionEvent.SaveTransaction)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save transaction")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceEvenly

        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ReadonlyTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    value = TextFieldValue(dateState.text),
                    onValueChange = {
                        viewModel.onEvent(UpsertTransactionEvent.EnteredDate(it.text))
                    },
                    onClick = { showDateDialog.value = true },
                    label = { Text("Select Date") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Save transaction"
                        )
                    },
                    trailingIcon = {},
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                ReadonlyTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    value = TextFieldValue(timeState.text),
                    onValueChange = {
                        viewModel.onEvent(UpsertTransactionEvent.EnteredTime(it.text))
                    },
                    onClick = { showTimeDialog.value = true },
                    label = { Text("Select Time") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "transaction time"
                        )
                    },
                    trailingIcon = {},
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                value = amountState.text,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                ),
                onValueChange = {
                    viewModel.onEvent(UpsertTransactionEvent.EnteredAmount(it))
                },
                label = { Text("Amount") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "Save transaction"
                    )
                },
                visualTransformation = NumberDelimiterTransformation(),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column {
                ReadonlyTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    value = TextFieldValue(accountRefState.text),
                    onValueChange = {
                        viewModel.onEvent(UpsertTransactionEvent.EnteredAccountRef(it.text))
                    },
                    onClick = { showAccountRefOptions.value = true },
                    label = { Text("Account Reference") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = "transaction time"
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown Arrow"
                        )
                    },
                    fontSize = 12.sp
                )
                DropdownMenu(
                    expanded = showAccountRefOptions.value,
                    onDismissRequest = { showAccountRefOptions.value = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                ) {
                    viewModel.accountReferences.forEach { label ->
                        DropdownMenuItem(text = { Text(text = label) }, onClick = {
                            viewModel.onEvent(UpsertTransactionEvent.EnteredAccountRef(label))
                            showAccountRefOptions.value = false
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Column {
                ReadonlyTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    value = TextFieldValue(typeState.text),
                    onValueChange = {
                        viewModel.onEvent(UpsertTransactionEvent.EnteredType(it.text))
                    },
                    onClick = { showTypeOptions.value = true },
                    label = { Text("Transaction Type") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Category,
                            contentDescription = "transaction type"
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown Arrow"
                        )
                    },
                    fontSize = 12.sp
                )
                DropdownMenu(
                    expanded = showTypeOptions.value,
                    onDismissRequest = { showTypeOptions.value = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                ) {
                    viewModel.transactionTypes.forEach { label ->
                        DropdownMenuItem(text = { Text(text = label) }, onClick = {
                            viewModel.onEvent(UpsertTransactionEvent.EnteredType(label))
                            showTypeOptions.value = false
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                value = senderReceiverNameState.text,
                onValueChange = {
                    viewModel.onEvent(UpsertTransactionEvent.EnteredSenderReceiverName(it))
                },
                label = { Text(if (viewModel.transactionType.value.text == "Expense") "Receiver Name" else "Sender Name") },
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                value = descriptionState.text,
                onValueChange = {
                    viewModel.onEvent(UpsertTransactionEvent.EnteredDescription(it))
                },
                label = { Text("Description") },
            )
            Spacer(modifier = Modifier.height(12.dp))


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePickerDialog(
    setShowDialog: (Boolean) -> Unit,
    setValue: (String) -> Unit
) {
    val state = rememberTimePickerState()

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                Modifier.padding(12.dp)
            ) {
                TimePicker(
                    state = state,
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            setShowDialog(false)
                        }) {
                        Text(text = "CANCEL")
                    }
                    TextButton(
                        onClick = {
                            var hour = state.hour.toString()
                            var minute = state.minute.toString()
                            if (state.hour < 10) {
                                hour = "0$hour"
                            }
                            if (state.minute < 10) {
                                minute = "0$minute"
                            }
                            setValue("$hour:$minute")
                            setShowDialog(false)
                        }) {
                        Text(text = "OK")
                    }

                }
            }
        }
    }
}

class NumberDelimiterTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString(text.text.toLongOrNull().formatWithDefaultDelimiter()),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return text.text.toLongOrNull().formatWithDefaultDelimiter().length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return text.length
                }
            }
        )
    }
}