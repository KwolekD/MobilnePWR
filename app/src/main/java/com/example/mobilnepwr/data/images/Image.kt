package com.example.mobilnepwr.data.images

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Image(
    @PrimaryKey(autoGenerate = true)
    val imageId: Int = 0,
    val courseId: Int,
    val filePath: String
)

data class ImageDetails(
    val imageId: Int,
    val courseId: Int,
    val filePath: String
)

fun Image.toImageDetails() = ImageDetails(
    imageId = imageId,
    courseId = courseId,
    filePath = filePath
)

fun ImageDetails.toImage() = Image(
    imageId = imageId,
    courseId = courseId,
    filePath = filePath
)