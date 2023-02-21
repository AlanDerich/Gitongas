package com.derich.gitongas.ui.screens.transactions

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.derich.gitongas.ui.bottomnavigation.BottomNavItem
import com.derich.gitongas.ui.data.DataOrException
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TransactionsViewModel : ViewModel() {
    private val repository: TransactionsRepository = TransactionsRepository()
    var loading = mutableStateOf(false)
    var uploadStatus = mutableStateOf(false)
    var transactionUploadStatus = false
    var contUploadStatus = false
    val data: MutableState<DataOrException<List<Transactions>, Exception>> = mutableStateOf(
        DataOrException(
            listOf(),
            Exception("")
        )
    )
    init {
        getAllTransactions()
    }

    private fun getAllTransactions() {
        viewModelScope.launch {
            loading.value = true
            data.value = repository.getAllTransactionsFromFirestone()
            loading.value = false
        }
    }
    fun launchAddTransactionScreen(navController: NavController) {
        navController.navigate(BottomNavItem.AddTransaction.screen_route)
    }
    fun launchTransactionScreen(navController: NavController) {
        navController.navigate(BottomNavItem.Transactions.screen_route)
    }
    fun addTransaction(transactionDetails: Transactions,
                       previousAmount: Int,
                       memberPhone: String) {
        //check if upload job is complete or not
        val waitingForResponse = viewModelScope.launch {
            transactionUploadStatus = repository.uploadTransactionToDb(transactionDetails)
            contUploadStatus = repository.updateMemberContributions(
                memberPhoneNumber = memberPhone,
                memberFullNames = transactionDetails.depositFor,
                resultingDate = calculateResultingDate(previousAmount + (transactionDetails.transactionAmount)),
                newUserAmount = (previousAmount + (transactionDetails.transactionAmount)).toString())
        }.isActive
        uploadStatus.value = waitingForResponse
    }
    private fun calculateResultingDate(newUserAmount: Int): String {
        val totalDays: Int = (newUserAmount / 30)
        //add number of days to the start date
        var sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val c: Calendar = Calendar.getInstance()
        c.time = sdf.parse("31/12/2022")!!
        c.add(Calendar.DATE, totalDays)
        sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        return sdf.format(Date(c.timeInMillis))
    }
}