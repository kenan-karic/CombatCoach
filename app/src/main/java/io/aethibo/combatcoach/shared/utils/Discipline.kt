package io.aethibo.combatcoach.shared.utils

import androidx.compose.ui.graphics.Color
import io.aethibo.combatcoach.core.ui.theme.GrapplingBlue
import io.aethibo.combatcoach.core.ui.theme.MMAViolet
import io.aethibo.combatcoach.core.ui.theme.MintGreen
import io.aethibo.combatcoach.core.ui.theme.StrengthOrange
import io.aethibo.combatcoach.core.ui.theme.StrikingRed

interface TrainingDiscipline {
    fun label(): String
    fun accentColor(): Color
}

enum class Discipline : TrainingDiscipline {
    STRIKING, GRAPPLING, MMA, STRENGTH, GENERAL;

    override fun label(): String = when (this) {
        STRIKING -> "Striking"
        GRAPPLING -> "Grappling"
        MMA -> "MMA"
        STRENGTH -> "Strength"
        GENERAL -> "General"
    }

    override fun accentColor(): Color = when (this) {
        STRIKING -> StrikingRed
        GRAPPLING -> GrapplingBlue
        MMA -> MMAViolet
        STRENGTH -> StrengthOrange
        GENERAL -> MintGreen
    }
}

/**
 * Physical training discipline for [Workout].
 *
 * Kept separate from [Discipline] (which is martial-arts-specific and used
 * by [Combo]) so each screen only shows options that make sense for its context:
 *
 *  - Combo edit  → [Discipline]  (STRIKING, GRAPPLING, MMA)
 *  - Workout edit → [WorkoutDiscipline] (STRENGTH, CIRCUIT, CARDIO, …)
 */
enum class WorkoutDiscipline : TrainingDiscipline {
    STRENGTH,
    CIRCUIT,
    CARDIO,
    MMA_CONDITIONING,
    BOXING,
    WRESTLING;

    override fun label(): String = when (this) {
        STRENGTH -> "Strength"
        CIRCUIT -> "Circuit"
        CARDIO -> "Cardio"
        MMA_CONDITIONING -> "MMA Conditioning"
        BOXING -> "Boxing"
        WRESTLING -> "Wrestling"
    }

    override fun accentColor(): Color = when (this) {
        STRENGTH -> StrengthOrange
        CIRCUIT -> MintGreen
        CARDIO -> GrapplingBlue
        MMA_CONDITIONING -> MMAViolet
        BOXING -> StrikingRed
        WRESTLING -> GrapplingBlue
    }
}
