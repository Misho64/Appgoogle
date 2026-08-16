package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.CvData
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun LiveCvPreview(
  cvData: CvData,
  modifier: Modifier = Modifier,
) {
  val isAr = cvData.lang == "ar"
  val primaryColor = remember(cvData.primaryColorHex) {
    try {
      Color(android.graphics.Color.parseColor(cvData.primaryColorHex))
    } catch (e: Exception) {
      Color(0xFF111827)
    }
  }

  val photoShape: Shape = when (cvData.photoShape) {
    "rounded" -> RoundedCornerShape(12.dp)
    "square" -> RectangleShape
    else -> CircleShape
  }

  val isDarkTemplate = cvData.template == "dark"
  val paperBg = if (isDarkTemplate) Color(0xFF0F172A) else Color.White
  val textColor = if (isDarkTemplate) Color(0xFFF8FAFC) else Color(0xFF0F172A)
  val textMuted = if (isDarkTemplate) Color(0xFF94A3B8) else Color(0xFF475569)
  val scale = cvData.fontSizeScale

  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = paperBg),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = androidx.compose.ui.graphics.SolidColor(
        if (isDarkTemplate) Slate800 else Color(0xFFE2E8F0)
      )
    ),
    modifier = modifier
      .fillMaxWidth()
      .testTag("live_cv_preview_card")
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(if (cvData.template == "creative") 0.dp else 16.dp)
    ) {
      when (cvData.template) {
        "modern" -> ModernSplitTemplate(cvData, primaryColor, photoShape, isAr, scale, textColor, textMuted)
        "minimal" -> MinimalTemplate(cvData, primaryColor, photoShape, isAr, scale, textColor, textMuted)
        "corporate" -> CorporateTemplate(cvData, primaryColor, photoShape, isAr, scale, textColor, textMuted)
        "creative" -> CreativeTemplate(cvData, primaryColor, photoShape, isAr, scale, textColor, textMuted)
        "executive" -> ExecutiveTemplate(cvData, primaryColor, photoShape, isAr, scale, textColor, textMuted)
        "ats" -> AtsTemplate(cvData, primaryColor, photoShape, isAr, scale, textColor, textMuted)
        "dark" -> ModernSplitTemplate(cvData, primaryColor, photoShape, isAr, scale, textColor, textMuted)
        "elegant" -> ElegantTemplate(cvData, primaryColor, photoShape, isAr, scale, textColor, textMuted)
        else -> ModernSplitTemplate(cvData, primaryColor, photoShape, isAr, scale, textColor, textMuted)
      }
    }
  }
}

