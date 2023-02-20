@file:OptIn(ExperimentalCoroutinesApi::class)

package com.derich.gitongas.ui.bottomnavigation

import android.util.Log
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.derich.gitongas.R
import com.derich.gitongas.ui.common.composables.CircularProgressBar
import com.derich.gitongas.ui.screens.account.AccountsComposable
import com.derich.gitongas.ui.screens.addtransaction.AddTransactionScreen
import com.derich.gitongas.ui.screens.home.ContributionsViewModel
import com.derich.gitongas.ui.screens.home.HomeComposable
import com.derich.gitongas.ui.screens.home.MemberDetails
import com.derich.gitongas.ui.screens.loans.LoansComposable
import com.derich.gitongas.ui.screens.loans.LoansViewModel
import com.derich.gitongas.ui.screens.login.AuthViewModel
import com.derich.gitongas.ui.screens.transactions.TransactionsComposable
import com.derich.gitongas.ui.screens.transactions.TransactionsViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun BottomNavigator(
    navController: NavController,
    contViewModel: ContributionsViewModel) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Transactions,
        BottomNavItem.Loans,
        BottomNavItem.Account
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.teal_200),
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title,
                    fontSize = 9.sp) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                enabled = contViewModel.memberData.value.data?.isNotEmpty() ?: false,
                onClick = {
                    navController.navigate(item.screen_route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
@Composable
fun NavigationGraph(
    navController: NavHostController,
    contViewModel: ContributionsViewModel,
    modifier: Modifier,
    transactionsViewModel: TransactionsViewModel,
    authVm: AuthViewModel,
    loansVM: LoansViewModel,
    allMemberInfo: List<MemberDetails>?
) {
//    var allMemberInfo by remember { mutableStateOf(List<MemberDetails>) }
    val memberDetails: MemberDetails
    if (allMemberInfo != null){
    if (allMemberInfo.isNotEmpty()){
        memberDetails = getMemberData(allMemberInfo)!!
        NavHost(navController, startDestination = BottomNavItem.Home.screen_route, modifier = modifier) {
            composable(BottomNavItem.Home.screen_route) {
                HomeComposable(viewModel = contViewModel, specificMemberDetails = memberDetails, allMembersInfo = allMemberInfo)
            }

            composable(BottomNavItem.Transactions.screen_route) {
                TransactionsComposable(transactionsViewModel = transactionsViewModel,
                        memberInfo = memberDetails, navController = navController)
            }
            composable(BottomNavItem.Loans.screen_route) {
                    LoansComposable(loansViewModel = loansVM,
                        memberInfo = memberDetails)
            }
            composable(BottomNavItem.Account.screen_route) {
                AccountsComposable(authViewModel = authVm,
                    memberInfo = memberDetails)
            }
            composable(BottomNavItem.AddTransaction.screen_route) {
                AddTransactionScreen(transactionsViewModel = transactionsViewModel,
                    allMemberInfo = allMemberInfo, navController = navController)
            }
        }
    }
    else{
        CircularProgressBar(
            isDisplayed = contViewModel.loadingMemberDetails.value
        )
    }
    }
    else{
        ErrorScreen(contViewModel.memberData.value.e.toString())
    }

}

@Composable
fun ErrorScreen(e: String) {
    Text(text = e)
    Log.e("Home", e)
}

fun getMemberData(memberInfo: List<MemberDetails>): MemberDetails? {
    var memberDets: MemberDetails?
    memberInfo.forEach {memberDetails ->
        if (memberDetails.phoneNumber == FirebaseAuth.getInstance().currentUser!!.phoneNumber){
            memberDets = memberDetails
            return memberDets
        }
    }
    return null
}