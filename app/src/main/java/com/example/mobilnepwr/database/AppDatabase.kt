package com.example.mobilnepwr.database

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ClassEntity::class,ClassDateEntity::class,ImageEntity::class,
    NoteEntity::class,DeadlineEntity::class],
    version = 1)
abstract class AppDatabase: RoomDatabase(){
    abstract fun classDao(): ClassDao
    abstract fun classDateDao(): ClassDateDao
    abstract fun imageDao(): ImageDao
    abstract fun noteDao(): NoteDao
    abstract fun deadlineDao(): DeadlineDao

}