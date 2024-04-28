package com.abdialidrus.nuta.transactiontracker.presentation.transactions

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abdialidrus.nuta.transactiontracker.presentation.transactions.components.OrderSection
import com.abdialidrus.nuta.transactiontracker.presentation.transactions.components.TransactionItem
import com.abdialidrus.nuta.transactiontracker.presentation.util.Screen
import com.abdialidrus.nuta.transactiontracker.util.convertToDateString
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    navController: NavController,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Transaction Tracker")
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(TransactionsEvent.ToggleOrderSection)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Sort"
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.onEvent(TransactionsEvent.ToggleOrder)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.UpsertTransactionScreen.route)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add transaction")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth(),
                    startDateValue = state.filterStartDate.convertToDateString(),
                    endDateValue = state.filterEndDate.convertToDateString(),
                    { startDate ->
                        startDate?.let { date ->
                            viewModel.onEvent(TransactionsEvent.UpdateFilterStartDate(date))
                            viewModel.onEvent(TransactionsEvent.GetTransactions)
                        }
                    },
                    { endDate ->
                        endDate?.let { date ->
                            viewModel.onEvent(TransactionsEvent.UpdateFilterEndDate(date))
                            viewModel.onEvent(TransactionsEvent.GetTransactions)
                        }
                    }
                )
            }
            if (state.transactions.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No transactions on the selected date range"
                    )
                }
            }
            LazyColumn() {
                items(state.transactions) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.UpsertTransactionScreen.route +
                                            "?transactionId=${transaction.id}"
                                )
                            },
                        onDeleteClick = {
                            viewModel.onEvent(TransactionsEvent.DeleteTransaction(transaction))
                            scope.launch {
                                val result = snackBarHostState.showSnackbar(
                                    message = "Note deleted",
                                    actionLabel = "Undo"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(TransactionsEvent.RestoreTransaction)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}