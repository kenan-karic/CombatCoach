package io.aethibo.combatcoach.shared.user.domain.model

enum class WeightUnit {
    KG, LBS;

    fun label(): String = when (this) {
        KG  -> "Kilograms (kg)"
        LBS -> "Pounds (lbs)"
    }

    fun convert(value: Float, to: WeightUnit): Float = when {
        this == to   -> value
        to == LBS    -> value * 2.20462f
        else         -> value / 2.20462f
    }
}