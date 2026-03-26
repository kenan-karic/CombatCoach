package io.aethibo.combatcoach.shared.combo.domain.model

enum class AdvanceMode {
    /** Timer counts down, auto-advance to next combo */
    AUTO,
    /** User taps Done to advance */
    MANUAL,
    /** Both controls shown — user decides */
    BOTH;

    fun label(): String = when (this) {
        AUTO   -> "Auto"
        MANUAL -> "Manual"
        BOTH   -> "Auto + Manual"
    }
}
