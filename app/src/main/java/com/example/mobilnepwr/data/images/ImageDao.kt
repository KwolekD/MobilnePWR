package com.example.mobilnepwr.data.images

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Insert
    suspend fun insertImage(image: Image)

    @Update
    suspend fun updateImage(image: Image)

    @Delete
    suspend fun deleteImage(image: Image)

    @Query("SELECT * FROM images WHERE imageId = :imageId")
    fun getImage(imageId: Int): Flow<Image>


    @Query("SELECT * FROM images")
    fun getAllImages(): Flow<List<Image>>

    @Query("SELECT * FROM images WHERE courseId = :courseId")
    fun getImageByCourseId(courseId: Int): Flow<List<Image>>


}