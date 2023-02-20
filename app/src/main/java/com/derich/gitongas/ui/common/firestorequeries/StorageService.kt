package com.derich.gitongas.ui.common.firestorequeries

import com.derich.gitongas.ui.screens.home.MemberDetails
import com.derich.gitongas.ui.screens.loans.Loan
import com.derich.gitongas.ui.screens.transactions.Transactions
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val memberDetails: Flow<List<MemberDetails>>
    val transactions: Flow<List<Transactions>>
    val loans: Flow<List<Loan>>
    //getting existing functions
    suspend fun getAllTransactions(): List<Transactions?>
    suspend fun getMembers(): List<MemberDetails?>
    suspend fun getLoans(): List<Loan>
    //add new details functions
    suspend fun saveNewTransaction(transactions: Transactions)
    suspend fun saveNewLoan(loan: Loan): String
    //update functions
    suspend fun updateMember(memberDetails: MemberDetails)
    suspend fun updateLoan(loan: Loan)
    suspend fun updateMemberContribution(memberDetails: MemberDetails,
                                         memberPhoneNumber: String,
                                         memberFullNames: String,
                                         resultingDate: String,
                                         newUserAmount: String)

//    suspend fun deleteMember(memberDetails: MemberDetails)
}