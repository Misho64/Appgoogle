package com.example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.CvViewModel
import com.example.ui.components.DhikrBanner
import com.example.ui.components.LiveCvPreview
import com.example.ui.components.TabAtsScreen
import com.example.ui.components.TabContentScreen
import com.example.ui.components.TabDesignScreen
import com.example.ui.components.TabSuggestionsScreen
import com.example.ui.components.TopToolbar
import com.example.ui.theme.Emerald500
import com.example.ui.theme.MyApplicationTheme
import com.example.util.PdfHelper

class MainActivity : ComponentActivity() {
  private val viewModel: CvViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val uiState by viewModel.uiState.collectAsState()
      val context = LocalContext.current
      val isAr = uiState.cvData.lang == "ar"

      // Listen to toast events
      LaunchedEffect(key1 = true) {
        viewModel.toastEvent.collect { message ->
          Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
      }

      CompositionLocalProvider(
        LocalLayoutDirection provides if (isAr) LayoutDirection.Rtl else LayoutDirection.Ltr
      ) {
        MyApplicationTheme(darkTheme = uiState.isDarkMode) {
          CvAppScreen(
            uiState = uiState,
            viewModel = viewModel,
            onPrintPdf = {
              PdfHelper.printOrSavePdf(context, uiState.cvData)
            },
            onSharePdf = {
              PdfHelper.sharePdf(context, uiState.cvData)
            }
          )
        }
      }
    }
  }
}

