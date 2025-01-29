package com.example.mobilnepwr.data

import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.data.courses.CourseWithDateDetails
import com.example.mobilnepwr.data.dates.Date
import com.example.mobilnepwr.data.deadlines.Deadline
import com.example.mobilnepwr.data.images.Image
import com.example.mobilnepwr.data.images.ImageDetails
import com.example.mobilnepwr.data.images.toImage
import com.example.mobilnepwr.data.images.toImageDetails
import com.example.mobilnepwr.data.notes.Note
import com.example.mobilnepwr.ui.course_deatails.DateDetails
import com.example.mobilnepwr.ui.course_deatails.DeadlineDetails
import com.example.mobilnepwr.ui.course_deatails.NoteDetails
import com.example.mobilnepwr.ui.course_deatails.deadline.toDeadline
import com.example.mobilnepwr.ui.course_deatails.note.toNote
import com.example.mobilnepwr.ui.course_deatails.toCourseDetails
import com.example.mobilnepwr.ui.course_deatails.toDate
import com.example.mobilnepwr.ui.course_deatails.toDateDetails
import com.example.mobilnepwr.ui.course_deatails.toDeadlineDetails
import com.example.mobilnepwr.ui.course_deatails.toNoteDetails
import com.example.mobilnepwr.ui.home.toDate
import org.junit.Test
import java.time.LocalDate

class DataDetailsConvertsTests {

    @Test
    fun `courseToDetails should convert Course to CourseDetails`() {
        val course = Course(
            courseId = 1,
            name = "Math",
            type = "Lecture",
            address = "Room 101",
            building = "Building A",
            hall = "Hall 1"
        )

        val details = course.toCourseDetails()

        assert(details.courseId == course.courseId)
        assert(details.name == course.name)
        assert(details.type == course.type)
        assert(details.address == course.address)
        assert(details.building == course.building)
        assert(details.hall == course.hall)
    }

    @Test
    fun `courseWithDateDetailsToDate should convert CourseWithDateDetails to Date`() {
        val courseWithDateDetails = CourseWithDateDetails(
            courseId = 1,
            name = "Math",
            type = "Lecture",
            dateId = 1,
            date = LocalDate.of(2023, 1, 1),
            attendance = false,
            startTime = "10:00",
            endTime = "12:00"
        )
        val date = courseWithDateDetails.toDate()
        assert(date.courseId == courseWithDateDetails.courseId)
        assert(date.dateId == courseWithDateDetails.dateId)
        assert(date.date == courseWithDateDetails.date)
        assert(date.attendance == courseWithDateDetails.attendance)
        assert(date.startTime == courseWithDateDetails.startTime)
        assert(date.endTime == courseWithDateDetails.endTime)
    }

    @Test
    fun `deadlineDetailsToDeadline should convert DeadlineDetails to Deadline`() {
        val deadlineDetails = DeadlineDetails(
            deadlineId = 1,
            courseId = 1,
            title = "Deadline 1",
            description = "Description 1",
            date = LocalDate.of(2023, 1, 1)
        )

        val deadline = deadlineDetails.toDeadline()

        assert(deadline.deadlineId == deadlineDetails.deadlineId)
        assert(deadline.courseId == deadlineDetails.courseId)
        assert(deadline.title == deadlineDetails.title)
        assert(deadline.description == deadlineDetails.description)
        assert(deadline.date == deadlineDetails.date)
    }

    @Test
    fun `deadlineToDeadlineDetails should convert Deadline to DeadlineDetails`() {
        val deadline = Deadline(
            deadlineId = 1,
            courseId = 1,
            title = "Deadline 1",
            description = "Description 1",
            date = LocalDate.of(2023, 1, 1)
        )

        val deadlineDetails = deadline.toDeadlineDetails()

        assert(deadlineDetails.deadlineId == deadline.deadlineId)
        assert(deadlineDetails.courseId == deadline.courseId)
        assert(deadlineDetails.title == deadline.title)
        assert(deadlineDetails.description == deadline.description)
        assert(deadlineDetails.date == deadline.date)

    }

    @Test
    fun `noteToNoteDetails should convert Note to NoteDetails`() {
        val note = Note(
            noteId = 1,
            courseId = 1,
            title = "Note 1",
            content = "Content 1",
            date = LocalDate.of(2023, 1, 1)
        )

        val noteDetails = note.toNoteDetails()

        assert(noteDetails.noteId == note.noteId)
        assert(noteDetails.courseId == note.courseId)
        assert(noteDetails.title == note.title)
        assert(noteDetails.content == note.content)
        assert(noteDetails.date == note.date)

    }

    @Test
    fun `noteDetailsToNote should convert NoteDetails to Note`() {
        val noteDetails = NoteDetails(
            noteId = 1,
            courseId = 1,
            title = "Note 1",
            content = "Content 1",
            date = LocalDate.of(2023, 1, 1)
        )

        val note = noteDetails.toNote()

        assert(note.noteId == noteDetails.noteId)
        assert(note.courseId == noteDetails.courseId)
        assert(note.title == noteDetails.title)
        assert(note.content == noteDetails.content)
        assert(note.date == noteDetails.date)
    }

    @Test
    fun `dateToDateDetails should convert Date to DateDetails`() {
        val date = Date(
            dateId = 1,
            courseId = 1,
            date = LocalDate.of(2023, 1, 1),
            attendance = false,
            startTime = "10:00",
            endTime = "12:00"
        )
        val dateDetails = date.toDateDetails()
        assert(dateDetails.date == date.date)
        assert(dateDetails.attendance == date.attendance)
        assert(dateDetails.courseId == date.courseId)
        assert(dateDetails.startTime == date.startTime)
        assert(dateDetails.endTime == date.endTime)
        assert(dateDetails.dateId == date.dateId)
    }

    @Test
    fun `dateDetailsToDate should convert DateDetails to Date`() {
        val dateDetails = DateDetails(
            dateId = 1,
            courseId = 1,
            date = LocalDate.of(2023, 1, 1),
            attendance = false,
            startTime = "10:00",
            endTime = "12:00"
        )
        val date = dateDetails.toDate()
        assert(date.dateId == dateDetails.dateId)
        assert(date.courseId == dateDetails.courseId)
        assert(date.date == dateDetails.date)
        assert(date.attendance == dateDetails.attendance)
        assert(date.startTime == dateDetails.startTime)
        assert(date.endTime == dateDetails.endTime)
    }

    @Test
    fun `imageToImageDetails should convert Image to ImageDetails`() {
        val image = Image(
            imageId = 1,
            courseId = 1,
            filePath = "path/to/image.jpg"
        )
        val imageDetails = image.toImageDetails()
        assert(imageDetails.imageId == image.imageId)
        assert(imageDetails.courseId == image.courseId)
        assert(imageDetails.filePath == image.filePath)

    }

    @Test
    fun `imageDetailsToImage should convert ImageDetails to Image`() {
        val imageDetails = ImageDetails(
            imageId = 1,
            courseId = 1,
            filePath = "path/to/image.jpg"
        )
        val image = imageDetails.toImage()
        assert(image.imageId == imageDetails.imageId)
        assert(image.courseId == imageDetails.courseId)
        assert(image.filePath == imageDetails.filePath)

    }

}