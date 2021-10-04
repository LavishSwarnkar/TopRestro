package com.lavish.toprestro.featureOwner.presentation.ownerHome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Title(text: String,
          modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.h5,
        color = MaterialTheme.colors.onBackground
    )
}

@Composable
fun Status(
    msg: String
) {
    Text(
        text = msg,
        style = MaterialTheme.typography.h6,
        maxLines = 10
    )
}

@Composable
fun OfflineStatus(onRetry: () -> Unit) {
    Row {
        Status("You are offline")

        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun NoItems(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.body1,
        fontStyle = FontStyle.Italic,
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colors.primary.copy(0.15f))
            .padding(start = 16.dp, end = 16.dp, top = 50.dp, bottom = 50.dp),
        textAlign = TextAlign.Center
    )
}