@Composable
fun CvAppScreen(
  uiState: com.example.ui.CvUiState,
  viewModel: CvViewModel,
  onPrintPdf: () -> Unit,
  onSharePdf: () -> Unit,
) {
  val context = LocalContext.current
  val isAr = uiState.cvData.lang == "ar"

  var showExportDialog by remember { mutableStateOf(false) }
  var showImportDialog by remember { mutableStateOf(false) }
  var showFullPreviewDialog by remember { mutableStateOf(false) }
  var jsonImportInput by remember { mutableStateOf("") }

  // JSON Export Dialog
  if (showExportDialog) {
    val jsonString = remember { viewModel.exportJsonString() }
    AlertDialog(
      onDismissRequest = { showExportDialog = false },
      title = {
        Text(
          text = if (isAr) "تصدير بيانات السيرة (JSON)" else "Export CV JSON",
          fontWeight = FontWeight.Bold
        )
      },
      text = {
        Column {
          Text(
            text = if (isAr)
              "يمكنك نسخ هذا الكود البرمجي لحفظ نسختك أو استيرادها في أي وقت:"
            else
              "Copy this JSON payload to backup or transfer your CV data:",
            fontSize = 12.sp
          )
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = jsonString,
            onValueChange = {},
            readOnly = true,
            minLines = 6,
            maxLines = 10,
            modifier = Modifier.fillMaxWidth().testTag("export_json_field")
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("CV JSON", jsonString)
            clipboard.setPrimaryClip(clip)
            showExportDialog = false
            Toast.makeText(
              context,
              if (isAr) "تم نسخ JSON إلى الحافظة بنجاح!" else "JSON copied to clipboard!",
              Toast.LENGTH_SHORT
            ).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Emerald500)
        ) {
          Text(if (isAr) "نسخ الكود" else "Copy JSON")
        }
      },
      dismissButton = {
        TextButton(onClick = { showExportDialog = false }) {
          Text(if (isAr) "إغلاق" else "Close")
        }
      }
    )
  }

  // JSON Import Dialog
  if (showImportDialog) {
    AlertDialog(
      onDismissRequest = { showImportDialog = false },
      title = {
        Text(
          text = if (isAr) "استيراد بيانات السيرة (JSON)" else "Import CV JSON",
          fontWeight = FontWeight.Bold
        )
      },
      text = {
        Column {
          Text(
            text = if (isAr) "الصق كود JSON الخاص بسيرتك الذاتية هنا:" else "Paste your CV JSON code below:",
            fontSize = 12.sp
          )
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = jsonImportInput,
            onValueChange = { jsonImportInput = it },
            placeholder = { Text("{\"lang\": \"ar\", ...}") },
            minLines = 6,
            maxLines = 10,
            modifier = Modifier.fillMaxWidth().testTag("import_json_field")
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val success = viewModel.importJsonString(jsonImportInput)
            if (success) {
              showImportDialog = false
              jsonImportInput = ""
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Text(if (isAr) "استيراد الآن" else "Import Now")
        }
      },
      dismissButton = {
        TextButton(onClick = { showImportDialog = false }) {
          Text(if (isAr) "إلغاء" else "Cancel")
        }
      }
    )
  }

  // Fullscreen Preview Dialog
  if (showFullPreviewDialog) {
    Dialog(
      onDismissRequest = { showFullPreviewDialog = false },
      properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
      Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
          // Dialog Header
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            IconButton(
              onClick = { showFullPreviewDialog = false },
              modifier = Modifier.testTag("close_fullscreen_preview")
            ) {
              Icon(Icons.Default.Close, contentDescription = "Close")
            }

            Text(
              text = if (isAr) "معاينة السيرة الذاتية" else "CV Live Preview",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Button(
                onClick = onPrintPdf,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Emerald500)
              ) {
                Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = if (isAr) "طباعة / PDF" else "Print PDF", fontSize = 12.sp)
              }
            }
          }

          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(12.dp)
          ) {
            LiveCvPreview(cvData = uiState.cvData, modifier = Modifier.fillMaxSize())
          }
        }
      }
    }
  }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .windowInsetsPadding(WindowInsets.safeDrawing),
    topBar = {
      Column(modifier = Modifier.fillMaxWidth()) {
        // 1. Dhikr Banner
        DhikrBanner(
          state = uiState.dhikrState,
          isAr = isAr,
          onDone = { viewModel.dismissDhikr("done") },
          onCancel = { viewModel.dismissDhikr("cancel") },
          onClose = { viewModel.dismissDhikr("close") },
          onRefresh = { viewModel.showNextDhikr() }
        )

        // 2. Sticky Top Toolbar
        TopToolbar(
          isAr = isAr,
          isDarkMode = uiState.isDarkMode,
          canUndo = uiState.canUndo,
          canRedo = uiState.canRedo,
          lastSavedTime = uiState.lastSavedTime,
          isSaving = uiState.isSaving,
          onPrintPdf = onPrintPdf,
          onExportJson = { showExportDialog = true },
          onImportJson = { showImportDialog = true },
          onUndo = { viewModel.undo() },
          onRedo = { viewModel.redo() },
          onToggleLang = { viewModel.toggleLanguage() },
          onToggleDarkMode = { viewModel.toggleDarkMode() },
          onResetData = { viewModel.resetData() },
          onTogglePreview = { showFullPreviewDialog = true }
        )
      }
    },
    bottomBar = {
      // Modern Bottom Navigation for Compact screens
      NavigationBar(
        containerColor = if (uiState.isDarkMode) Color(0xFF1D1B20) else Color(0xFFF7F2FA),
        tonalElevation = 3.dp
      ) {
        val tabs = listOf(
          Triple(0, if (isAr) "المحتوى" else "Content", Icons.Default.Edit),
          Triple(1, if (isAr) "التصميم" else "Design", Icons.Default.ColorLens),
          Triple(2, if (isAr) "مقترحات" else "Suggestions", Icons.Default.AutoAwesome),
          Triple(3, if (isAr) "فاحص ATS" else "ATS", Icons.Default.Speed)
        )

        tabs.forEach { (index, label, icon) ->
          NavigationBarItem(
            selected = uiState.activeTab == index,
            onClick = { viewModel.setActiveTab(index) },
            icon = { Icon(icon, contentDescription = label) },
            label = {
              Text(
                label,
                fontSize = 11.5.sp,
                fontWeight = if (uiState.activeTab == index) FontWeight.Bold else FontWeight.Medium
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = if (uiState.isDarkMode) Color(0xFFD0BCFF) else Color(0xFF6750A4),
              selectedTextColor = if (uiState.isDarkMode) Color(0xFFD0BCFF) else Color(0xFF6750A4),
              indicatorColor = if (uiState.isDarkMode) Color(0xFF4F378B) else Color(0xFFE8DEF8),
              unselectedIconColor = if (uiState.isDarkMode) Color(0xFFCAC4D0) else Color(0xFF49454F),
              unselectedTextColor = if (uiState.isDarkMode) Color(0xFFCAC4D0) else Color(0xFF49454F)
            ),
            modifier = Modifier.testTag("nav_tab_$index")
          )
        }
      }
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showFullPreviewDialog = true },
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.testTag("fab_live_preview")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Visibility, contentDescription = "Preview")
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = if (isAr) "معاينة" else "Preview", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
      }
    }
  ) { innerPadding ->
    BoxWithConstraints(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      val isWideScreen = maxWidth >= 840.dp

      if (isWideScreen) {
        // Dual Pane: Left Editor Tabs, Right Live CV View
        Row(modifier = Modifier.fillMaxSize().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
          // Left Editor Column
          Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            ActiveTabContent(uiState, viewModel)
          }

          // Right Live CV Column
          Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Text(
              text = if (isAr) "📄 المعاينة الحية الفورية" else "📄 Real-Time Live Preview",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(bottom = 8.dp)
            )
            LiveCvPreview(cvData = uiState.cvData, modifier = Modifier.fillMaxSize())
          }
        }
      } else {
        // Single Pane: Active Tab content
        ActiveTabContent(uiState, viewModel)
      }
    }
  }
}

