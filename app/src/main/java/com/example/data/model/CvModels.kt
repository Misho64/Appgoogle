package com.example.data.model

import com.squareup.moshi.JsonClass
import java.util.UUID

@JsonClass(generateAdapter = true)
data class PersonalDetails(
  val fullName: String = "",
  val title: String = "",
  val email: String = "",
  val phone: String = "",
  val address: String = "",
  val website: String = "",
  val summary: String = "",
  val photoUri: String? = null,
)

@JsonClass(generateAdapter = true)
data class ExperienceItem(
  val id: String = UUID.randomUUID().toString(),
  val company: String = "",
  val position: String = "",
  val dates: String = "",
  val desc: String = "",
)

@JsonClass(generateAdapter = true)
data class EducationItem(
  val id: String = UUID.randomUUID().toString(),
  val school: String = "",
  val degree: String = "",
  val dates: String = "",
  val desc: String = "",
)

@JsonClass(generateAdapter = true)
data class SkillItem(
  val id: String = UUID.randomUUID().toString(),
  val name: String = "",
  val level: Int = 85,
)

@JsonClass(generateAdapter = true)
data class ProjectItem(
  val id: String = UUID.randomUUID().toString(),
  val name: String = "",
  val desc: String = "",
)

@JsonClass(generateAdapter = true)
data class CvData(
  val lang: String = "ar", // "ar" or "en"
  val template: String = "modern", // modern, minimal, corporate, creative, executive, ats, dark, elegant
  val photoShape: String = "circle", // circle, rounded, square
  val primaryColorHex: String = "#111827",
  val secondaryColorHex: String = "#374151",
  val fontSizeScale: Float = 1.0f,
  val personal: PersonalDetails = PersonalDetails(),
  val experiences: List<ExperienceItem> = emptyList(),
  val education: List<EducationItem> = emptyList(),
  val skills: List<SkillItem> = emptyList(),
  val projects: List<ProjectItem> = emptyList(),
) {
  companion object {
    fun defaultData(): CvData {
      return CvData(
        lang = "ar",
        template = "modern",
        photoShape = "circle",
        primaryColorHex = "#111827",
        secondaryColorHex = "#374151",
        fontSizeScale = 1.0f,
        personal = PersonalDetails(
          fullName = "مشعل سعيد عبد العزيز",
          title = "محاسب مالي وإداري",
          email = "mishal@example.com",
          phone = "+20 100 123 4567",
          address = "القاهرة، مصر",
          website = "https://linkedin.com/in/mishal",
          summary = "محاسب مالي متمكن يمتلك خبرة عملية في إعداد القوائم المالية، إدارة الحسابات العامة والتسويات البنكية، والتعامل مع الأنظمة المحاسبية الحديثة وبرامج ERP.",
          photoUri = null
        ),
        experiences = listOf(
          ExperienceItem(
            company = "شركة المجموعة العربية للتجارة",
            position = "محاسب حسابات عامة",
            dates = "2023 - الحالي",
            desc = "• إعداد وإغلاق القوائم المالية الشهرية والسنوية بدقة عالية وفق معايير المحاسبة الدولية.\n• إدارة حسابات الموردين والعملاء وإجراء التسويات البنكية ومطابقة الأرصدة."
          )
        ),
        education = listOf(
          EducationItem(
            school = "جامعة القاهرة",
            degree = "بكالوريوس التجارة والمحاسبة",
            dates = "2017 - 2021",
            desc = "تقدير عام: جيد جداً. التخصص: محاسبة ومراجعة مالية."
          )
        ),
        skills = listOf(
          SkillItem(name = "إعداد القوائم المالية", level = 95),
          SkillItem(name = "Excel المتقدم (Pivot & VLOOKUP)", level = 90),
          SkillItem(name = "برامج ERP (Odoo / Dynamics AX)", level = 85)
        ),
        projects = listOf(
          ProjectItem(
            name = "شهادة أخصائي محاسب معتمد (CMA Candidate)",
            desc = "دورة تدريبية متقدمة في التحليل المالي والموازنات التقديرية."
          )
        )
      )
    }

    fun defaultEnglishData(): CvData {
      return CvData(
        lang = "en",
        template = "modern",
        photoShape = "circle",
        primaryColorHex = "#111827",
        secondaryColorHex = "#374151",
        fontSizeScale = 1.0f,
        personal = PersonalDetails(
          fullName = "Mishal Said Abdelaziz",
          title = "Senior Financial Accountant",
          email = "mishal@example.com",
          phone = "+1 (555) 234-5678",
          address = "New York, USA",
          website = "https://linkedin.com/in/mishal",
          summary = "Detail-oriented Financial Accountant with over 4 years of experience preparing financial statements, managing general ledgers, executing bank reconciliations, and ensuring compliance with IFRS standards.",
          photoUri = null
        ),
        experiences = listOf(
          ExperienceItem(
            company = "Arabian Trading Group Corp",
            position = "General Ledger Accountant",
            dates = "2023 - Present",
            desc = "• Prepared and closed monthly and annual financial statements with high accuracy following IFRS standards.\n• Managed vendor and client reconciliations and audited ledger accounts."
          )
        ),
        education = listOf(
          EducationItem(
            school = "Cairo University",
            degree = "B.Sc. in Accounting & Business Administration",
            dates = "2017 - 2021",
            desc = "Graduated with High Honors (Very Good with Distinction). Major: Auditing & Financial Accounting."
          )
        ),
        skills = listOf(
          SkillItem(name = "Financial Statements (IFRS)", level = 95),
          SkillItem(name = "Advanced Excel (Pivot & VLOOKUP)", level = 90),
          SkillItem(name = "ERP Systems (Odoo / Dynamics AX)", level = 85)
        ),
        projects = listOf(
          ProjectItem(
            name = "Certified Management Accountant (CMA Candidate)",
            desc = "Advanced coursework in corporate financial planning and strategic budgeting."
          )
        )
      )
    }
  }
}
