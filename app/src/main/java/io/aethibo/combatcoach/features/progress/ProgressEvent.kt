package io.aethibo.combatcoach.features.progress

import io.aethibo.combatcoachex.features.progress.presentation.model.LogEntryUi

sealed interface ProgressEvent {
    data class FilterSelected(val filter: ProgressFilter) : ProgressEvent
    data class EntryTapped(val entry: LogEntryUi) : ProgressEvent
    data class DeleteLog(val logId: Int) : ProgressEvent
    data object DismissDetail : ProgressEvent
}
