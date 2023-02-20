package com.derich.gitongas.ui.screens.transactions

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.derich.gitongas.R
import com.derich.gitongas.ui.common.composables.CircularProgressBar
import com.derich.gitongas.ui.screens.home.MemberDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun TransactionsComposable(modifier: Modifier = Modifier,
                           transactionsViewModel: TransactionsViewModel,
                           memberInfo: MemberDetails?,
                           navController: NavController) {

    //get data from firebase firestone
    val dataOrException = transactionsViewModel.data.value
    val context = LocalContext.current
    val transactions = dataOrException.data
//    DropdownMenu(expanded = , onDismissRequest = { /*TODO*/ }) {
//
//    }
    Box(modifier = modifier,
        contentAlignment = Alignment.BottomEnd) {
//display all the transactions as a horizontal list
        transactions?.let {
            LazyColumn{
                items(
                    items = transactions
                ){ transaction ->
                    TransactionCard( transaction = transaction,
                        modifier = modifier)
                }
            }
        }
        //check if member is admin and launch addTransaction page
        if (memberInfo!!.phoneNumber == "+254792705723"){
            IconButton(
                onClick = {
                    Toast.makeText(context, "Add Button Clicked", Toast.LENGTH_SHORT).show()
                    transactionsViewModel.launchAddTransactionScreen(navController)
                },
                enabled = true
            )
            {
                Image(
                    painterResource(id = R.drawable.baseline_add),
                    contentDescription = "Add Icon",
                    modifier = Modifier
                        .size(64.dp))
            }
        }
    }

    val e = dataOrException.e
    e?.let {
        Text(text = e.message!!,
            modifier = modifier.padding(16.dp)
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressBar(
            isDisplayed = transactionsViewModel.loading.value
        )

    }
}

@Composable
fun TransactionCard(transaction: Transactions,
                    modifier: Modifier
) {
        Column(horizontalAlignment = Alignment.Start,
            modifier = modifier
                .border(border = BorderStroke(width = 2.dp, color = Color.White))
                .padding(8.dp)
                .fillMaxWidth()) {
            Text(text = transaction.depositFor,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp))
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "Date: ${ transaction.transactionDate }",
                modifier = Modifier.padding(start = 8.dp, end = 8.dp))
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "Amount: KSH ${ transaction.transactionAmount }",
                modifier = Modifier.padding(start = 8.dp, end = 8.dp))
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = transaction.transactionConfirmation,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis)
        }
}
