package com.derich.gitongas.ui.common

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}