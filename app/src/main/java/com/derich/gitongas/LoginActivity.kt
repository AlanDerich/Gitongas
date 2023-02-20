package com.derich.gitongas

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.derich.gitongas.ui.common.composables.BigFutAppBar
import com.derich.gitongas.ui.screens.login.AuthViewModel
import com.derich.gitongas.ui.screens.login.PhoneLoginUI
import com.derich.gitongas.ui.theme.BigFootTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity: ComponentActivity() {
    private lateinit var authVM: AuthViewModel
    companion object {
        var LoginActivity: LoginActivity? = null

//        fun getInstance(): LoginActivity? = LoginActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LoginActivity = this
        super.onCreate(savedInstanceState)
        setContent {
            authVM = viewModel()
            BigFootTheme {
                Scaffold(
                        topBar = {
                            BigFutAppBar()
                        }
                    ) { innerPadding ->
                    PhoneLoginUI(
                            navigateToHome = {
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                        },
                        restartLogin = {
                            authVM.resetAuthState()
                        },
                        modifier = Modifier.padding(innerPadding))

                    }
                }
        }
    }

    override fun onResume() {
        super.onResume()
        LoginActivity = this
    }

    override fun onRestart() {
        super.onRestart()
        LoginActivity = this
    }

    override fun onDestroy() {
        super.onDestroy()
        LoginActivity = null
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}


//class LoginActivity : ComponentActivity() {
//    private lateinit var authVM: AuthViewModel
//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
//        setContent {
//            authVM = viewModel()
//            BigFootTheme {
//                Scaffold(
//                    topBar = {
//                        BigFutAppBar()
//                    }
//                ) {
//                        innerPadding ->
//                    PhoneLoginUI(
//                        modifier = Modifier.padding(innerPadding),
//                        viewModel = authVM,
//                        navigateToHome = {
//                            startActivity(Intent(this, MainActivity::class.java))
//                        },
//                        restartLogin = {
//                            authVM.resetAuthState()
//                        }
//                    )
//
//                }
//            }
//
//        }
//    }
//
//    @Deprecated("Deprecated in Java", ReplaceWith("finish()"))
//    override fun onBackPressed() {
//        finish()
//    }
//}