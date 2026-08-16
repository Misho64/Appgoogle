package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
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
import com.example.data.model.CvData
import com.example.ui.theme.Emerald500

data class TemplateItem(
  val id: String,
  val nameAr: String,
  val nameEn: String,
  val descAr: String,
  val descEn: String,
  val icon: String,
)

val templateList = listOf(
  TemplateItem("modern", "عصري (Modern Split)", "Modern Split", "تخطيط مقسم بعمودين واحترافي", "2-column modern professional layout", "🎨"),
  TemplateItem("minimal", "بسيط (Minimalist)", "Minimalist", "تصميم ناعم ومرتب خفيف على العين", "Clean, centered minimalist hierarchy", "✨"),
  TemplateItem("corporate", "مؤسسي (Corporate)", "Corporate", "إطار رسمي للشركات الكبرى والمؤسسات", "Formal dual-accent corporate border", "🏢"),
  TemplateItem("creative", "إبداعي (Creative Header)", "Creative Header", "شريط علوي ملون وجذاب للقطاعات الإبداعية", "Bold colorful header for creatives", "💡"),
  TemplateItem("executive", "تنفيذي (Executive)", "Executive", "شارة احترافية للإداريين والمدراء", "High-end badge and timeline for leaders", "👑"),
  TemplateItem("ats", "معياري ATS (ATS Friendly)", "ATS Standard", "نص قياسي فائق التوافق مع أنظمة الفرز", "High-scoring single-column scanner text", "🎯"),
  TemplateItem("dark", "داكن فاخر (Dark Slate)", "Dark Luxury", "خلفية داكنة فخمة وعصرية", "High contrast dark slate theme", "🌙"),
  TemplateItem("elegant", "كلاسيكي (Classic Serif)", "Classic Serif", "خطوط راقية وتنسيق أكاديمي رزين", "Refined classic serif look", "📜")
)

val colorSwatches = listOf(
  "#111827" to "كحلي داكن / Black Navy",
  "#059669" to "زمردي / Emerald Green",
  "#2563EB" to "أزرق ملكي / Royal Blue",
  "#4F46E5" to "نيلي / Indigo",
  "#991B1B" to "عنابي / Burgundy",
  "#0D9488" to "فيروزي / Dark Teal",
  "#D97706" to "ذهبي عنبري / Amber Gold",
  "#374151" to "رمادي فحمي / Charcoal"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TabDesignScreen(
  cvData: CvData,
  onSelectTemplate: (String) -> Unit,
  onSelectPrimaryColor: (String) -> Unit,
  onSelectPhotoShape: (String) -> Unit,
  onSelectFontSizeScale: (Float) -> Unit,
  modifier: Modifier = Modifier,
) {
  val isAr = cvData.lang == "ar"

  LazyColumn(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Templates Selection Card
    item {
      SectionCard(title = if (isAr) "🎨 اختيار القالب (8 قوالب احترافية)" else "🎨 Choose Template (8 Styles)") {
        Column(
          modifier = Modifier.fillMaxWidth().padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          templateList.forEach { tmpl ->
            val isSelected = cvData.template == tmpl.id
            Card(
              shape = RoundedCornerShape(10.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
              ),
              border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary), width = 2.dp) else null,
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectTemplate(tmpl.id) }
                .testTag("template_${tmpl.id}")
            ) {
              Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.weight(1f)
                ) {
                  Text(text = tmpl.icon, fontSize = 24.sp, modifier = Modifier.padding(end = 12.dp))
                  Column {
                    Text(
                      text = if (isAr) tmpl.nameAr else tmpl.nameEn,
                      fontWeight = FontWeight.Bold,
                      fontSize = 14.sp,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = if (isAr) tmpl.descAr else tmpl.descEn,
                      fontSize = 11.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }

                if (isSelected) {
                  Box(
                    modifier = Modifier
                      .size(24.dp)
                      .clip(CircleShape)
                      .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = "Selected",
                      tint = MaterialTheme.colorScheme.onPrimary,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    // 2. Primary Color Palette Card
    item {
      SectionCard(title = if (isAr) "🌈 اللون الرئيسي للهوية" else "🌈 Brand Accent Color") {
        Column(
          modifier = Modifier.fillMaxWidth().padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            colorSwatches.forEach { (hex, name) ->
              val color = try {
                Color(android.graphics.Color.parseColor(hex))
              } catch (e: Exception) {
                Color.Black
              }
              val isSelected = cvData.primaryColorHex.equals(hex, ignoreCase = true)

              Box(
                modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(color)
                  .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.5f),
                    shape = CircleShape
                  )
                  .clickable { onSelectPrimaryColor(hex) }
                  .testTag("color_swatch_$hex"),
                contentAlignment = Alignment.Center
              ) {
                if (isSelected) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = name,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                  )
                }
              }
            }
          }
        }
      }
    }

    // 3. Photo Shape Card
    item {
      SectionCard(title = if (isAr) "🖼️ شكل الصورة الشخصية" else "🖼️ Photo Frame Shape") {
        Row(
          modifier = Modifier.fillMaxWidth().padding(12.dp),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          val shapes = listOf(
            "circle" to (if (isAr) "دائري ⚪" else "Circle ⚪"),
            "rounded" to (if (isAr) "حواف دائرية 🟩" else "Rounded 🟩"),
            "square" to (if (isAr) "مربع ⬛" else "Square ⬛")
          )

          shapes.forEach { (shapeKey, label) ->
            val isSelected = cvData.photoShape == shapeKey
            Card(
              shape = RoundedCornerShape(10.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
              ),
              modifier = Modifier
                .weight(1f)
                .clickable { onSelectPhotoShape(shapeKey) }
                .testTag("photo_shape_$shapeKey")
            ) {
              Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = label,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }
    }

    // 4. Font Sizing Scale Card
    item {
      SectionCard(title = if (isAr) "🔤 حجم وتكبير الخطوط" else "🔤 Font Sizing Scale") {
        Column(
          modifier = Modifier.fillMaxWidth().padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = if (isAr) "مقياس الخط:" else "Font Scale:",
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "${(cvData.fontSizeScale * 100).toInt()}%",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
          }

          Slider(
            value = cvData.fontSizeScale,
            onValueChange = onSelectFontSizeScale,
            valueRange = 0.85f..1.25f,
            steps = 7,
            colors = SliderDefaults.colors(
              thumbColor = MaterialTheme.colorScheme.primary,
              activeTrackColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth().testTag("slider_font_size")
          )
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun SectionCard(
  title: String,
  content: @Composable () -> Unit,
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.outlineVariant)
    ),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.1).sp,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
      )
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(1.dp)
          .background(MaterialTheme.colorScheme.outlineVariant)
      )
      content()
    }
  }
}