// 1. MODERN SPLIT TEMPLATE
@Composable
private fun ModernSplitTemplate(
  cvData: CvData,
  primaryColor: Color,
  photoShape: Shape,
  isAr: Boolean,
  scale: Float,
  textColor: Color,
  textMuted: Color,
) {
  Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      PhotoComponent(cvData.personal.photoUri, photoShape, 72.dp, primaryColor)

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = cvData.personal.fullName.ifBlank { if (isAr) "الاسم الكامل" else "Full Name" },
          fontSize = 20.sp * scale,
          fontWeight = FontWeight.Black,
          color = primaryColor
        )
        Text(
          text = cvData.personal.title.ifBlank { if (isAr) "المسمى الوظيفي" else "Job Title" },
          fontSize = 13.sp * scale,
          fontWeight = FontWeight.Bold,
          color = textMuted
        )
      }
    }

    // Contact Information Bar
    ContactBar(cvData, textMuted, scale)

    DividerBar(primaryColor)

    // Summary
    if (cvData.personal.summary.isNotBlank()) {
      SectionTitle(if (isAr) "النبذة الشخصية" else "Professional Summary", primaryColor, scale)
      Text(
        text = cvData.personal.summary,
        fontSize = 11.5.sp * scale,
        lineHeight = 17.sp * scale,
        color = textColor
      )
    }

    // Work Experience
    if (cvData.experiences.isNotEmpty()) {
      SectionTitle(if (isAr) "الخبرات المهنية" else "Work Experience", primaryColor, scale)
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        cvData.experiences.forEach { exp ->
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = exp.position.ifBlank { if (isAr) "المسمى الوظيفي" else "Position" },
                fontSize = 12.5.sp * scale,
                fontWeight = FontWeight.Bold,
                color = primaryColor
              )
              Text(
                text = exp.dates,
                fontSize = 10.5.sp * scale,
                fontWeight = FontWeight.SemiBold,
                color = textMuted
              )
            }
            Text(
              text = exp.company.ifBlank { if (isAr) "الشركة" else "Company" },
              fontSize = 11.sp * scale,
              fontWeight = FontWeight.Medium,
              color = textMuted
            )
            if (exp.desc.isNotBlank()) {
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = exp.desc,
                fontSize = 11.sp * scale,
                lineHeight = 16.sp * scale,
                color = textColor
              )
            }
          }
        }
      }
    }

    // Education
    if (cvData.education.isNotEmpty()) {
      SectionTitle(if (isAr) "المؤهلات التعليمية" else "Education", primaryColor, scale)
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        cvData.education.forEach { edu ->
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = edu.degree.ifBlank { if (isAr) "الدرجة العلمية" else "Degree" },
                fontSize = 12.sp * scale,
                fontWeight = FontWeight.Bold,
                color = primaryColor
              )
              Text(
                text = edu.dates,
                fontSize = 10.5.sp * scale,
                color = textMuted
              )
            }
            Text(
              text = edu.school,
              fontSize = 11.sp * scale,
              color = textMuted
            )
            if (edu.desc.isNotBlank()) {
              Text(
                text = edu.desc,
                fontSize = 10.5.sp * scale,
                color = textColor
              )
            }
          }
        }
      }
    }

    // Skills with progress bars
    if (cvData.skills.isNotEmpty()) {
      SectionTitle(if (isAr) "المهارات والخبرات" else "Skills & Competencies", primaryColor, scale)
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        cvData.skills.forEach { skill ->
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = skill.name,
                fontSize = 11.5.sp * scale,
                fontWeight = FontWeight.SemiBold,
                color = textColor
              )
              Text(
                text = "${skill.level}%",
                fontSize = 10.sp * scale,
                fontWeight = FontWeight.Bold,
                color = primaryColor
              )
            }
            Spacer(modifier = Modifier.height(2.dp))
            LinearProgressIndicator(
              progress = { skill.level / 100f },
              color = primaryColor,
              trackColor = primaryColor.copy(alpha = 0.15f),
              modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp))
            )
          }
        }
      }
    }

    // Projects
    if (cvData.projects.isNotEmpty()) {
      SectionTitle(if (isAr) "المشاريع والشهادات" else "Projects & Certifications", primaryColor, scale)
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        cvData.projects.forEach { proj ->
          Column {
            Text(
              text = "• ${proj.name}",
              fontSize = 11.5.sp * scale,
              fontWeight = FontWeight.Bold,
              color = textColor
            )
            if (proj.desc.isNotBlank()) {
              Text(
                text = "  ${proj.desc}",
                fontSize = 10.5.sp * scale,
                color = textMuted,
                lineHeight = 15.sp * scale
              )
            }
          }
        }
      }
    }
  }
}

