package io.aethibo.combatcoach.features.plandetail.model

sealed interface SessionType {
    data class Workout(val id: Int) : SessionType
    data class Combo(val id: Int) : SessionType
}
