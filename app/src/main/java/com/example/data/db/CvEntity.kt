package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cv_documents")
data class CvEntity(
  @PrimaryKey val id: Long = 1L,
  val title: String,
  val jsonContent: String,
  val lastUpdated: Long = System.currentTimeMillis(),
)
