package io.aethibo.combatcoach.shared.combo.data.dao.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "combos")
data class ComboEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val discipline: String,
    val strikesJson: String,
    val durationSeconds: Int,
    val rounds: Int,
    val restBetweenRoundsSeconds: Int,
    val advanceMode: String,
    val createdAt: Long,
    val updatedAt: Long,
)
