package com.derich.gitongas.ui.common.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.derich.gitongas.R

@Composable
fun BigFutAppBar(modifier: Modifier = Modifier) {
    Row(modifier = modifier
        .background(color = Color.Blue)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.bigfut1),
                contentDescription = "App Icon",
                modifier = Modifier
                    .padding(8.dp)
                    .size(32.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
        Text(text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.h4,
            color = Color.White)
    }
}