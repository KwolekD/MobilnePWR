package com.example.mobilnepwr.data.dates


import android.util.Log
import biweekly.ICalendar
import biweekly.property.Summary
import com.example.mobilnepwr.data.courses.Course
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class DateRepository(private val dateDao: DateDao) {
    fun getDatesByCourseId(courseId: Int): Flow<List<Date>> =
        dateDao.getDatesByCourseId(courseId)

    suspend fun insertDate(item: Date) = dateDao.insertDate(item)

    suspend fun deleteDate(item: Date) = dateDao.deleteDate(item)

    suspend fun updateDate(item: Date) = dateDao.updateDate(item)

    fun getGreaterThan(date: LocalDate): Flow<List<Date>> = dateDao.getGreaterThan(date)
    fun getLessThan(date: LocalDate): Flow<List<Date>> = dateDao.getLessThan(date)


    suspend fun importDatesFromIcal(ical: ICalendar, courses: List<Course>) {
        ical.events.forEach { event ->
            val dateTimeStart = event.dateStart.value
            val localDateTimeStart =
                LocalDateTime.ofInstant(dateTimeStart.toInstant(), ZoneId.systemDefault())
            val timeStart = localDateTimeStart.toLocalTime().toString()
            val dateStart = localDateTimeStart.toLocalDate()
            print(dateStart.toString())
            Log.d("DateRepository", "dateStart: $dateStart")


            val dateTimeEnd = event.dateEnd.value
            val localDateTimeEnd =
                LocalDateTime.ofInstant(dateTimeEnd.toInstant(), ZoneId.systemDefault())
            val timeEnd = localDateTimeEnd.toLocalTime().toString()


            val courseName = getName(event.summary)
            val courseType = getType(event.summary)

            val courseId = courses.find { it.name == courseName && it.type == courseType }?.courseId

            if (courseId != null) {
                val date = Date(
                    courseId = courseId,
                    date = dateStart,
                    startTime = timeStart,
                    endTime = timeEnd,
                    attendance = true
                )
                insertDate(date)
            }
        }
    }

    private fun getType(summary: Summary): String {
        return summary.value.split(" ").first()
    }

    private fun getName(summary: Summary): String {
        return summary.value.split(" ").drop(2).joinToString(" ")
    }
}