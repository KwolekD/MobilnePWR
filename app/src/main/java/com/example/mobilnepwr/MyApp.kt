package com.example.mobilnepwr

import android.app.Application
import androidx.room.Room
import com.example.mobilnepwr.database.AppDatabase

class MyApp: Application() {
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-database"
        ).build()
    }

}