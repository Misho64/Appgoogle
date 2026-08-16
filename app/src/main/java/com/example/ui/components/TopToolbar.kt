package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Red500
import com.example.ui.theme.Red50

@Composable
fun TopToolbar(
  isAr: Boolean,
  isDarkMode: Boolean,
  canUndo: Boolean,
  canRedo: Boolean,
  lastSavedTime: String,
  isSaving: Boolean,
  onPrintPdf: () -> Unit,
  onExportJson: () -> Unit,
  onImportJson: () -> Unit,
  onUndo: () -> Unit,
  onRedo: () -> Unit,
  onToggleLang: () -> Unit,
  onToggleDarkMode: () -> Unit,
  onResetData: () -> Unit,
  onTogglePreview: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var showResetDialog by remember { mutableStateOf(false) }

  if (showResetDialog) {
    AlertDialog(
      onDismissRequest = { showResetDialog = false },
      title = {
        Text(
          text = if (isAr) "إعادة ضبط البيانات" else "Reset CV Data",
          fontWeight = FontWeight.Bold
        )
      },
      text = {
        Text(
          text = if (isAr)
            "هل أنت متأكد من إعادة ضبط وتعيين كافة بيانات السيرة الذاتية إلى الإعدادات الافتراضية؟"
          else
            "Are you sure you want to reset all CV information to default sample data?"
        )
      },
      confirmButton = {
        Button(
          onClick = {
            showResetDialog = false
            onResetData()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Red500)
        ) {
          Text(text = if (isAr) "إعادة ضبط" else "Reset")
        }
      },
      dismissButton = {
        TextButton(onClick = { showResetDialog = false }) {
          Text(text = if (isAr) "إلغاء" else "Cancel")
        }
      }
    )
  }

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    shadowElevation = 2.dp
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 14.dp)) {
      // Top Row: Brand & Status & Quick preview
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Brand Title with sleek circular badge
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = if (isAr) "س" else "M",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = if (isAr) "سيرتي الاحترافية Pro" else "CV Builder Pro",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.2).sp,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        // Auto-save indicator
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 4.dp)
        ) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(if (isSaving) Color(0xFFF59E0B) else Emerald500)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isSaving) {
              if (isAr) "جارِ الحفظ..." else "Saving..."
            } else if (lastSavedTime.isNotBlank()) {
              if (isAr) "حفظ $lastSavedTime" else "Saved $lastSavedTime"
            } else {
              if (isAr) "حفظ تلقائي" else "Auto-saved"
            },
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Bottom Row: Action Buttons scrollable bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        // Print / PDF Button
        Button(
          onClick = onPrintPdf,
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .height(38.dp)
            .testTag("toolbar_print_button")
        ) {
          Icon(
            imageVector = Icons.Default.Print,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isAr) "طباعة / PDF" else "Print / PDF",
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold
          )
        }

        // Preview Toggle Button
        OutlinedButton(
          onClick = onTogglePreview,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .height(38.dp)
            .testTag("toolbar_preview_button")
        ) {
          Icon(
            imageVector = Icons.Default.Visibility,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isAr) "معاينة السيرة" else "Preview CV",
            fontSize = 12.5.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        // Export JSON Button
        OutlinedButton(
          onClick = onExportJson,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .height(38.dp)
            .testTag("toolbar_export_button")
        ) {
          Icon(
            imageVector = Icons.Default.Download,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "JSON", fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
        }

        // Import JSON Button
        OutlinedButton(
          onClick = onImportJson,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .height(38.dp)
            .testTag("toolbar_import_button")
        ) {
          Icon(
            imageVector = Icons.Default.Upload,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (isAr) "استيراد" else "Import",
            fontSize = 12.5.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        // Undo Button
        IconButton(
          onClick = onUndo,
          enabled = canUndo,
          modifier = Modifier.testTag("toolbar_undo_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.Undo,
            contentDescription = "Undo",
            tint = if (canUndo) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            modifier = Modifier.size(20.dp)
          )
        }

        // Redo Button
        IconButton(
          onClick = onRedo,
          enabled = canRedo,
          modifier = Modifier.testTag("toolbar_redo_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.Redo,
            contentDescription = "Redo",
            tint = if (canRedo) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            modifier = Modifier.size(20.dp)
          )
        }

        // Language Switch Button
        OutlinedButton(
          onClick = onToggleLang,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .height(38.dp)
            .testTag("toolbar_lang_toggle_button")
        ) {
          Text(
            text = if (isAr) "English" else "عربي",
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold
          )
        }

        // Dark Mode Toggle Button
        IconButton(
          onClick = onToggleDarkMode,
          modifier = Modifier.testTag("toolbar_dark_mode_button")
        ) {
          Icon(
            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
            contentDescription = "Toggle Dark Mode",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp)
          )
        }

        // Reset Button
        IconButton(
          onClick = { showResetDialog = true },
          modifier = Modifier.testTag("toolbar_reset_button")
        ) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Reset Data",
            tint = Red500,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}
