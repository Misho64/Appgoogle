package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CvDao {
  @Query("SELECT * FROM cv_documents WHERE id = :id LIMIT 1")
  fun getCvById(id: Long): Flow<CvEntity?>

  @Query("SELECT * FROM cv_documents ORDER BY lastUpdated DESC")
  fun getAllCvs(): Flow<List<CvEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdate(cv: CvEntity)

  @Query("DELETE FROM cv_documents WHERE id = :id")
  suspend fun deleteCv(id: Long)
}