// 2. MINIMALIST CENTER TEMPLATE
@Composable
private fun MinimalTemplate(
  cvData: CvData,
  primaryColor: Color,
  photoShape: Shape,
  isAr: Boolean,
  scale: Float,
  textColor: Color,
  textMuted: Color,
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    PhotoComponent(cvData.personal.photoUri, photoShape, 68.dp, primaryColor)

    Text(
      text = cvData.personal.fullName.ifBlank { if (isAr) "الاسم الكامل" else "Full Name" },
      fontSize = 22.sp * scale,
      fontWeight = FontWeight.Light,
      letterSpacing = 1.sp,
      color = primaryColor,
      textAlign = TextAlign.Center
    )
    Text(
      text = cvData.personal.title.ifBlank { if (isAr) "المسمى الوظيفي" else "Job Title" },
      fontSize = 12.5.sp * scale,
      color = textMuted,
      textAlign = TextAlign.Center
    )

    ContactBar(cvData, textMuted, scale)

    DividerBar(primaryColor.copy(alpha = 0.3f))

    if (cvData.personal.summary.isNotBlank()) {
      Text(
        text = cvData.personal.summary,
        fontSize = 11.sp * scale,
        lineHeight = 17.sp * scale,
        textAlign = TextAlign.Center,
        color = textColor,
        modifier = Modifier.padding(horizontal = 8.dp)
      )
      DividerBar(primaryColor.copy(alpha = 0.2f))
    }

    // Experiences
    if (cvData.experiences.isNotEmpty()) {
      Text(
        text = (if (isAr) "الخبرات العملية" else "EXPERIENCE").uppercase(),
        fontSize = 12.sp * scale,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        color = primaryColor
      )
      cvData.experiences.forEach { exp ->
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
          Text(text = "${exp.position} — ${exp.company}", fontSize = 12.sp * scale, fontWeight = FontWeight.Bold, color = textColor)
          Text(text = exp.dates, fontSize = 10.sp * scale, color = textMuted)
          if (exp.desc.isNotBlank()) {
            Text(text = exp.desc, fontSize = 11.sp * scale, color = textColor, lineHeight = 16.sp * scale)
          }
          Spacer(modifier = Modifier.height(6.dp))
        }
      }
    }

    // Skills Chips
    if (cvData.skills.isNotEmpty()) {
      DividerBar(primaryColor.copy(alpha = 0.2f))
      Text(
        text = (if (isAr) "المهارات" else "SKILLS").uppercase(),
        fontSize = 12.sp * scale,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        color = primaryColor
      )
      SkillsFlowChips(cvData.skills, primaryColor, textColor, scale)
    }
  }
}

// 3. CORPORATE TEMPLATE
@Composable
private fun CorporateTemplate(
  cvData: CvData,
  primaryColor: Color,
  photoShape: Shape,
  isAr: Boolean,
  scale: Float,
  textColor: Color,
  textMuted: Color,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .border(2.dp, primaryColor, RoundedCornerShape(8.dp))
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Corporate Header with dual lines
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = cvData.personal.fullName.ifBlank { if (isAr) "الاسم الكامل" else "Full Name" }.uppercase(),
          fontSize = 19.sp * scale,
          fontWeight = FontWeight.Black,
          color = primaryColor
        )
        Text(
          text = cvData.personal.title.ifBlank { if (isAr) "المسمى الوظيفي" else "Job Title" },
          fontSize = 12.5.sp * scale,
          fontWeight = FontWeight.Bold,
          color = textMuted
        )
      }
      PhotoComponent(cvData.personal.photoUri, photoShape, 64.dp, primaryColor)
    }

    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(primaryColor))
    ContactBar(cvData, textMuted, scale)

    if (cvData.personal.summary.isNotBlank()) {
      SectionTitle(if (isAr) "الملخص التنفيذي" else "Executive Summary", primaryColor, scale)
      Text(text = cvData.personal.summary, fontSize = 11.5.sp * scale, lineHeight = 17.sp * scale, color = textColor)
    }

    if (cvData.experiences.isNotEmpty()) {
      SectionTitle(if (isAr) "السجل الوظيفي" else "Professional Experience", primaryColor, scale)
      cvData.experiences.forEach { exp ->
        Column {
          Text(text = "${exp.position} | ${exp.company}", fontSize = 12.sp * scale, fontWeight = FontWeight.Bold, color = primaryColor)
          Text(text = exp.dates, fontSize = 10.5.sp * scale, color = textMuted)
          if (exp.desc.isNotBlank()) Text(text = exp.desc, fontSize = 11.sp * scale, lineHeight = 16.sp * scale, color = textColor)
          Spacer(modifier = Modifier.height(4.dp))
        }
      }
    }

    if (cvData.skills.isNotEmpty()) {
      SectionTitle(if (isAr) "الكفاءات والمهارات" else "Key Competencies", primaryColor, scale)
      SkillsFlowChips(cvData.skills, primaryColor, textColor, scale)
    }
  }
}

