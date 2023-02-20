package com.derich.gitongas.ui.screens.login

sealed class Response {
    object NotInitialized : Response()
    class Loading(val message: String?) : Response()
    object Success : Response()
    class Error(val exception: Throwable?) : Response()
}