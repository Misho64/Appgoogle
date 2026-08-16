package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DhikrState
import com.example.ui.theme.Emerald500
import com.example.ui.theme.SleekBannerAccent
import com.example.ui.theme.SleekBannerBg
import com.example.ui.theme.SleekBannerText
import com.example.ui.theme.SleekPurple
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun DhikrBanner(
  state: DhikrState,
  isAr: Boolean,
  onDone: () -> Unit,
  onCancel: () -> Unit,
  onClose: () -> Unit,
  onRefresh: () -> Unit,
  modifier: Modifier = Modifier,
) {
  AnimatedVisibility(
    visible = state.isVisible,
    enter = fadeIn() + expandVertically(),
    exit = fadeOut() + shrinkVertically(),
    modifier = modifier
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 6.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(SleekBannerBg)
        .testTag("dhikr_banner")
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          // Left side: Icon & Dhikr Text
          Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "✨",
              fontSize = 18.sp,
              modifier = Modifier.padding(end = 8.dp)
            )
            Text(
              text = state.text,
              color = SleekBannerText,
              fontSize = 13.5.sp,
              fontWeight = FontWeight.Medium,
              lineHeight = 19.sp
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          // Right side: Action buttons
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            // Done button
            Button(
              onClick = onDone,
              colors = ButtonDefaults.buttonColors(
                containerColor = SleekPurple,
                contentColor = Color.White
              ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .height(30.dp)
                .testTag("dhikr_done_button")
            ) {
              Text(
                text = if (isAr) "تم" else "Done",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold
              )
            }

            // Cancel button
            OutlinedButton(
              onClick = onCancel,
              colors = ButtonDefaults.outlinedButtonColors(
                contentColor = SleekBannerAccent
              ),
              border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(SleekBannerAccent.copy(alpha = 0.4f))
              ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .height(30.dp)
                .testTag("dhikr_cancel_button")
            ) {
              Text(
                text = if (isAr) "إلغاء" else "Cancel",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium
              )
            }

            // Next / Refresh button
            IconButton(
              onClick = onRefresh,
              modifier = Modifier
                .size(30.dp)
                .testTag("dhikr_refresh_button")
            ) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = if (isAr) "ذكر آخر" else "Next Dhikr",
                tint = SleekBannerAccent.copy(alpha = 0.85f),
                modifier = Modifier.size(16.dp)
              )
            }

            // Close icon
            IconButton(
              onClick = onClose,
              modifier = Modifier
                .size(30.dp)
                .testTag("dhikr_close_button")
            ) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = if (isAr) "إغلاق" else "Close",
                tint = SleekBannerAccent.copy(alpha = 0.7f),
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }

        // Sleek purple countdown progress bar (7 seconds)
        LinearProgressIndicator(
          progress = { state.progress },
          modifier = Modifier
            .fillMaxWidth()
            .height(3.dp),
          color = SleekBannerAccent,
          trackColor = Color(0xFF2C2830),
        )
      }
    }
  }
}
