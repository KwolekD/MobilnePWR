package com.example.mobilnepwr.data.dates

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AttendanceStatus{
    ABSENT,
    PRESENT
}

@Entity(tableName = "dates")
data class Date(
    @PrimaryKey(autoGenerate = true) val dateId: Int = 0,
    val courseId: Int,
    val date: String,
    val attendanceStatus: AttendanceStatus
)