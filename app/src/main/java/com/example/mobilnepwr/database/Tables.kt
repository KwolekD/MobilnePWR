package com.example.mobilnepwr.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

//enum types

enum class AttendanceStatus{
    ABSENT,
    PRESENT
}

//tables
@Entity(tableName = "classes")
data class  ClassEntity(
    @PrimaryKey(autoGenerate = true) val classId: Int = 0,
    val name: String,
    val timeStart: String,
    val timeEnd: String,
    val day: String,
//    val instructor: String,
    val type: String,
    val address: String,
    val building: String,
    val hall: String
)

@Entity(tableName = "class_dates")
data class ClassDateEntity(
    @PrimaryKey(autoGenerate = true) val dateId: Int,
    val classId: Int,
    val date: String,
    val attendanceStatus: AttendanceStatus
)

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val noteId: Int,
    val classId: Int, // Klucz obcy do Classes
    val title: String,
    val date: String,
    val content: String
)

@Entity(tableName = "deadlines")
data class DeadlineEntity(
    @PrimaryKey(autoGenerate = true) val deadlineId: Int,
    val classId: Int,
    val title: String,
    val date: String,
    val note: String
)

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true) val imageId: Int,
    val classId: Int,
    val filePath: String
)

//relations

data class ClassWithDates(
    @Embedded val classEntity: ClassEntity,
    @Relation(
        parentColumn = "classId",
        entityColumn = "classId"
    )
    val dates: List<ClassDateEntity>
)

data class ClassWithNotes(
    @Embedded val classEntity: ClassEntity,
    @Relation(
        parentColumn = "classId",
        entityColumn = "classId"
    )
    val notes: List<NoteEntity>
)

data class ClassWithDeadlines(
    @Embedded val classEntity: ClassEntity,
    @Relation(
        parentColumn = "classId",
        entityColumn = "classId"
    )
    val notes: List<DeadlineEntity>
)

data class ClassWithImages(
    @Embedded val classEntity: ClassEntity,
    @Relation(
        parentColumn = "classId",
        entityColumn = "classId"
    )
    val notes: List<ImageEntity>
)