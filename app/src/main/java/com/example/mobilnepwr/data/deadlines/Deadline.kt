package com.example.mobilnepwr.data.deadlines

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deadlines")
data class Deadline(
    @PrimaryKey(autoGenerate = true) val deadlineId: Int,
    val courseId: Int,
    val title: String,
    val date: String,
    val note: String
)
