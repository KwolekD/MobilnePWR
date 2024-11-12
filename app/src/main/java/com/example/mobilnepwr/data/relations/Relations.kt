package com.example.mobilnepwr.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.data.dates.Date
import com.example.mobilnepwr.data.deadlines.Deadline
import com.example.mobilnepwr.data.images.Image
import com.example.mobilnepwr.data.notes.Note


data class CourseWithDates(
    @Embedded val classEntity: Course,
    @Relation(
        parentColumn = "courseId",
        entityColumn = "courseId"
    )
    val dates: List<Date>
)

data class CourseWithNotes(
    @Embedded val classEntity: Course,
    @Relation(
        parentColumn = "courseId",
        entityColumn = "courseId"
    )
    val notes: List<Note>
)

data class CourseWithDeadlines(
    @Embedded val classEntity: Course,
    @Relation(
        parentColumn = "courseId",
        entityColumn = "courseId"
    )
    val notes: List<Deadline>
)

data class CourseWithImages(
    @Embedded val classEntity: Course,
    @Relation(
        parentColumn = "courseId",
        entityColumn = "courseId"
    )
    val notes: List<Image>
)