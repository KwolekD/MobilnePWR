package com.example.mobilnepwr.data.notes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val noteId: Int = 0,
    val courseId: Int, // Klucz obcy do Classes
    val title: String,
    val date: LocalDate,
    val content: String
)
