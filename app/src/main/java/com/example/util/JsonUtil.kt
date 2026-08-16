package com.example.util

import com.example.data.model.CvData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object JsonUtil {
  private val moshi: Moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

  private val adapter = moshi.adapter(CvData::class.java).indent("  ")

  fun toJson(cvData: CvData): String {
    return try {
      adapter.toJson(cvData)
    } catch (e: Exception) {
      "{}"
    }
  }

  fun fromJson(jsonString: String): CvData? {
    return try {
      adapter.fromJson(jsonString)
    } catch (e: Exception) {
      null
    }
  }
}
