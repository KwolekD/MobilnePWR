package com.example.mobilnepwr.data.deadlines

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DeadlineDao {
    @Insert
    suspend fun insertDeadline(deadline: Deadline)

    @Update
    suspend fun updateDeadline(deadline: Deadline)

    @Delete
    suspend fun deleteDeadline(deadline: Deadline)

    @Query("SELECT * FROM deadlines WHERE courseId = :courseId")
    fun getDeadlinesByCourseId(courseId: Int): Flow<List<Deadline>>

    @Query("SELECT * FROM deadlines WHERE deadlineId = :deadlineId")
    fun getDeadline(deadlineId: Int): Flow<Deadline>


    @Query("SELECT * FROM deadlines")
    fun getAllDeadlines(): Flow<List<Deadline>>
}