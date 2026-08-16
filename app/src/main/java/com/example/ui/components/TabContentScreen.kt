package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.CvData
import com.example.data.model.EducationItem
import com.example.data.model.ExperienceItem
import com.example.data.model.ProjectItem
import com.example.data.model.SkillItem
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Red500

@Composable
fun TabContentScreen(
  cvData: CvData,
  onUpdatePersonalFullName: (String) -> Unit,
  onUpdatePersonalTitle: (String) -> Unit,
  onUpdatePersonalEmail: (String) -> Unit,
  onUpdatePersonalPhone: (String) -> Unit,
  onUpdatePersonalAddress: (String) -> Unit,
  onUpdatePersonalWebsite: (String) -> Unit,
  onUpdatePersonalSummary: (String) -> Unit,
  onUpdatePersonalPhoto: (String?) -> Unit,
  onAddExperience: () -> Unit,
  onUpdateExperience: (Int, (ExperienceItem) -> ExperienceItem) -> Unit,
  onRemoveExperience: (Int) -> Unit,
  onAddEducation: () -> Unit,
  onUpdateEducation: (Int, (EducationItem) -> EducationItem) -> Unit,
  onRemoveEducation: (Int) -> Unit,
  onAddSkill: () -> Unit,
  onUpdateSkill: (Int, (SkillItem) -> SkillItem) -> Unit,
  onRemoveSkill: (Int) -> Unit,
  onAddProject: () -> Unit,
  onUpdateProject: (Int, (ProjectItem) -> ProjectItem) -> Unit,
  onRemoveProject: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val isAr = cvData.lang == "ar"

  val openSections = remember {
    mutableStateMapOf(
      "personal" to true,
      "experience" to true,
      "education" to false,
      "skills" to false,
      "projects" to false
    )
  }

  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri: Uri? ->
    uri?.let { onUpdatePersonalPhoto(it.toString()) }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // 1. Personal Details Accordion
    item {
      AccordionCard(
        title = if (isAr) "👤 البيانات الشخصية" else "👤 Personal Details",
        isOpen = openSections["personal"] == true,
        onToggle = { openSections["personal"] = !(openSections["personal"] ?: false) }
      ) {
        Column(
          modifier = Modifier.fillMaxWidth().padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Photo upload section
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              if (!cvData.personal.photoUri.isNullOrBlank()) {
                AsyncImage(
                  model = cvData.personal.photoUri,
                  contentDescription = "Profile Photo",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.size(64.dp).clip(CircleShape)
                )
              } else {
                Icon(
                  imageVector = Icons.Default.Person,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(36.dp)
                )
              }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                  onClick = { photoPickerLauncher.launch("image/*") },
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.height(36.dp).testTag("button_upload_photo")
                ) {
                  Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(text = if (isAr) "رفع صورة" else "Upload Photo", fontSize = 12.sp)
                }

                if (!cvData.personal.photoUri.isNullOrBlank()) {
                  OutlinedButton(
                    onClick = { onUpdatePersonalPhoto(null) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                  ) {
                    Text(text = if (isAr) "إزالة" else "Remove", fontSize = 12.sp, color = Red500)
                  }
                }
              }
              Text(
                text = if (isAr) "صورة احترافية تدعم نسبة 1:1" else "Recommended: square 1:1 photo",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          // Full Name
          OutlinedTextField(
            value = cvData.personal.fullName,
            onValueChange = onUpdatePersonalFullName,
            label = { Text(if (isAr) "الاسم الكامل" else "Full Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_full_name")
          )

          // Job Title
          OutlinedTextField(
            value = cvData.personal.title,
            onValueChange = onUpdatePersonalTitle,
            label = { Text(if (isAr) "المسمى الوظيفي" else "Job Title / Position") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_job_title")
          )

          // Email & Phone
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedTextField(
              value = cvData.personal.email,
              onValueChange = onUpdatePersonalEmail,
              label = { Text(if (isAr) "البريد الإلكتروني" else "Email") },
              singleLine = true,
              modifier = Modifier.weight(1f).testTag("input_email")
            )
            OutlinedTextField(
              value = cvData.personal.phone,
              onValueChange = onUpdatePersonalPhone,
              label = { Text(if (isAr) "رقم الهاتف" else "Phone") },
              singleLine = true,
              modifier = Modifier.weight(1f).testTag("input_phone")
            )
          }

          // Address & Website
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedTextField(
              value = cvData.personal.address,
              onValueChange = onUpdatePersonalAddress,
              label = { Text(if (isAr) "المدينة / الدولة" else "Address / City") },
              singleLine = true,
              modifier = Modifier.weight(1f).testTag("input_address")
            )
            OutlinedTextField(
              value = cvData.personal.website,
              onValueChange = onUpdatePersonalWebsite,
              label = { Text(if (isAr) "LinkedIn / موقع" else "LinkedIn / Website") },
              singleLine = true,
              modifier = Modifier.weight(1f).testTag("input_website")
            )
          }

          // Professional Summary
          OutlinedTextField(
            value = cvData.personal.summary,
            onValueChange = onUpdatePersonalSummary,
            label = { Text(if (isAr) "نبذة شخصية (About Me)" else "Professional Summary") },
            minLines = 3,
            maxLines = 6,
            modifier = Modifier.fillMaxWidth().testTag("input_summary")
          )
        }
      }
    }

    // 2. Work Experience Accordion
    item {
      AccordionCard(
        title = if (isAr) "💼 الخبرات المهنية (${cvData.experiences.size})" else "💼 Work Experience (${cvData.experiences.size})",
        isOpen = openSections["experience"] == true,
        onToggle = { openSections["experience"] = !(openSections["experience"] ?: false) }
      ) {
        Column(
          modifier = Modifier.fillMaxWidth().padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          cvData.experiences.forEachIndexed { index, exp ->
            ItemEditCard(
              title = "${if (isAr) "خبرة" else "Experience"} #${index + 1}",
              onDelete = { onRemoveExperience(index) }
            ) {
              OutlinedTextField(
                value = exp.position,
                onValueChange = { newVal -> onUpdateExperience(index) { it.copy(position = newVal) } },
                label = { Text(if (isAr) "المسمى الوظيفي" else "Job Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = exp.company,
                onValueChange = { newVal -> onUpdateExperience(index) { it.copy(company = newVal) } },
                label = { Text(if (isAr) "الشركة / المؤسسة" else "Company / Organization") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = exp.dates,
                onValueChange = { newVal -> onUpdateExperience(index) { it.copy(dates = newVal) } },
                label = { Text(if (isAr) "الفترة (مثال: 2021 - الحالي)" else "Period (e.g. 2021 - Present)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = exp.desc,
                onValueChange = { newVal -> onUpdateExperience(index) { it.copy(desc = newVal) } },
                label = { Text(if (isAr) "الوصف والمهام والإنجازات" else "Description & Achievements") },
                minLines = 2,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
              )
            }
          }

          Button(
            onClick = onAddExperience,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(42.dp).testTag("button_add_experience")
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (isAr) "+ إضافة خبرة عمل" else "+ Add Work Experience",
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    // 3. Education Accordion
    item {
      AccordionCard(
        title = if (isAr) "🎓 المؤهلات التعليمية (${cvData.education.size})" else "🎓 Education (${cvData.education.size})",
        isOpen = openSections["education"] == true,
        onToggle = { openSections["education"] = !(openSections["education"] ?: false) }
      ) {
        Column(
          modifier = Modifier.fillMaxWidth().padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          cvData.education.forEachIndexed { index, edu ->
            ItemEditCard(
              title = "${if (isAr) "مؤهل" else "Education"} #${index + 1}",
              onDelete = { onRemoveEducation(index) }
            ) {
              OutlinedTextField(
                value = edu.degree,
                onValueChange = { newVal -> onUpdateEducation(index) { it.copy(degree = newVal) } },
                label = { Text(if (isAr) "الدرجة العلمية والتخصص" else "Degree & Major") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = edu.school,
                onValueChange = { newVal -> onUpdateEducation(index) { it.copy(school = newVal) } },
                label = { Text(if (isAr) "الجامعة / الكلية" else "University / School") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = edu.dates,
                onValueChange = { newVal -> onUpdateEducation(index) { it.copy(dates = newVal) } },
                label = { Text(if (isAr) "سنوات الدراسة (مثال: 2017 - 2021)" else "Years (e.g. 2017 - 2021)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = edu.desc,
                onValueChange = { newVal -> onUpdateEducation(index) { it.copy(desc = newVal) } },
                label = { Text(if (isAr) "التقدير / تفاصيل إضافية" else "Honors / Additional Details") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
              )
            }
          }

          Button(
            onClick = onAddEducation,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(42.dp).testTag("button_add_education")
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (isAr) "+ إضافة مؤهل تعليمي" else "+ Add Education",
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    // 4. Skills Accordion
    item {
      AccordionCard(
        title = if (isAr) "⚡ المهارات والخبرات (${cvData.skills.size})" else "⚡ Skills (${cvData.skills.size})",
        isOpen = openSections["skills"] == true,
        onToggle = { openSections["skills"] = !(openSections["skills"] ?: false) }
      ) {
        Column(
          modifier = Modifier.fillMaxWidth().padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          cvData.skills.forEachIndexed { index, skill ->
            Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  OutlinedTextField(
                    value = skill.name,
                    onValueChange = { newVal -> onUpdateSkill(index) { it.copy(name = newVal) } },
                    label = { Text(if (isAr) "اسم المهارة" else "Skill Name") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  IconButton(
                    onClick = { onRemoveSkill(index) },
                    modifier = Modifier.size(32.dp)
                  ) {
                    Icon(Icons.Default.Close, contentDescription = "Delete", tint = Red500)
                  }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = if (isAr) "المستوى: ${skill.level}%" else "Level: ${skill.level}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.width(90.dp)
                  )
                  Slider(
                    value = skill.level.toFloat(),
                    onValueChange = { newVal -> onUpdateSkill(index) { it.copy(level = newVal.toInt()) } },
                    valueRange = 10f..100f,
                    steps = 17,
                    colors = SliderDefaults.colors(
                      thumbColor = MaterialTheme.colorScheme.primary,
                      activeTrackColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.weight(1f)
                  )
                }
              }
            }
          }

          Button(
            onClick = onAddSkill,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(42.dp).testTag("button_add_skill")
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (isAr) "+ إضافة مهارة" else "+ Add Skill",
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    // 5. Projects & Certifications Accordion
    item {
      AccordionCard(
        title = if (isAr) "🚀 المشاريع والشهادات (${cvData.projects.size})" else "🚀 Projects & Certifications (${cvData.projects.size})",
        isOpen = openSections["projects"] == true,
        onToggle = { openSections["projects"] = !(openSections["projects"] ?: false) }
      ) {
        Column(
          modifier = Modifier.fillMaxWidth().padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          cvData.projects.forEachIndexed { index, proj ->
            ItemEditCard(
              title = "${if (isAr) "مشروع / شهادة" else "Project / Certificate"} #${index + 1}",
              onDelete = { onRemoveProject(index) }
            ) {
              OutlinedTextField(
                value = proj.name,
                onValueChange = { newVal -> onUpdateProject(index) { it.copy(name = newVal) } },
                label = { Text(if (isAr) "اسم المشروع / عنوان الشهادة" else "Title / Certificate Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = proj.desc,
                onValueChange = { newVal -> onUpdateProject(index) { it.copy(desc = newVal) } },
                label = { Text(if (isAr) "التفاصيل والوصف" else "Description & Details") },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
              )
            }
          }

          Button(
            onClick = onAddProject,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(42.dp).testTag("button_add_project")
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (isAr) "+ إضافة مشروع / شهادة" else "+ Add Project / Certificate",
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun AccordionCard(
  title: String,
  isOpen: Boolean,
  onToggle: () -> Unit,
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
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onToggle() }
          .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          letterSpacing = (-0.1).sp,
          color = MaterialTheme.colorScheme.onSurface
        )
        Icon(
          imageVector = if (isOpen) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      AnimatedVisibility(
        visible = isOpen,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
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
  }
}

@Composable
private fun ItemEditCard(
  title: String,
  onDelete: () -> Unit,
  content: @Composable () -> Unit,
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(12.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = title,
          fontWeight = FontWeight.Bold,
          fontSize = 12.5.sp,
          color = MaterialTheme.colorScheme.primary
        )
        IconButton(
          onClick = onDelete,
          modifier = Modifier.size(28.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Delete",
            tint = Red500,
            modifier = Modifier.size(16.dp)
          )
        }
      }
      content()
    }
  }
}
