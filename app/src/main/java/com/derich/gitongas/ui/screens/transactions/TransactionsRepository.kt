package com.derich.gitongas.ui.screens.transactions

import com.derich.gitongas.ui.common.firestorequeries.FirestoreQueries
import com.derich.gitongas.ui.data.DataOrException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class TransactionsRepository {
    private val firestoreQueries: FirestoreQueries = FirestoreQueries()
    private val getAllTransactions : Query = firestoreQueries.queryAllTransactions()
  suspend fun getAllTransactionsFromFirestone(): DataOrException<List<Transactions>, Exception> {
      val dataOrException = DataOrException<List<Transactions>, Exception>()
      try {
          dataOrException.data = getAllTransactions.get().await().map { document ->
              document.toObject(Transactions::class.java)
          }
      }catch (e: FirebaseFirestoreException) {
          dataOrException.e = e
      }
      return dataOrException
  }
    fun uploadTransactionToDb(transactionDetails: Transactions) : Boolean{
        return try{
            firestoreQueries.uploadToTransactions(transactionDetails = transactionDetails).isSuccessful
            true
        }catch (e : Exception){
            false
        }
    }
    fun updateMemberContributions(memberPhoneNumber: String,
                                          memberFullNames: String,
                                          resultingDate: String, newUserAmount: String) : Boolean{
        return try{
            firestoreQueries.updateContributionsDetails(memberPhoneNumber, memberFullNames, resultingDate, newUserAmount)
                .isSuccessful
            true
        }catch (e : Exception){
            false
        }
    }
}