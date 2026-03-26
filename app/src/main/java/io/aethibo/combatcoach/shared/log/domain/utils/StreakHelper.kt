package io.aethibo.combatcoach.shared.log.domain.utils

import io.aethibo.combatcoach.shared.log.domain.model.WorkoutLog
import java.util.Calendar

fun computeStreak(logs: List<WorkoutLog>): Int {
    if (logs.isEmpty()) return 0
    val cal = Calendar.getInstance()
    val daySet = logs.map { log ->
        cal.timeInMillis = log.completedAt
        Triple(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }.toSet()

    var streak = 0
    cal.timeInMillis = System.currentTimeMillis()
    while (true) {
        val key = Triple(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        if (key !in daySet) break
        streak++
        cal.add(Calendar.DAY_OF_MONTH, -1)
    }
    return streak
}
