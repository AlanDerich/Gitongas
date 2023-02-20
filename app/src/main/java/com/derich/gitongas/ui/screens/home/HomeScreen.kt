package com.derich.gitongas.ui.screens.home

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.derich.gitongas.R
import com.derich.gitongas.ui.common.composables.CircularProgressBar
import kotlinx.coroutines.ExperimentalCoroutinesApi


//this is the default home screen
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun HomeComposable(modifier: Modifier = Modifier,
                   viewModel: ContributionsViewModel,
                   specificMemberDetails: MemberDetails?,
                   allMembersInfo: List<MemberDetails>
) {
    val context = LocalContext.current
    if(specificMemberDetails != null){
//        val memberCont = contributions!!.contains("", )

            Column(modifier = modifier.padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val differenceInContributions = viewModel.calculateContributionsDifference(
                        specificMemberDetails.totalAmount.toInt()
                    )
                    if( differenceInContributions < 0){
                        Icon(painter = painterResource(id = R.drawable.baseline_check_circle_24),
                            contentDescription = "Status of Contribution",
                            modifier = Modifier.size(68.dp))
                        Spacer(modifier = Modifier.padding(2.dp))
                        Text(text = "Hello ${specificMemberDetails.firstName}, you\'re on ${specificMemberDetails.contributionsDate}. Congrats! You are KSH ${-differenceInContributions} ahead on schedule")
                    }
                    else{
                        Icon(painter = painterResource(id = R.drawable.baseline_cancel_24),
                            contentDescription = "Status of Contribution",
                            modifier = Modifier.size(68.dp))
                        Text(text = "Hello ${specificMemberDetails.firstName}, you\'re on ${specificMemberDetails.contributionsDate}. You need KSH $differenceInContributions to be back on track.")
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                }
                LazyColumn(modifier = Modifier.padding(top= 8.dp)) {
                    items(
                        items = allMembersInfo
                    ) { contribution ->
                        ContributionCard(contribution = contribution,
                            modifier = modifier)
                    }
                }
            }



        val e = viewModel.memberData.value.e
        e?.let {
            Text(
                text = e.message!!,
                modifier = Modifier.padding(16.dp)
            )
            Log.e("homepage", "Error $e")
        }

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressBar(
                isDisplayed = viewModel.loadingContributions.value || viewModel.loadingMemberDetails.value
            )

        }
        BackHandler {
            val activity = (context as? Activity)
            activity?.finish()
        }
    }
    else {
        Text(text = "Oops. No details we're not found in our database. Please Contact Admin to make things right.")
    }


}

@Composable
fun ContributionCard(contribution: MemberDetails,
                     modifier: Modifier
) {
    Row(
        modifier = modifier.padding(start = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(contribution.profPicUrl),
            contentDescription = stringResource(R.string.profile_image_description),
            Modifier
                .clip(MaterialTheme.shapes.medium)
                .size(68.dp)
        )
        UsersColumn(contribution = contribution)
    }
}

@Composable 
fun UsersColumn(modifier: Modifier = Modifier, contribution: MemberDetails) {
        Column(horizontalAlignment = Alignment.Start, modifier = modifier.padding(8.dp)) {
            Text(text = contribution.fullNames, fontWeight = Bold)
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "KSH ${contribution.totalAmount}")
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = contribution.contributionsDate)
        } 
}
