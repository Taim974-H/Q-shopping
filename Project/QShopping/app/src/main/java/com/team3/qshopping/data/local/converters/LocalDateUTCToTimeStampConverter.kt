package com.team3.qshopping.data.local.converters

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

// from https://stackoverflow.com/a/58259703/14200676
class LocalDateUTCToTimeStampConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
        }
    }

    @TypeConverter
    fun toTimestamp(localDateTime: LocalDateTime?): Long? {
        return localDateTime?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli();
    }
}