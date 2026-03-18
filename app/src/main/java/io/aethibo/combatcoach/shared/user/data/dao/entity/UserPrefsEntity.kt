package io.aethibo.combatcoach.shared.user.data.dao.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_prefs")
data class UserPrefsEntity(
    @PrimaryKey val key: String,
    val value: String,
)
