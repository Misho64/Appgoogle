package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.AtsAnalysisResult
import com.example.ui.theme.Amber500
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Red500

@Composable
fun TabAtsScreen(
  atsResult: AtsAnalysisResult,
  isAr: Boolean,
  modifier: Modifier = Modifier,
) {
  val animatedProgress by animateFloatAsState(
    targetValue = atsResult.score / 100f,
    animationSpec = tween(durationMillis = 800),
    label = "ats_gauge"
  )

  val gaugeColor = when {
    atsResult.score >= 85 -> Emerald500
    atsResult.score >= 60 -> Amber500
    else -> Red500
  }

  LazyColumn(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. ATS Score Overview Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder().copy(
          brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.outlineVariant)
        ),
        modifier = Modifier.fillMaxWidth().testTag("ats_score_overview_card")
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Text(
            text = if (isAr) "🎯 فاحص التوافق مع أنظمة التوظيف (ATS)" else "🎯 ATS Compatibility Scanner",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
          )

          // Circular Gauge
          Box(
            modifier = Modifier.size(130.dp),
            contentAlignment = Alignment.Center
          ) {
            CircularProgressIndicator(
              progress = { 1f },
              modifier = Modifier.size(130.dp),
              color = MaterialTheme.colorScheme.surfaceVariant,
              strokeWidth = 10.dp,
            )
            CircularProgressIndicator(
              progress = { animatedProgress },
              modifier = Modifier.size(130.dp),
              color = gaugeColor,
              strokeWidth = 10.dp,
              strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "${atsResult.score}%",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = gaugeColor
              )
              Text(
                text = "ATS SCORE",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          // Grade Badge
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = gaugeColor.copy(alpha = 0.15f)
          ) {
            Text(
              text = atsResult.grade,
              color = gaugeColor,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
          }

          Text(
            text = if (isAr)
              "يقوم هذا الفاحص بتحليل السيرة الذاتية للتأكد من احتوائها على العناصر الأساسية والكلمات المفتاحية المطلوبة لاجتياز أنظمة الفرز الآلي للشركات."
            else
              "This scanner checks your resume against standard ATS parsing rules to ensure high discoverability by recruiters.",
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
          )
        }
      }
    }

    // 2. Detailed Checklist Breakdown
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder().copy(
          brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.outlineVariant)
        ),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Text(
            text = if (isAr) "📋 تقرير الفحص والتوصيات" else "📋 Analysis & Recommendations",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          atsResult.tips.forEach { tip ->
            Card(
              shape = RoundedCornerShape(10.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (tip.isPositive)
                  Emerald500.copy(alpha = 0.08f)
                else
                  Amber500.copy(alpha = 0.08f)
              ),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = if (tip.isPositive) Icons.Default.CheckCircle else Icons.Default.Warning,
                  contentDescription = null,
                  tint = if (tip.isPositive) Emerald500 else Amber500,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = tip.text,
                  fontSize = 12.5.sp,
                  fontWeight = FontWeight.Medium,
                  color = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.weight(1f)
                )
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
