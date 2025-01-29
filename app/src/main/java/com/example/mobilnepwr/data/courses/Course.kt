package com.example.mobilnepwr.data.courses

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "courses",
    indices = [Index(value = ["name", "type"], unique = true)]
)
data class Course(
    @PrimaryKey(autoGenerate = true) val courseId: Int = 0,
    val name: String,
    val type: String,
    val address: String,
    val building: String,
    val hall: String
)