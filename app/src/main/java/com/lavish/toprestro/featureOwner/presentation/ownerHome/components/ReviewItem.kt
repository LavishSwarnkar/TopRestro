package com.lavish.toprestro.featureOwner.presentation.ownerHome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.lavish.toprestro.R
import com.lavish.toprestro.featureOwner.domain.model.Review

@Composable
fun ReviewItem(
    review: Review,
    onReplyClick: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(color = MaterialTheme.colors.primary.copy(alpha = 0.1f))
        .padding(8.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = review.userName,
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    text = "${review.formattedDate()} (${review.restroName})",
                    style = MaterialTheme.typography.overline,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                )
            }

            RatingBar(
                value = review.starRating,
                ratingBarStyle = RatingBarStyle.HighLighted,
                onValueChange = {},
                activeColor = review.colorForRating(),
                hideInactiveStars = true
            ) {}
        }

        Text(
            text = review.review,
            modifier = Modifier.padding(8.dp)
        )

        IconButton(
            onClick = onReplyClick,
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_reply),
                contentDescription = "Reply"
            )
        }
    }
}