@Composable
private fun ActiveTabContent(
  uiState: com.example.ui.CvUiState,
  viewModel: CvViewModel,
) {
  when (uiState.activeTab) {
    0 -> TabContentScreen(
      cvData = uiState.cvData,
      onUpdatePersonalFullName = { name -> viewModel.updatePersonal { it.copy(fullName = name) } },
      onUpdatePersonalTitle = { title -> viewModel.updatePersonal { it.copy(title = title) } },
      onUpdatePersonalEmail = { email -> viewModel.updatePersonal { it.copy(email = email) } },
      onUpdatePersonalPhone = { phone -> viewModel.updatePersonal { it.copy(phone = phone) } },
      onUpdatePersonalAddress = { address -> viewModel.updatePersonal { it.copy(address = address) } },
      onUpdatePersonalWebsite = { web -> viewModel.updatePersonal { it.copy(website = web) } },
      onUpdatePersonalSummary = { sum -> viewModel.updatePersonal { it.copy(summary = sum) } },
      onUpdatePersonalPhoto = { photo -> viewModel.setProfilePhoto(photo) },
      onAddExperience = { viewModel.addExperience() },
      onUpdateExperience = { idx, upd -> viewModel.updateExperience(idx, upd) },
      onRemoveExperience = { idx -> viewModel.removeExperience(idx) },
      onAddEducation = { viewModel.addEducation() },
      onUpdateEducation = { idx, upd -> viewModel.updateEducation(idx, upd) },
      onRemoveEducation = { idx -> viewModel.removeEducation(idx) },
      onAddSkill = { viewModel.addSkill() },
      onUpdateSkill = { idx, upd -> viewModel.updateSkill(idx, upd) },
      onRemoveSkill = { idx -> viewModel.removeSkill(idx) },
      onAddProject = { viewModel.addProject() },
      onUpdateProject = { idx, upd -> viewModel.updateProject(idx, upd) },
      onRemoveProject = { idx -> viewModel.removeProject(idx) }
    )

    1 -> TabDesignScreen(
      cvData = uiState.cvData,
      onSelectTemplate = { tmpl -> viewModel.setTemplate(tmpl) },
      onSelectPrimaryColor = { color -> viewModel.setPrimaryColor(color) },
      onSelectPhotoShape = { shape -> viewModel.setPhotoShape(shape) },
      onSelectFontSizeScale = { scale -> viewModel.setFontSizeScale(scale) }
    )

    2 -> TabSuggestionsScreen(
      cvData = uiState.cvData,
      onApplySummary = { summary -> viewModel.applySummaryText(summary) },
      onAppendVerb = { verb -> viewModel.appendVerbToSummary(verb) },
      onAddSkill = { skillName -> viewModel.addSuggestedSkill(skillName) }
    )

    3 -> TabAtsScreen(
      atsResult = uiState.atsResult,
      isAr = uiState.cvData.lang == "ar"
    )
  }
}
