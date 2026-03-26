package io.aethibo.combatcoach.shared.combo.domain.model

import io.aethibo.combatcoach.shared.utils.Discipline

data class Combo(
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val discipline: Discipline = Discipline.STRIKING,
    val strikes: List<String> = emptyList(),
    val durationSeconds: Int = 180,
    val rounds: Int = 3,
    val restBetweenRoundsSeconds: Int = 60,
    val advanceMode: AdvanceMode = AdvanceMode.BOTH,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
) {
    val isNew: Boolean get() = id == 0

    val totalDurationSeconds: Int
        get() = (durationSeconds * rounds) + (restBetweenRoundsSeconds * (rounds - 1).coerceAtLeast(
            0
        ))

    val strikesSummary: String
        get() = strikes.joinToString(" → ")

    fun withUpdatedAt(): Combo = copy(updatedAt = System.currentTimeMillis())
}
