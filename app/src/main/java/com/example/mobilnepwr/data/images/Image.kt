package com.example.mobilnepwr.data.images

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Image(
    @PrimaryKey(autoGenerate = true)
    val imageId: Int,
    val courseId: Int,
    val filePath: String
)
