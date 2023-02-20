package com.derich.gitongas.ui.screens.loans

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.derich.gitongas.ui.common.BigFootViewModel
import com.derich.gitongas.ui.common.LogService
import com.derich.gitongas.ui.common.firestorequeries.StorageService
import com.derich.gitongas.ui.data.DataOrException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoansViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService
) : BigFootViewModel(logService) {
    var loans: List<Loan> = mutableStateListOf()
    private val repository: LoansRepository = LoansRepository()
    var loading = mutableStateOf(false)
    val data: MutableState<DataOrException<List<Loan>, Exception>> = mutableStateOf(
        DataOrException(
            listOf(),
            Exception("")
        )
    )
    fun initialize() {
        launchCatching {
                loans = storageService.getLoans()
        }
    }
    private fun getAllLoans() {
        viewModelScope.launch {
            loading.value = true
            data.value = repository.getAllLoansFromFirestone()
            loading.value = false
        }
    }
}