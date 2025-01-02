package com.example.mobilnepwr.data.dates

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DateDao {
    @Insert
    suspend fun insertDate(classDateEntity: Date)

    @Delete
    suspend fun deleteDate(date: Date)

    @Update
    suspend fun updateDate(date: Date)

    @Query("SELECT * FROM dates WHERE courseId = :courseId")
    fun getDatesByCourseId(courseId: Int): Flow<List<Date>>

    @Query("SELECT * FROM dates WHERE dateId = :dateId")
    fun getDate(dateId: Int): Flow<Date>

    @Query("SELECT * FROM dates")
    fun getAllDates(): Flow<List<Date>>
}