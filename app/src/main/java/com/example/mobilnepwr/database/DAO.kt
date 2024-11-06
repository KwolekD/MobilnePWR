package com.example.mobilnepwr.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction


@Dao
interface ClassDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(classEntity: ClassEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(classesEntity: List<ClassEntity>)

    @Query("SELECT * FROM classes WHERE classId = :id")
    suspend fun getClassById(id: Int): ClassEntity?

    @Query("SELECT * FROM classes")
    suspend fun getAllClasses(): List<ClassEntity>

    @Transaction
    @Query("SELECT * FROM classes WHERE classId = :classId")
    suspend fun getClassWithDates(classId: Int): List<ClassWithDates>
}

@Dao
interface NoteDao {
    @Insert
    suspend fun insertNote(noteEntity: NoteEntity)

    @Query("SELECT * FROM notes WHERE classId = :classId")
    suspend fun getNotesForClass(classId: Int): List<NoteEntity>
}

@Dao
interface ClassDateDao{
    @Insert
    suspend fun insertClassDate(classDateEntity: ClassDateEntity)

    @Query("SELECT * FROM class_dates WHERE classId = :classId")
    suspend fun getDatesForClass(classId: Int): List<ClassDateEntity>
}

@Dao
interface DeadlineDao {
    @Insert
    suspend fun insertDeadline(deadlineEntity: DeadlineEntity)

    @Query("SELECT * FROM deadlines WHERE classId = :classId")
    suspend fun getDeadlinesForClass(classId: Int): List<DeadlineEntity>
}

@Dao
interface ImageDao {
    @Insert
    suspend fun insertImage(imageEntity: ImageEntity)

    @Query("SELECT * FROM images WHERE classId = :classId")
    suspend fun getImagesForClass(classId: Int): List<ImageEntity>
}