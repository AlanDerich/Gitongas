package com.derich.gitongas.ui.screens.transactions

data class Transactions (
    var transactionDate:String = "",
    var depositFor:String = "",
    var depositBy:String = "",
    var transactionAmount:Int = 0,
    var transactionConfirmation:String = ""
)