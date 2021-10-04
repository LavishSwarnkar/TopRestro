package com.lavish.toprestro.featureOwner.presentation.ownerHome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.lavish.toprestro.featureOwner.domain.model.Restaurant

@Composable
fun RestaurantItem(restaurant: Restaurant) {
    Text(
        text = restaurant.name,
        style = MaterialTheme.typography.body1,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.primary.copy(0.15f))
            .clickable { }
            .padding(16.dp)
    )
}