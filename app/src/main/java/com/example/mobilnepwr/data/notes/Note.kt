package com.example.mobilnepwr.data.notes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val noteId: Int,
    val courseId: Int, // Klucz obcy do Classes
    val title: String,
    val date: String,
    val content: String
)
