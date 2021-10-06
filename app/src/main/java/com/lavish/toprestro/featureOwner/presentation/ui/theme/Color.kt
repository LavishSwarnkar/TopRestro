package com.lavish.compose.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * (ColorStateList.valueOf(
when {
review.starRating!! > 4 -> Color.parseColor("#10C300")
review.starRating!! > 3 -> Color.parseColor("#82C300")
review.starRating!! > 2 -> Color.parseColor("#C3A300")
review.starRating!! >= 1 -> Color.parseColor("#C36800")
else -> Color.parseColor("#C36800")
}
))
 */

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val Stars5 = Color(0xFF10C300)
val Stars4 = Color(0xFF82C300)
val Stars3 = Color(0xFFC3A300)
val Stars2 = Color(0xFFC36800)

val PurpleFaded = Color(0x0e6200EE)
val PurpleFadedBorder = Color(0x446200EE)