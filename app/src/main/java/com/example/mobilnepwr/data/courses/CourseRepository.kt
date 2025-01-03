package com.example.mobilnepwr.data.courses

import biweekly.ICalendar
import biweekly.property.Summary
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class CourseRepository(private val courseDao: CourseDao) {
    fun getAllItemsStream(): Flow<List<Course>> = courseDao.getAllClasses()

    fun getItemStream(id: Int): Flow<Course> = courseDao.getClassById(id)

    suspend fun insertItem(item: Course) = courseDao.insertClass(item)

    suspend fun deleteItem(item: Course) = courseDao.delete(item)

    suspend fun updateItem(item: Course) = courseDao.update(item)

    suspend fun clearDatabase() = courseDao.clearDatabase()

    fun getClassesAtDate(date: LocalDate): Flow<List<Course>> = courseDao.getClassesAtDate(date)

    suspend fun importCoursesFromIcal(ical: ICalendar) {
        courseDao.clearDatabase()
        val events = ical.events.map { event ->
            Course(
                type = getType(event.summary),
                name = getName(event.summary),
                address = event.location?.value ?: "",
                building = event.description.value.split("\n")[1],
                hall = event.description.value.split("\n")[0].split(" ").drop(1).joinToString(" ")
            )
        }
        courseDao.insertAll(events)
    }

    private fun getType(summary: Summary): String {
        return summary.value.split(" ").first()
    }

    private fun getName(summary: Summary): String {
        return summary.value.split(" ").drop(2).joinToString(" ")
    }
}