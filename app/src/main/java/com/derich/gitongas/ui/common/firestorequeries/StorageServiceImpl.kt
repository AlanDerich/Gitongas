package com.derich.gitongas.ui.common.firestorequeries

import com.derich.gitongas.ui.screens.home.MemberDetails
import com.derich.gitongas.ui.screens.loans.Loan
import com.derich.gitongas.ui.screens.transactions.Transactions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class StorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore) :
    StorageService {
    private val sdfDate = SimpleDateFormat("dd-M-yyyy", Locale.US)
    private val sdfTime = SimpleDateFormat("hh:mm:ss", Locale.US)
    private val currentDate = sdfDate.format(Date())
    private val currentTime = sdfTime.format(Date())
    override val memberDetails: Flow<List<MemberDetails>>
        get() =
            memberDetailsCollection().snapshots().map { snapshot -> snapshot.toObjects() }

    override val loans: Flow<List<Loan>>
        get() =
            loansCollection().snapshots().map { snapshot -> snapshot.toObjects() }
    override val transactions: Flow<List<Transactions>>
        get() =
            memberDetailsCollection().snapshots().map { snapshot -> snapshot.toObjects() }

    //retrieval functions
    override suspend fun getAllTransactions(): List<Transactions?> {
        return trace(GET_ALL_MEMBER_DETAILS) {memberDetailsCollection().get().await().toObjects(Transactions::class.java)}
    }

    override suspend fun getLoans(): List<Loan> {
        return trace(GET_ALL_LOANS) { loansCollection().get().await().toObjects(Loan::class.java) }
    }

    override suspend fun getMembers(): List<MemberDetails?> {
        return trace(GET_ALL_TRANSACTIONS) { memberDetailsCollection().get().await().toObjects(MemberDetails::class.java) }
    }

    //create new details functions

    override suspend fun saveNewTransaction(transactions: Transactions) {
        trace(SAVE_NEW_TRANSACTION) { addTransactionCollection().set(transactions).await() }
    }

    override suspend fun saveNewLoan(loan: Loan): String {
        TODO("Not yet implemented")
    }

    override suspend fun updateMember(memberDetails: MemberDetails) {
        TODO("Not yet implemented")
    }

    //update functions
    //update members contributions
    override suspend fun updateMemberContribution(memberDetails: MemberDetails,
                                                  memberPhoneNumber: String,
                                                  memberFullNames: String,
                                                  resultingDate: String,
                                                  newUserAmount: String) {
        trace(UPDATE_MEMBER_CONTRIBUTION) {memberDetailCollection(memberPhoneNumber)
            .document(memberFullNames)
            .update(
                "contributionsDate", resultingDate,
                "totalAmount", newUserAmount
            ).await()}
    }
    //update loans
    override suspend fun updateLoan(loan: Loan) {
        TODO("Not yet implemented")
    }
    //update transaction

//    override suspend fun delete(taskId: String) {
//        currentCollection(auth.currentUserId).document(taskId).delete().await()
//    }

//    // TODO: It's not recommended to delete on the client:
//    // https://firebase.google.com/docs/firestore/manage-data/delete-data#kotlin+ktx_2
//    override suspend fun deleteAllForUser(userId: String) {
//        val matchingTasks = currentCollection(userId).get().await()
//        matchingTasks.map { it.reference.delete().asDeferred() }.awaitAll()
//    }

//    private fun currentCollection(uid: String): CollectionReference =
//        firestore.collection(USER_COLLECTION).document(uid).collection(TASK_COLLECTION)
    //member details collection
    private fun memberDetailsCollection(): Query =
        firestore
            .collectionGroup("allMembers")
            .orderBy("totalAmount", Query.Direction.DESCENDING)
    //a collection reference of a member
    private fun memberDetailCollection(memberPhoneNumber: String): CollectionReference =
        firestore.collection("Members")
            .document(memberPhoneNumber)
            .collection("allMembers")

    private fun addTransactionCollection(): DocumentReference =
        firestore.collection("Transactions")
            .document(currentDate)
            .collection("allTransactions")
            .document(currentTime)
    //transactions collection
    private fun transactionsCollection(): Query =
        firestore
            .collectionGroup("allTransactions")
            .orderBy("transactionDate", Query.Direction.DESCENDING)
    //loans collection
    private fun loansCollection(): Query =
        firestore
            .collectionGroup("allLoans")
            .orderBy("dateLoaned", Query.Direction.DESCENDING)


    companion object {
//        private const val USER_COLLECTION = "users"
//        private const val TASK_COLLECTION = "tasks"
//        private const val SAVE_TASK_TRACE = "saveTask"
        private const val SAVE_NEW_TRANSACTION = "saveTransaction"
        private const val UPDATE_MEMBER_CONTRIBUTION = "updateContribution"
        private const val GET_ALL_MEMBER_DETAILS = "getMemberDetails"
        private const val GET_ALL_LOANS = "getAllLoans"
        private const val GET_ALL_TRANSACTIONS = "getAllTransactions"
//        private const val UPDATE_TASK_TRACE = "updateTask"
    }
}