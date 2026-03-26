package io.aethibo.combatcoach.core.database

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter fun fromStringList(value: List<String>): String =
        json.encodeToString(value)

    @TypeConverter fun toStringList(value: String): List<String> =
        json.decodeFromString(value)
}
