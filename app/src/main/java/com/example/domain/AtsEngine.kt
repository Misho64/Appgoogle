package com.example.domain

import com.example.data.model.CvData

data class AtsTip(
  val isPositive: Boolean,
  val text: String,
  val category: String,
)

data class AtsAnalysisResult(
  val score: Int,
  val grade: String,
  val tips: List<AtsTip>,
)

object AtsEngine {
  fun analyze(cvData: CvData): AtsAnalysisResult {
    val isAr = cvData.lang == "ar"
    var score = 100
    val tips = mutableListOf<AtsTip>()

    // Check Email
    if (cvData.personal.email.isBlank()) {
      score -= 15
      tips.add(
        AtsTip(
          isPositive = false,
          text = if (isAr) "❌ البريد الإلكتروني مفقود (أساسي للتواصل وفحص ATS)." else "❌ Missing Email address (critical for ATS contact extraction).",
          category = "contact"
        )
      )
    } else if (!cvData.personal.email.contains("@") || !cvData.personal.email.contains(".")) {
      score -= 5
      tips.add(
        AtsTip(
          isPositive = false,
          text = if (isAr) "⚠️ صيغة البريد الإلكتروني قد تكون غير صحيحة." else "⚠️ Email address format looks unusual.",
          category = "contact"
        )
      )
    } else {
      tips.add(
        AtsTip(
          isPositive = true,
          text = if (isAr) "✅ البريد الإلكتروني مسجل بشكل صحيح." else "✅ Valid Email address detected.",
          category = "contact"
        )
      )
    }

    // Check Phone
    if (cvData.personal.phone.isBlank()) {
      score -= 15
      tips.add(
        AtsTip(
          isPositive = false,
          text = if (isAr) "❌ رقم الهاتف مفقود." else "❌ Missing Phone number.",
          category = "contact"
        )
      )
    } else {
      tips.add(
        AtsTip(
          isPositive = true,
          text = if (isAr) "✅ رقم الهاتف متوفر." else "✅ Phone number provided.",
          category = "contact"
        )
      )
    }

    // Check Name & Title
    if (cvData.personal.fullName.isBlank()) {
      score -= 15
      tips.add(
        AtsTip(
          isPositive = false,
          text = if (isAr) "❌ الاسم الكامل مفقود." else "❌ Full Name is missing.",
          category = "personal"
        )
      )
    }
    if (cvData.personal.title.isBlank()) {
      score -= 10
      tips.add(
        AtsTip(
          isPositive = false,
          text = if (isAr) "⚠️ يُفضل إضافة مسمى وظيفي واضح في الرأس." else "⚠️ Professional Title is missing.",
          category = "personal"
        )
      )
    }

    // Check Summary
    val summary = cvData.personal.summary.trim()
    if (summary.isBlank()) {
      score -= 15
      tips.add(
        AtsTip(
          isPositive = false,
          text = if (isAr) "❌ النبذة الشخصية مفقودة تماماً." else "❌ Professional summary is missing.",
          category = "summary"
        )
      )
    } else if (summary.length < 40) {
      score -= 10
      tips.add(
        AtsTip(
          isPositive = false,
          text = if (isAr) "⚠️ النبذة الشخصية قصيرة جداً (أقل من 40 حرفاً)." else "⚠️ Summary is too short (under 40 characters).",
          category = "summary"
        )
      )
    } else {
      tips.add(
        AtsTip(
          isPositive = true,
          text = if (isAr) "✅ النبذة الشخصية مكتوبة ومفصلة جيداً." else "✅ Professional summary is well elaborated.",
          category = "summary"
        )
      )
    }

    // Check Experience
    if (cvData.experiences.isEmpty()) {
      score -= 20
      tips.add(
        AtsTip(
          isPositive = false,
          text = if (isAr) "❌ لا توجد أي خبرات مهنية مضافة." else "❌ No work experience entries added.",
          category = "experience"
        )
      )
    } else {
      var hasEmptyDesc = false
      cvData.experiences.forEach { exp ->
        if (exp.desc.isBlank()) hasEmptyDesc = true
      }
      if (hasEmptyDesc) {
        score -= 5
        tips.add(
          AtsTip(
            isPositive = false,
            text = if (isAr) "⚠️ بعض خبرات العمل لا تحتوي على مهام أو إنجازات محددة." else "⚠️ Some work experiences lack task details/achievements.",
            category = "experience"
          )
        )
      } else {
        tips.add(
          AtsTip(
            isPositive = true,
            text = if (isAr) "✅ قسم الخبرات المهنية منظم ومدعوم بالمهام." else "✅ Work experiences are well structured with duties.",
            category = "experience"
          )
        )
      }
    }

    // Check Education
    if (cvData.education.isEmpty()) {
      score -= 10
      tips.add(
        AtsTip(
          isPositive = false,
          text = if (isAr) "⚠️ يُفضل إضافة مؤهل تعليمي واحد على الأقل." else "⚠️ Add at least one education credential.",
          category = "education"
        )
      )
    } else {
      tips.add(
        AtsTip(
          isPositive = true,
          text = if (isAr) "✅ المؤهلات التعليمية مسجلة بنجاح." else "✅ Education section is complete.",
          category = "education"
        )
      )
    }

    // Check Skills
    if (cvData.skills.size < 3) {
      score -= 10
      tips.add(
        AtsTip(
          isPositive = false,
          text = if (isAr) "⚠️ أضف 3 مهارات على الأقل لزيادة فرص المطابقة مع خوارزميات ATS." else "⚠️ Add at least 3 skills to improve keyword matching.",
          category = "skills"
        )
      )
    } else {
      tips.add(
        AtsTip(
          isPositive = true,
          text = if (isAr) "✅ تم إدراج (${cvData.skills.size}) مهارات متنوعة." else "✅ (${cvData.skills.size}) relevant skills included.",
          category = "skills"
        )
      )
    }

    // Normalizing Score
    val finalScore = score.coerceIn(0, 100)
    val grade = when {
      finalScore >= 90 -> if (isAr) "ممتاز (ATS Ready)" else "Excellent (ATS Ready)"
      finalScore >= 75 -> if (isAr) "جيد جداً" else "Very Good"
      finalScore >= 50 -> if (isAr) "مقبول" else "Fair"
      else -> if (isAr) "يحتاج تحسين" else "Needs Improvement"
    }

    return AtsAnalysisResult(
      score = finalScore,
      grade = grade,
      tips = tips
    )
  }
}
