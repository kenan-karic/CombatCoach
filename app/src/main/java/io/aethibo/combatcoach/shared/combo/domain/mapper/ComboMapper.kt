package io.aethibo.combatcoach.shared.combo.domain.mapper

import io.aethibo.combatcoach.shared.combo.data.dao.entity.ComboEntity
import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.utils.Discipline
import io.aethibo.combatcoach.shared.combo.domain.model.AdvanceMode
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

private fun String.toDisciplineModel(): Discipline =
    try { Discipline.valueOf(this) }
    catch (e: IllegalArgumentException) { Discipline.GENERAL }

fun ComboEntity.toDomain(): Combo = Combo(
    id = id,
    name = name,
    description = description,
    discipline = discipline.toDisciplineModel(),
    strikes = json.decodeFromString(strikesJson),
    durationSeconds = durationSeconds,
    rounds = rounds,
    restBetweenRoundsSeconds = restBetweenRoundsSeconds,
    advanceMode = AdvanceMode.valueOf(advanceMode),
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun Combo.toEntity(): ComboEntity = ComboEntity(
    id = id,
    name = name,
    description = description,
    discipline = discipline.name,
    strikesJson = json.encodeToString(strikes),
    durationSeconds = durationSeconds,
    rounds = rounds,
    restBetweenRoundsSeconds = restBetweenRoundsSeconds,
    advanceMode = advanceMode.name,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