// 4. CREATIVE HEADER TEMPLATE
@Composable
private fun CreativeTemplate(
  cvData: CvData,
  primaryColor: Color,
  photoShape: Shape,
  isAr: Boolean,
  scale: Float,
  textColor: Color,
  textMuted: Color,
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    // Top Color Banner
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(primaryColor)
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        PhotoComponent(cvData.personal.photoUri, photoShape, 68.dp, Color.White)
        Column {
          Text(
            text = cvData.personal.fullName.ifBlank { if (isAr) "الاسم الكامل" else "Full Name" },
            fontSize = 20.sp * scale,
            fontWeight = FontWeight.Black,
            color = Color.White
          )
          Text(
            text = cvData.personal.title.ifBlank { if (isAr) "المسمى الوظيفي" else "Job Title" },
            fontSize = 13.sp * scale,
            color = Color.White.copy(alpha = 0.9f)
          )
        }
      }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      ContactBar(cvData, textMuted, scale)

      if (cvData.personal.summary.isNotBlank()) {
        SectionTitle(if (isAr) "نبذة عني" else "About Me", primaryColor, scale)
        Text(text = cvData.personal.summary, fontSize = 11.5.sp * scale, lineHeight = 17.sp * scale, color = textColor)
      }

      if (cvData.experiences.isNotEmpty()) {
        SectionTitle(if (isAr) "الخبرات الإبداعية والعملية" else "Experience", primaryColor, scale)
        cvData.experiences.forEach { exp ->
          Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = primaryColor.copy(alpha = 0.05f)),
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text(text = exp.position, fontSize = 12.sp * scale, fontWeight = FontWeight.Bold, color = primaryColor)
              Text(text = "${exp.company} • ${exp.dates}", fontSize = 10.5.sp * scale, color = textMuted)
              if (exp.desc.isNotBlank()) Text(text = exp.desc, fontSize = 11.sp * scale, color = textColor)
            }
          }
        }
      }

      if (cvData.skills.isNotEmpty()) {
        SectionTitle(if (isAr) "المهارات والتقنيات" else "Skills & Tools", primaryColor, scale)
        SkillsFlowChips(cvData.skills, primaryColor, textColor, scale)
      }
    }
  }
}

// 5. EXECUTIVE TEMPLATE
@Composable
private fun ExecutiveTemplate(
  cvData: CvData,
  primaryColor: Color,
  photoShape: Shape,
  isAr: Boolean,
  scale: Float,
  textColor: Color,
  textMuted: Color,
) {
  Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    Surface(
      shape = RoundedCornerShape(10.dp),
      color = primaryColor,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier.padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        PhotoComponent(cvData.personal.photoUri, photoShape, 64.dp, Color.White)
        Column {
          Text(
            text = cvData.personal.fullName,
            fontSize = 19.sp * scale,
            fontWeight = FontWeight.Black,
            color = Color.White
          )
          Text(
            text = cvData.personal.title,
            fontSize = 12.5.sp * scale,
            color = Emerald500
          )
        }
      }
    }

    ContactBar(cvData, textMuted, scale)

    if (cvData.personal.summary.isNotBlank()) {
      SectionTitle(if (isAr) "الرؤية والقيادة" else "Executive Profile", primaryColor, scale)
      Text(text = cvData.personal.summary, fontSize = 11.5.sp * scale, lineHeight = 17.sp * scale, color = textColor)
    }

    if (cvData.experiences.isNotEmpty()) {
      SectionTitle(if (isAr) "المسيرة القيادية والمهنية" else "Leadership History", primaryColor, scale)
      cvData.experiences.forEach { exp ->
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)) {
          Text(text = exp.position, fontSize = 12.5.sp * scale, fontWeight = FontWeight.Bold, color = primaryColor)
          Text(text = "${exp.company} | ${exp.dates}", fontSize = 11.sp * scale, color = textMuted)
          if (exp.desc.isNotBlank()) Text(text = exp.desc, fontSize = 11.sp * scale, color = textColor)
        }
      }
    }

    if (cvData.skills.isNotEmpty()) {
      SectionTitle(if (isAr) "المهارات الاستراتيجية" else "Core Competencies", primaryColor, scale)
      SkillsFlowChips(cvData.skills, primaryColor, textColor, scale)
    }
  }
}

