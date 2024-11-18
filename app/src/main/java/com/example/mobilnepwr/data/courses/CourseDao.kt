package com.example.mobilnepwr.data.courses

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mobilnepwr.data.relations.CourseWithDates
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClass(classEntity: Course)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(classesEntity: List<Course>)

    @Delete
    suspend fun delete(course: Course)

    @Update
    suspend fun update(course: Course)

    @Query("DELETE FROM courses")
    suspend fun clearDatabase()

    @Query("SELECT * FROM courses WHERE courseId = :id")
    fun getClassById(id: Int): Flow<Course>

    @Query("SELECT * FROM courses ORDER BY name ASC")
    fun getAllClasses(): Flow<List<Course>>

    @Query("SELECT * FROM courses WHERE name = :name AND type = :type")
    fun getCourseByNameAndType(name: String,type: String): Flow<Course>

    @Transaction
    @Query("SELECT * FROM courses WHERE courseId = :classId")
    suspend fun getClassWithDates(classId: Int): List<CourseWithDates>

}