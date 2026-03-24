package io.aethibo.combatcoach.shared.achievement.domain.mapper

import io.aethibo.combatcoach.shared.achievement.data.dao.entity.AchievementEntity
import io.aethibo.combatcoach.shared.achievement.domain.model.Achievement
import io.aethibo.combatcoach.shared.achievement.domain.model.AchievementCategory

fun AchievementEntity.toDomain(): Achievement = Achievement(
    id = id,
    key = key,
    title = title,
    description = description,
    iconRes = iconRes,
    category = AchievementCategory.valueOf(category),
    earnedAt = earnedAt,
    progressCurrent = progressCurrent,
    progressTarget = progressTarget,
)

fun Achievement.toEntity(): AchievementEntity = AchievementEntity(
    id = id,
    key = key,
    title = title,
    description = description,
    iconRes = iconRes,
    category = category.name,
    earnedAt = earnedAt,
    progressCurrent = progressCurrent,
    progressTarget = progressTarget,
)
