package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.CvDatabase
import com.example.data.db.CvRepository
import com.example.data.model.CvData
import com.example.data.model.EducationItem
import com.example.data.model.ExperienceItem
import com.example.data.model.PersonalDetails
import com.example.data.model.ProjectItem
import com.example.data.model.SkillItem
import com.example.domain.AtsAnalysisResult
import com.example.domain.AtsEngine
import com.example.util.JsonUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DhikrState(
  val isVisible: Boolean = true,
  val text: String = "صلي على النبي",
  val progress: Float = 1.0f,
  val isRunning: Boolean = true,
)

data class CvUiState(
  val cvData: CvData = CvData.defaultData(),
  val canUndo: Boolean = false,
  val canRedo: Boolean = false,
  val activeTab: Int = 0, // 0: Content, 1: Design, 2: Suggestions, 3: ATS
  val isDarkMode: Boolean = false,
  val isPreviewExpanded: Boolean = false,
  val dhikrState: DhikrState = DhikrState(),
  val atsResult: AtsAnalysisResult = AtsEngine.analyze(CvData.defaultData()),
  val lastSavedTime: String = "",
  val isSaving: Boolean = false,
)

class CvViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: CvRepository
  private val _uiState = MutableStateFlow(CvUiState())
  val uiState: StateFlow<CvUiState> = _uiState.asStateFlow()

  private val _toastEvent = MutableSharedFlow<String>()
  val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

  // Undo / Redo History Stack
  private val history = mutableListOf<CvData>()
  private var historyIndex = -1
  private var isPerformingUndoRedo = false

  // Dhikr Phrases
  private val dhikrPhrases = listOf(
    "صلي على النبي",
    "أستغفر الله العظيم 100×",
    "لا إله إلا الله",
    "أشهد أن لا إله إلا الله وحده لا شريك له له الملك وله الحمد وهو على كل شيء قدير",
    "سبحان الله وبحمده، سبحان الله العظيم",
    "اللهم صل وسلم وبارك على نبينا محمد وعلى آله وصحبه",
    "لا حول ولا قوة إلا بالله العلي العظيم"
  )

  private var dhikrTimerJob: Job? = null
  private var autoSaveJob: Job? = null

  init {
    val db = CvDatabase.getDatabase(application)
    repository = CvRepository(db.cvDao())

    // Initialize with default
    val initialData = CvData.defaultData()
    recordHistory(initialData)

    // Load from Room DB
    viewModelScope.launch {
      repository.getActiveCv(1L).collect { savedCv ->
        if (savedCv != null && history.size <= 1) {
          _uiState.update {
            it.copy(
              cvData = savedCv,
              atsResult = AtsEngine.analyze(savedCv),
              lastSavedTime = getFormattedTime()
            )
          }
          history.clear()
          recordHistory(savedCv)
        }
      }
    }

    startDhikrBanner()
  }

  // --- Dhikr Banner Logic (7 seconds timer with progress line) ---
  fun startDhikrBanner() {
    val randomPhrase = dhikrPhrases.random()
    _uiState.update {
      it.copy(
        dhikrState = DhikrState(
          isVisible = true,
          text = randomPhrase,
          progress = 1f,
          isRunning = true
        )
      )
    }

    dhikrTimerJob?.cancel()
    dhikrTimerJob = viewModelScope.launch {
      val totalTimeMs = 7000L
      val intervalMs = 50L
      var elapsedMs = 0L

      while (elapsedMs < totalTimeMs) {
        delay(intervalMs)
        elapsedMs += intervalMs
        val remainingRatio = 1f - (elapsedMs.toFloat() / totalTimeMs)
        _uiState.update {
          it.copy(
            dhikrState = it.dhikrState.copy(
              progress = remainingRatio.coerceIn(0f, 1f)
            )
          )
        }
      }

      // Auto close when timer ends
      _uiState.update {
        it.copy(dhikrState = it.dhikrState.copy(isVisible = false, isRunning = false))
      }
    }
  }

  fun dismissDhikr(action: String) {
    dhikrTimerJob?.cancel()
    _uiState.update {
      it.copy(dhikrState = it.dhikrState.copy(isVisible = false, isRunning = false))
    }
    if (action == "done") {
      emitToast(if (_uiState.value.cvData.lang == "ar") "تقبل الله طاعتكم وجزاكم خيراً" else "May Allah reward you")
    }
  }

  fun showNextDhikr() {
    startDhikrBanner()
  }

  // --- Tab & UI Controls ---
  fun setActiveTab(tabIndex: Int) {
    _uiState.update { it.copy(activeTab = tabIndex) }
  }

  fun toggleDarkMode() {
    _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
  }

  fun togglePreviewExpanded() {
    _uiState.update { it.copy(isPreviewExpanded = !it.isPreviewExpanded) }
  }

  // --- Language Toggle ---
  fun toggleLanguage() {
    val currentLang = _uiState.value.cvData.lang
    val newLang = if (currentLang == "ar") "en" else "ar"
    
    val currentCv = _uiState.value.cvData
    val updatedCv = currentCv.copy(lang = newLang)
    applyCvChange(updatedCv)
    emitToast(if (newLang == "ar") "تم التحويل إلى اللغة العربية" else "Switched to English")
  }

  // --- Template, Style & Design ---
  fun setTemplate(template: String) {
    applyCvChange(_uiState.value.cvData.copy(template = template))
  }

  fun setPrimaryColor(colorHex: String) {
    applyCvChange(_uiState.value.cvData.copy(primaryColorHex = colorHex))
  }

  fun setSecondaryColor(colorHex: String) {
    applyCvChange(_uiState.value.cvData.copy(secondaryColorHex = colorHex))
  }

  fun setPhotoShape(shape: String) {
    applyCvChange(_uiState.value.cvData.copy(photoShape = shape))
  }

  fun setFontSizeScale(scale: Float) {
    applyCvChange(_uiState.value.cvData.copy(fontSizeScale = scale))
  }

  // --- Personal Details ---
  fun updatePersonal(updateBlock: (PersonalDetails) -> PersonalDetails) {
    val current = _uiState.value.cvData.personal
    val newPersonal = updateBlock(current)
    applyCvChange(_uiState.value.cvData.copy(personal = newPersonal))
  }

  fun setProfilePhoto(uriString: String?) {
    updatePersonal { it.copy(photoUri = uriString) }
  }

  // --- Experience CRUD ---
  fun addExperience() {
    val list = _uiState.value.cvData.experiences.toMutableList()
    list.add(ExperienceItem())
    applyCvChange(_uiState.value.cvData.copy(experiences = list))
  }

  fun updateExperience(index: Int, update: (ExperienceItem) -> ExperienceItem) {
    val list = _uiState.value.cvData.experiences.toMutableList()
    if (index in list.indices) {
      list[index] = update(list[index])
      applyCvChange(_uiState.value.cvData.copy(experiences = list))
    }
  }

  fun removeExperience(index: Int) {
    val list = _uiState.value.cvData.experiences.toMutableList()
    if (index in list.indices) {
      list.removeAt(index)
      applyCvChange(_uiState.value.cvData.copy(experiences = list))
    }
  }

  // --- Education CRUD ---
  fun addEducation() {
    val list = _uiState.value.cvData.education.toMutableList()
    list.add(EducationItem())
    applyCvChange(_uiState.value.cvData.copy(education = list))
  }

  fun updateEducation(index: Int, update: (EducationItem) -> EducationItem) {
    val list = _uiState.value.cvData.education.toMutableList()
    if (index in list.indices) {
      list[index] = update(list[index])
      applyCvChange(_uiState.value.cvData.copy(education = list))
    }
  }

  fun removeEducation(index: Int) {
    val list = _uiState.value.cvData.education.toMutableList()
    if (index in list.indices) {
      list.removeAt(index)
      applyCvChange(_uiState.value.cvData.copy(education = list))
    }
  }

  // --- Skills CRUD ---
  fun addSkill(name: String = "", level: Int = 85) {
    val list = _uiState.value.cvData.skills.toMutableList()
    list.add(SkillItem(name = name, level = level))
    applyCvChange(_uiState.value.cvData.copy(skills = list))
  }

  fun updateSkill(index: Int, update: (SkillItem) -> SkillItem) {
    val list = _uiState.value.cvData.skills.toMutableList()
    if (index in list.indices) {
      list[index] = update(list[index])
      applyCvChange(_uiState.value.cvData.copy(skills = list))
    }
  }

  fun removeSkill(index: Int) {
    val list = _uiState.value.cvData.skills.toMutableList()
    if (index in list.indices) {
      list.removeAt(index)
      applyCvChange(_uiState.value.cvData.copy(skills = list))
    }
  }

  // --- Projects CRUD ---
  fun addProject() {
    val list = _uiState.value.cvData.projects.toMutableList()
    list.add(ProjectItem())
    applyCvChange(_uiState.value.cvData.copy(projects = list))
  }

  fun updateProject(index: Int, update: (ProjectItem) -> ProjectItem) {
    val list = _uiState.value.cvData.projects.toMutableList()
    if (index in list.indices) {
      list[index] = update(list[index])
      applyCvChange(_uiState.value.cvData.copy(projects = list))
    }
  }

  fun removeProject(index: Int) {
    val list = _uiState.value.cvData.projects.toMutableList()
    if (index in list.indices) {
      list.removeAt(index)
      applyCvChange(_uiState.value.cvData.copy(projects = list))
    }
  }

  // --- Suggestion actions ---
  fun applySummaryText(text: String) {
    updatePersonal { it.copy(summary = text) }
    emitToast(if (_uiState.value.cvData.lang == "ar") "تم إدراج النبذة بنجاح!" else "Summary inserted successfully!")
  }

  fun appendVerbToSummary(verb: String) {
    val current = _uiState.value.cvData.personal.summary.trim()
    val updated = if (current.isEmpty()) "• $verb." else "$current\n• $verb."
    updatePersonal { it.copy(summary = updated) }
    emitToast(if (_uiState.value.cvData.lang == "ar") "تمت إضافة العبارة بنجاح!" else "Phrase added successfully!")
  }

  fun addSuggestedSkill(skillName: String) {
    val exists = _uiState.value.cvData.skills.any { it.name.equals(skillName, ignoreCase = true) }
    if (!exists) {
      addSkill(name = skillName, level = 90)
      emitToast(if (_uiState.value.cvData.lang == "ar") "تمت إضافة مهارة: $skillName" else "Added skill: $skillName")
    } else {
      emitToast(if (_uiState.value.cvData.lang == "ar") "المهارة موجودة بالفعل!" else "Skill already exists!")
    }
  }

  // --- History (Undo / Redo) ---
  private fun applyCvChange(newData: CvData) {
    if (!isPerformingUndoRedo) {
      recordHistory(newData)
    }
    _uiState.update {
      it.copy(
        cvData = newData,
        atsResult = AtsEngine.analyze(newData),
        canUndo = historyIndex > 0,
        canRedo = historyIndex < history.size - 1
      )
    }
    triggerAutoSave(newData)
  }

  private fun recordHistory(data: CvData) {
    // Truncate future states if we were in the middle of history
    while (history.size > historyIndex + 1) {
      history.removeAt(history.size - 1)
    }
    history.add(data)
    // Limit history stack size to 30
    if (history.size > 30) {
      history.removeAt(0)
    }
    historyIndex = history.size - 1
    _uiState.update {
      it.copy(
        canUndo = historyIndex > 0,
        canRedo = false
      )
    }
  }

  fun undo() {
    if (historyIndex > 0) {
      historyIndex--
      val prev = history[historyIndex]
      isPerformingUndoRedo = true
      _uiState.update {
        it.copy(
          cvData = prev,
          atsResult = AtsEngine.analyze(prev),
          canUndo = historyIndex > 0,
          canRedo = historyIndex < history.size - 1
        )
      }
      isPerformingUndoRedo = false
      triggerAutoSave(prev)
      emitToast(if (_uiState.value.cvData.lang == "ar") "تم التراجع" else "Undo executed")
    }
  }

  fun redo() {
    if (historyIndex < history.size - 1) {
      historyIndex++
      val next = history[historyIndex]
      isPerformingUndoRedo = true
      _uiState.update {
        it.copy(
          cvData = next,
          atsResult = AtsEngine.analyze(next),
          canUndo = historyIndex > 0,
          canRedo = historyIndex < history.size - 1
        )
      }
      isPerformingUndoRedo = false
      triggerAutoSave(next)
      emitToast(if (_uiState.value.cvData.lang == "ar") "تمت الإعادة" else "Redo executed")
    }
  }

  // --- JSON Import / Export & Reset ---
  fun exportJsonString(): String {
    return JsonUtil.toJson(_uiState.value.cvData)
  }

  fun importJsonString(jsonString: String): Boolean {
    val parsed = JsonUtil.fromJson(jsonString)
    return if (parsed != null) {
      applyCvChange(parsed)
      emitToast(if (parsed.lang == "ar") "تم استيراد البيانات بنجاح" else "Data imported successfully")
      true
    } else {
      emitToast("الملف غير صالح / Invalid JSON format")
      false
    }
  }

  fun resetData() {
    val isAr = _uiState.value.cvData.lang == "ar"
    val defaultData = if (isAr) CvData.defaultData() else CvData.defaultEnglishData()
    applyCvChange(defaultData)
    emitToast(if (isAr) "تمت إعادة تعيين البيانات" else "Data reset successfully")
  }

  // --- Auto Save to Room DB ---
  private fun triggerAutoSave(cvData: CvData) {
    autoSaveJob?.cancel()
    autoSaveJob = viewModelScope.launch {
      _uiState.update { it.copy(isSaving = true) }
      delay(800) // Debounce auto-save
      repository.saveActiveCv(cvData)
      val timeStr = getFormattedTime()
      _uiState.update {
        it.copy(
          isSaving = false,
          lastSavedTime = timeStr
        )
      }
    }
  }

  private fun getFormattedTime(): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date())
  }

  private fun emitToast(message: String) {
    viewModelScope.launch {
      _toastEvent.emit(message)
    }
  }
}
