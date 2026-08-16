package com.example.data.db

import com.example.data.model.CvData
import com.example.util.JsonUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CvRepository(private val cvDao: CvDao) {

  fun getActiveCv(id: Long = 1L): Flow<CvData?> {
    return cvDao.getCvById(id).map { entity ->
      entity?.let { JsonUtil.fromJson(it.jsonContent) }
    }
  }

  suspend fun saveActiveCv(cvData: CvData, id: Long = 1L) {
    val json = JsonUtil.toJson(cvData)
    val title = cvData.personal.fullName.ifBlank { "My CV" }
    cvDao.insertOrUpdate(
      CvEntity(
        id = id,
        title = title,
        jsonContent = json,
        lastUpdated = System.currentTimeMillis()
      )
    )
  }

  suspend fun resetCv(id: Long = 1L) {
    val defaultCv = CvData.defaultData()
    saveActiveCv(defaultCv, id)
  }
}
