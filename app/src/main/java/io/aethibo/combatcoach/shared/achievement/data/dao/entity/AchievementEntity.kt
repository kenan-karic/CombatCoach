package io.aethibo.combatcoach.shared.achievement.data.dao.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "achievements", indices = [Index(value = ["key"], unique = true)])
data class AchievementEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val key: String,
    val title: String,
    val description: String,
    val iconRes: String,
    val category: String,
    val earnedAt: Long?,
    val progressCurrent: Int,
    val progressTarget: Int,
)