// 6. ATS FRIENDLY STANDARD TEMPLATE
@Composable
private fun AtsTemplate(
  cvData: CvData,
  primaryColor: Color,
  photoShape: Shape,
  isAr: Boolean,
  scale: Float,
  textColor: Color,
  textMuted: Color,
) {
  Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
    // ATS pure text header
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = cvData.personal.fullName.uppercase(),
        fontSize = 18.sp * scale,
        fontWeight = FontWeight.Bold,
        color = Color.Black
      )
      Text(
        text = cvData.personal.title,
        fontSize = 12.sp * scale,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF333333)
      )
      Spacer(modifier = Modifier.height(4.dp))
      val contacts = listOfNotNull(
        cvData.personal.email.takeIf { it.isNotBlank() },
        cvData.personal.phone.takeIf { it.isNotBlank() },
        cvData.personal.address.takeIf { it.isNotBlank() },
        cvData.personal.website.takeIf { it.isNotBlank() }
      ).joinToString(" | ")
      Text(
        text = contacts,
        fontSize = 10.sp * scale,
        color = Color(0xFF555555),
        textAlign = TextAlign.Center
      )
    }

    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.Black))

    if (cvData.personal.summary.isNotBlank()) {
      Text(text = if (isAr) "النبذة الشخصية" else "PROFESSIONAL SUMMARY", fontSize = 12.sp * scale, fontWeight = FontWeight.Bold, color = Color.Black)
      Text(text = cvData.personal.summary, fontSize = 11.sp * scale, lineHeight = 16.sp * scale, color = Color.Black)
    }

    if (cvData.experiences.isNotEmpty()) {
      Text(text = if (isAr) "الخبرات المهنية" else "WORK EXPERIENCE", fontSize = 12.sp * scale, fontWeight = FontWeight.Bold, color = Color.Black)
      cvData.experiences.forEach { exp ->
        Column(modifier = Modifier.padding(bottom = 4.dp)) {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "${exp.position}, ${exp.company}", fontSize = 11.5.sp * scale, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = exp.dates, fontSize = 10.sp * scale, color = Color.Black)
          }
          if (exp.desc.isNotBlank()) Text(text = exp.desc, fontSize = 10.5.sp * scale, color = Color.Black)
        }
      }
    }

    if (cvData.education.isNotEmpty()) {
      Text(text = if (isAr) "المؤهلات التعليمية" else "EDUCATION", fontSize = 12.sp * scale, fontWeight = FontWeight.Bold, color = Color.Black)
      cvData.education.forEach { edu ->
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text(text = "${edu.degree}, ${edu.school}", fontSize = 11.sp * scale, fontWeight = FontWeight.Bold, color = Color.Black)
          Text(text = edu.dates, fontSize = 10.sp * scale, color = Color.Black)
        }
      }
    }

    if (cvData.skills.isNotEmpty()) {
      Text(text = if (isAr) "المهارات" else "SKILLS", fontSize = 12.sp * scale, fontWeight = FontWeight.Bold, color = Color.Black)
      Text(
        text = cvData.skills.joinToString(" • ") { it.name },
        fontSize = 11.sp * scale,
        color = Color.Black
      )
    }
  }
}

// 7. ELEGANT SERIF TEMPLATE
@Composable
private fun ElegantTemplate(
  cvData: CvData,
  primaryColor: Color,
  photoShape: Shape,
  isAr: Boolean,
  scale: Float,
  textColor: Color,
  textMuted: Color,
) {
  Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      PhotoComponent(cvData.personal.photoUri, photoShape, 68.dp, primaryColor)
      Column {
        Text(
          text = cvData.personal.fullName,
          fontSize = 21.sp * scale,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Serif,
          color = primaryColor
        )
        Text(
          text = cvData.personal.title,
          fontSize = 12.5.sp * scale,
          fontFamily = FontFamily.Serif,
          color = textMuted
        )
      }
    }

    Box(modifier = Modifier.fillMaxWidth().height(1.5.dp).background(primaryColor))
    ContactBar(cvData, textMuted, scale)

    if (cvData.personal.summary.isNotBlank()) {
      Text(text = if (isAr) "— النبذة التعريفية —" else "— Profile —", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 13.sp * scale, color = primaryColor)
      Text(text = cvData.personal.summary, fontFamily = FontFamily.Serif, fontSize = 11.5.sp * scale, lineHeight = 17.sp * scale, color = textColor)
    }

    if (cvData.experiences.isNotEmpty()) {
      Text(text = if (isAr) "— الخبرات الأكاديمية والمهنية —" else "— Experience —", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 13.sp * scale, color = primaryColor)
      cvData.experiences.forEach { exp ->
        Column {
          Text(text = "${exp.position} / ${exp.company}", fontFamily = FontFamily.Serif, fontSize = 12.sp * scale, fontWeight = FontWeight.Bold, color = primaryColor)
          Text(text = exp.dates, fontSize = 10.sp * scale, color = textMuted)
          if (exp.desc.isNotBlank()) Text(text = exp.desc, fontFamily = FontFamily.Serif, fontSize = 11.sp * scale, color = textColor)
          Spacer(modifier = Modifier.height(4.dp))
        }
      }
    }

    if (cvData.skills.isNotEmpty()) {
      Text(text = if (isAr) "— المهارات —" else "— Skills —", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 13.sp * scale, color = primaryColor)
      SkillsFlowChips(cvData.skills, primaryColor, textColor, scale)
    }
  }
}

