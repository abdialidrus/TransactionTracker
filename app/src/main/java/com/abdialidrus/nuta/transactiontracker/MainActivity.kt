package com.abdialidrus.nuta.transactiontracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.abdialidrus.nuta.transactiontracker.presentation.transactions.TransactionsScreen
import com.abdialidrus.nuta.transactiontracker.presentation.upsert_transaction.UpsertTransactionScreen
import com.abdialidrus.nuta.transactiontracker.presentation.util.Screen
import com.abdialidrus.nuta.transactiontracker.ui.theme.ExpenseTrackerAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseTrackerAppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.TransactionsScreen.route
                    ) {
                        composable(route = Screen.TransactionsScreen.route) {
                            TransactionsScreen(navController = navController)
                        }
                        composable(
                            route = Screen.UpsertTransactionScreen.route +
                                    "?transactionId={transactionId}",
                            arguments = listOf(
                                navArgument(
                                    name = "transactionId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                            )
                        ) {
                            UpsertTransactionScreen(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}