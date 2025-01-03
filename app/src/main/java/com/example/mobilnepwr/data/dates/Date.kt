package com.example.mobilnepwr.data.dates

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.mobilnepwr.data.courses.Course
import java.time.LocalDate

@Entity(
    tableName = "dates",
    foreignKeys = [
        ForeignKey(
            entity = Course::class,
            parentColumns = ["courseId"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("courseId")]
)
data class Date(
    @PrimaryKey(autoGenerate = true) val dateId: Int = 0,
    val courseId: Int,
    val date: LocalDate,
    val startTime: String,
    val endTime: String,
    val attendance: Boolean
)