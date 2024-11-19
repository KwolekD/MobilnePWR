package com.example.mobilnepwr.data.dates


import biweekly.ICalendar
import biweekly.property.Summary
import com.example.mobilnepwr.data.courses.Course
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.ZoneId

class DateRepository(private val dateDao: DateDao) {
    fun getAllDatesStream(): Flow<List<Date>> = dateDao.getAllDates()

    fun getDateStream(id: Int): Flow<Date?> = dateDao.getDate(id)

    suspend fun insertDate(item: Date) = dateDao.insertDate(item)

    suspend fun deleteDate(item: Date) = dateDao.deleteDate(item)

    suspend fun updateDate(item: Date) = dateDao.updateDate(item)

    suspend fun importDatesFromIcal(ical: ICalendar, courses: List<Course>) {
        // Mapowanie eventów do listy dat
        ical.events.forEach { event ->
            val dateTimeStart = event.dateStart.value
            val localDateTimeStart = LocalDateTime.ofInstant(dateTimeStart.toInstant(), ZoneId.systemDefault())
            val timeStart = localDateTimeStart.toLocalTime().toString()
            val dateStart = localDateTimeStart.toLocalDate().toString()


            val dateTimeEnd = event.dateEnd.value
            val localDateTimeEnd = LocalDateTime.ofInstant(dateTimeEnd.toInstant(), ZoneId.systemDefault())
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
                    attendanceStatus = AttendanceStatus.PRESENT
                )
                insertDate(date)
            }
        }
    }
    private fun getType(summary: Summary): String{
        return summary.value.split(" ").first()
    }

    private fun getName(summary: Summary): String{
        return summary.value.split(" ").drop(2).joinToString(" ")
    }
}