package com.derich.gitongas.ui.common.firestorequeries

import com.derich.gitongas.ui.screens.transactions.Transactions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Singleton
class FirestoreQueries {
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    fun queryAllTransactions(): Query = firebaseFirestore
        .collectionGroup("allTransactions")
        .orderBy("transactionDate", Query.Direction.DESCENDING)

    //query all loans from DB
    fun queryAllLoans() = firebaseFirestore
        .collectionGroup("allLoans")
        .orderBy("dateLoaned", Query.Direction.DESCENDING)
    fun queryAllMemberDetails() = firebaseFirestore
        .collectionGroup("allMembers")
        .orderBy("totalAmount", Query.Direction.DESCENDING)
    private val sdfDate = SimpleDateFormat("dd-M-yyyy", Locale.US)
    private val sdfTime = SimpleDateFormat("hh:mm:ss", Locale.US)
    private val currentDate = sdfDate.format(Date())
    private val currentTime = sdfTime.format(Date())
    fun uploadToTransactions(transactionDetails: Transactions) =
        firebaseFirestore.collection("Transactions")
        .document(currentDate)
        .collection("allTransactions")
        .document(currentTime)
        .set(transactionDetails)
    fun updateContributionsDetails(
        memberPhoneNumber: String,
        memberFullNames: String,
        resultingDate: String, newUserAmount: String) =
        firebaseFirestore.collection("Members")
        .document(memberPhoneNumber)
        .collection("allMembers")
        .document(memberFullNames)
        .update(
            "contributionsDate", resultingDate,
            "totalAmount", newUserAmount
        )
}