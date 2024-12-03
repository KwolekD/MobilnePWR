package com.example.mobilnepwr.data

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun toLocalDate(value: Long?): LocalDate?{
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate?): Long?{
        return localDate?.toEpochDay()
    }
}