// Common Shared Components
@Composable
private fun PhotoComponent(photoUri: String?, shape: Shape, size: androidx.compose.ui.unit.Dp, borderColor: Color) {
  Box(
    modifier = Modifier
      .size(size)
      .clip(shape)
      .background(Color(0xFFE2E8F0))
      .border(2.dp, borderColor, shape),
    contentAlignment = Alignment.Center
  ) {
    if (!photoUri.isNullOrBlank()) {
      AsyncImage(
        model = photoUri,
        contentDescription = "CV Photo",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize().clip(shape)
      )
    } else {
      Icon(
        imageVector = Icons.Default.Person,
        contentDescription = null,
        tint = Color(0xFF64748B),
        modifier = Modifier.size(size * 0.55f)
      )
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ContactBar(cvData: CvData, textColor: Color, scale: Float) {
  FlowRow(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    if (cvData.personal.email.isNotBlank()) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Email, contentDescription = null, tint = textColor, modifier = Modifier.size(12.dp))
        Spacer(modifier = Modifier.width(3.dp))
        Text(text = cvData.personal.email, fontSize = 10.5.sp * scale, color = textColor)
      }
    }
    if (cvData.personal.phone.isNotBlank()) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Phone, contentDescription = null, tint = textColor, modifier = Modifier.size(12.dp))
        Spacer(modifier = Modifier.width(3.dp))
        Text(text = cvData.personal.phone, fontSize = 10.5.sp * scale, color = textColor)
      }
    }
    if (cvData.personal.address.isNotBlank()) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.LocationOn, contentDescription = null, tint = textColor, modifier = Modifier.size(12.dp))
        Spacer(modifier = Modifier.width(3.dp))
        Text(text = cvData.personal.address, fontSize = 10.5.sp * scale, color = textColor)
      }
    }
    if (cvData.personal.website.isNotBlank()) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Public, contentDescription = null, tint = textColor, modifier = Modifier.size(12.dp))
        Spacer(modifier = Modifier.width(3.dp))
        Text(text = cvData.personal.website, fontSize = 10.5.sp * scale, color = textColor)
      }
    }
  }
}

@Composable
private fun SectionTitle(title: String, color: Color, scale: Float) {
  Column(modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 2.dp)) {
    Text(
      text = title,
      fontSize = 13.sp * scale,
      fontWeight = FontWeight.Bold,
      color = color
    )
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(color.copy(alpha = 0.4f))
    )
  }
}

@Composable
private fun DividerBar(color: Color) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(1.dp)
      .background(color)
  )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SkillsFlowChips(skills: List<com.example.data.model.SkillItem>, primaryColor: Color, textColor: Color, scale: Float) {
  FlowRow(
    horizontalArrangement = Arrangement.spacedBy(6.dp),
    verticalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    skills.forEach { skill ->
      Surface(
        shape = RoundedCornerShape(6.dp),
        color = primaryColor.copy(alpha = 0.08f),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(primaryColor.copy(alpha = 0.3f)))
      ) {
        Text(
          text = skill.name,
          fontSize = 10.5.sp * scale,
          fontWeight = FontWeight.SemiBold,
          color = textColor,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
      }
    }
  }
}
