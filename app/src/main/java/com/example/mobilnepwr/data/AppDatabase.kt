package com.example.mobilnepwr.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mobilnepwr.data.courses.Course
import com.example.mobilnepwr.data.courses.CourseDao
import com.example.mobilnepwr.data.dates.Date
import com.example.mobilnepwr.data.dates.DateDao
import com.example.mobilnepwr.data.deadlines.Deadline
import com.example.mobilnepwr.data.deadlines.DeadlineDao
import com.example.mobilnepwr.data.images.Image
import com.example.mobilnepwr.data.images.ImageDao
import com.example.mobilnepwr.data.notes.Note
import com.example.mobilnepwr.data.notes.NoteDao

/**
 * Database class with a singleton Instance object.
 */
@Database(
    entities = [Course::class,Date::class, Image::class,
    Note::class, Deadline::class],
    version = 1,
    exportSchema = false)
@TypeConverters(
    Converters::class
)
abstract class AppDatabase: RoomDatabase(){

    abstract fun courseDao(): CourseDao
    abstract fun dateDao(): DateDao
    abstract fun imageDao(): ImageDao
    abstract fun noteDao(): NoteDao
    abstract fun deadlineDao(): DeadlineDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        // if the Instance is not null, return it, otherwise create a new database instance.
        fun getDatabase(context: Context): AppDatabase {
            return Instance?: synchronized(this ){
                Room.databaseBuilder(context,AppDatabase::class.java,"database")
                    .build().also { Instance= it }
            }
        }
    }
}