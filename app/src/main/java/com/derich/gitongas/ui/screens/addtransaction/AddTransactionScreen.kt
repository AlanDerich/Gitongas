package com.derich.gitongas.ui.screens.addtransaction

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.derich.gitongas.ui.common.composables.CircularProgressBar
import com.derich.gitongas.ui.screens.home.MemberDetails
import com.derich.gitongas.ui.screens.transactions.Transactions
import com.derich.gitongas.ui.screens.transactions.TransactionsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@Composable
fun AddTransactionScreen(transactionsViewModel: TransactionsViewModel,
                         allMemberInfo: List<MemberDetails>,
                         modifier: Modifier = Modifier,
                         navController: NavController) {
//    var requestToOpen by remember { mutableStateOf(false) }
    Column(modifier = modifier.padding(8.dp)) {
        var selectedMember by remember { mutableStateOf(allMemberInfo[0]) }
        val isOpen = rememberSaveable { mutableStateOf(false) } // initial value
        val openCloseOfDropDownList: (Boolean) -> Unit = {
            isOpen.value = it
        }
        val userSelectedString: (MemberDetails) -> Unit = {
            selectedMember = it
        }
        Box {
            Column {
                OutlinedTextField(
                    value = selectedMember.fullNames,
                    onValueChange = { selectedMember.fullNames = it },
                    label = { Text(text = "TextFieldTitle") },
                    modifier = Modifier.wrapContentWidth()
                )
                DropDownList(
                    requestToOpen = isOpen.value,
                    list = allMemberInfo,
                    openCloseOfDropDownList,
                    userSelectedString
                )
            }
            Spacer(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Transparent)
                    .padding(10.dp)
                    .clickable(
                        onClick = { isOpen.value = true }
                    )
            )
        }

        AddTransactionPage(selectedMember, transactionsViewModel, navController)
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun AddTransactionPage(selectedMember: MemberDetails,
                       transactionsViewModel: TransactionsViewModel, navController: NavController) {
    var dateOfTransaction by rememberSaveable { mutableStateOf("2020/01/01") }
    var reversedDateOfTransaction by rememberSaveable { mutableStateOf("01/01/2020") }
    var transactionPaidBy by rememberSaveable { mutableStateOf("") }
    var transactionAmountPaid by rememberSaveable { mutableStateOf("0") }
    var transactionConfirmation by rememberSaveable { mutableStateOf("") }
    var buttonEnabled by rememberSaveable { mutableStateOf(true) }
    // Fetching the Local Context
    val mContext = LocalContext.current
    OutlinedButton(onClick = {
        val yearSelected: Int
        val monthSelected: Int
        val daySelected: Int
        // Initializing a Calendar
        val mCalendar = Calendar.getInstance()
        // Fetching current year, month and day
        yearSelected = mCalendar.get(Calendar.YEAR)
        monthSelected = mCalendar.get(Calendar.MONTH)
        daySelected = mCalendar.get(Calendar.DAY_OF_MONTH)
        mCalendar.time = Date()
        DatePickerDialog(
            mContext,
            { _: DatePicker, mYear, mMonth, mDayOfMonth ->
                val month = String.format("%02d", mMonth+1)
                val date = String.format("%02d", mDayOfMonth)
                dateOfTransaction = "$mYear/$month/$date"
                reversedDateOfTransaction = "$date/${month}/$mYear"
            }, yearSelected, monthSelected, daySelected
        ).show()
        Toast.makeText(mContext, dateOfTransaction, Toast.LENGTH_SHORT).show()
    }) {
        Text(text = dateOfTransaction)

    }
    OutlinedTextField(
        label = { Text(text = "Paid by") },
        value = transactionPaidBy,
        onValueChange = {transactionPaidBy = it},
        modifier = Modifier.padding(top = 4.dp),
        isError = (transactionPaidBy == ""))
    OutlinedTextField(value = transactionAmountPaid,
        onValueChange = { transactionAmountPaid = it },
        label = { Text(text = "Amount paid") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.padding(top = 4.dp),
        isError = (transactionAmountPaid == "0")
        )
    OutlinedTextField(
        label = { Text(text = "Confirmation Message") },
        value = transactionConfirmation,
        onValueChange = {transactionConfirmation = it},
        modifier = Modifier.padding(top = 4.dp),
        isError = (transactionConfirmation == ""))
    OutlinedTextField(
        label = { Text(text = "Previous amount") },
        value = selectedMember.totalAmount,
        enabled = false,
        onValueChange = {},
        modifier = Modifier.padding(top = 4.dp))
    Button(
        enabled = buttonEnabled,
        onClick = {
            if (dateOfTransaction != "2020/01/01"
                    && transactionConfirmation  != ""
                    && transactionAmountPaid != "0"
                    && transactionPaidBy != "")
                {
                buttonEnabled = false
                transactionsViewModel.addTransaction(
                    transactionDetails = Transactions(
                        transactionDate = dateOfTransaction,
                        depositFor = selectedMember.fullNames,
                        depositBy = transactionPaidBy,
                        transactionAmount = transactionAmountPaid.toInt(),
                        transactionConfirmation = transactionConfirmation),
                    previousAmount = selectedMember.totalAmount.toInt(),
                    memberPhone = selectedMember.phoneNumber)
                    if(transactionsViewModel.uploadStatus.value) {
                        Toast.makeText(mContext, "Still uploading", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        //check if both transactions and contributions were updated successfully
                        if(transactionsViewModel.transactionUploadStatus && transactionsViewModel.contUploadStatus) {
                            Toast.makeText(mContext, "Both transactions and MemberContributions Uploaded successfully", Toast.LENGTH_SHORT).show()
                            transactionsViewModel.launchTransactionScreen(navController)
                        }
                        //show status to know which failed
                        else {
                            Toast.makeText(mContext, "Transactions status ${transactionsViewModel.transactionUploadStatus} and MemberContributions Uploaded status ${transactionsViewModel.contUploadStatus}", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
                else {
                    Toast.makeText(mContext, "Please confirm the details again", Toast.LENGTH_SHORT).show()
                }

                      },
            modifier = Modifier.padding(top = 4.dp))
        {
            Text(text = "Add Transaction")
        }
    CircularProgressBar(
        isDisplayed = transactionsViewModel.uploadStatus.value
    )
}

@Composable
fun DropDownList(
    requestToOpen: Boolean = false,
    list: List<MemberDetails>,
    request: (Boolean) -> Unit,
    selectedString: (MemberDetails) -> Unit
) {
    DropdownMenu(
        modifier = Modifier.fillMaxWidth(),
        expanded = requestToOpen,
        onDismissRequest = { request(false) },
    ) {
        list.forEach {
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    request(false)
                    selectedString(it)
                }
            ) {
                Text(it.fullNames, modifier = Modifier.wrapContentWidth())
            }
        }
    }
}
