package com.abdialidrus.nuta.transactiontracker.presentation.transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdialidrus.nuta.transactiontracker.presentation.upsert_transaction.components.ReadonlyTextField
import com.abdialidrus.nuta.transactiontracker.util.convertToDateString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    startDateValue: String,
    endDateValue: String,
    onStartDateSelected: (Long?) -> Unit,
    onEndDateSelected: (Long?) -> Unit
) {

    val showDateDialog = remember { mutableStateOf(false) }
    val startDateText = remember {
        mutableStateOf(startDateValue)
    }
    val endDateText = remember {
        mutableStateOf(endDateValue)
    }
    val datePickerState = rememberDatePickerState()
    val dateType = remember { mutableStateOf("start_date") }

    if (showDateDialog.value) {
        DatePickerDialog(
            onDismissRequest = {
                showDateDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (dateType.value == "start_date") {
                            datePickerState.selectedDateMillis?.let {
                                startDateText.value = it.convertToDateString()
                            }
                            onStartDateSelected(datePickerState.selectedDateMillis)
                        } else {
                            datePickerState.selectedDateMillis?.let {
                                endDateText.value = it.convertToDateString()
                            }
                            onEndDateSelected(datePickerState.selectedDateMillis)
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

    Column(
        modifier = modifier
            .height(80.dp)
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ReadonlyTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                value = TextFieldValue(startDateText.value),
                onValueChange = {

                },
                onClick = {
                    dateType.value = "start_date"
                    showDateDialog.value = true
                },
                label = { Text("Start") },
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
                value = TextFieldValue(endDateText.value),
                onValueChange = {

                },
                onClick = {
                    dateType.value = "end_date"
                    showDateDialog.value = true
                },
                label = { Text("End") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Save transaction"
                    )
                },
                trailingIcon = {},
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}