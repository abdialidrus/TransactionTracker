package com.abdialidrus.nuta.transactiontracker.presentation.transactions.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.abdialidrus.nuta.transactiontracker.domain.model.Transaction
import com.abdialidrus.nuta.transactiontracker.util.convertToDateString
import com.abdialidrus.nuta.transactiontracker.util.formatWithDefaultDelimiter

@Composable
fun TransactionItem(
    transaction: Transaction,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (transaction.type == "Expense") "Sent to" else "Received from",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = transaction.senderReceiverName,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = transaction.date.convertToDateString(),
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            val amountWithDelimiter = transaction.amount.formatWithDefaultDelimiter()
            val amountText =
                if (transaction.type == "Expense") "-$amountWithDelimiter" else "+$amountWithDelimiter"
            Column(
                modifier.fillMaxSize(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = amountText,
                    style = MaterialTheme.typography.h6,
                    color = if (transaction.type == "Expense") MaterialTheme.colors.error else Color.Green,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                IconButton(
                    onClick = onDeleteClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete note",
                        tint = Color.Gray
                    )
                }
            }

        }

    }
}