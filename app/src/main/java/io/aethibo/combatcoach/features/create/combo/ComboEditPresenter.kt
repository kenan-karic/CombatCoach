package io.aethibo.combatcoach.features.create.combo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import io.aethibo.combatcoach.features.create.utils.CreateEditMode
import io.aethibo.combatcoach.shared.combo.domain.model.AdvanceMode
import io.aethibo.combatcoach.shared.combo.domain.model.Combo
import io.aethibo.combatcoach.shared.combo.domain.usecase.DeleteComboUseCase
import io.aethibo.combatcoach.shared.combo.domain.usecase.ObserveComboByIdUseCase
import io.aethibo.combatcoach.shared.combo.domain.usecase.SaveComboUseCase
import io.aethibo.combatcoach.shared.utils.Discipline
import io.aethibo.combatcoach.shared.utils.ItemType
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun comboEditPresenter(
    mode: CreateEditMode,
    observeComboById: ObserveComboByIdUseCase,
    saveCombo: SaveComboUseCase,
    deleteCombo: DeleteComboUseCase,
    onSaved: () -> Unit,
    onDeleted: () -> Unit,
    onStartTimer: ((ItemType, Int) -> Unit)? = null,
): ComboEditState {

    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var discipline by remember { mutableStateOf(Discipline.STRIKING) }
    var strikes by remember { mutableStateOf<List<String>>(emptyList()) }
    var durationSeconds by remember { mutableIntStateOf(180) }
    var rounds by remember { mutableIntStateOf(3) }
    var restSeconds by remember { mutableIntStateOf(60) }
    var advanceMode by remember { mutableStateOf(AdvanceMode.BOTH) }
    var isLoading by remember { mutableStateOf(mode is CreateEditMode.Edit) }
    var isSaving by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (mode is CreateEditMode.Edit) {
        LaunchedEffect(mode.id) {
            val combo = observeComboById(mode.id).filterNotNull().first()
            name = combo.name
            description = combo.description
            discipline = combo.discipline
            strikes = combo.strikes
            durationSeconds = combo.durationSeconds
            rounds = combo.rounds
            restSeconds = combo.restBetweenRoundsSeconds
            advanceMode = combo.advanceMode
            isLoading = false
        }
    }

    fun validate(): Boolean {
        nameError = if (name.isBlank()) "Name is required" else null
        return nameError == null
    }

    val eventSink: (ComboEditEvent) -> Unit = remember {
        { event ->
            when (event) {
                is ComboEditEvent.NameChanged -> {
                    name = event.value; nameError = null
                }

                is ComboEditEvent.DescriptionChanged -> description = event.value
                is ComboEditEvent.DisciplineChanged -> discipline = event.value
                is ComboEditEvent.AddStrike -> strikes = strikes + event.strike
                is ComboEditEvent.RemoveStrike -> {
                    strikes = strikes.toMutableList().also { it.removeAt(event.index) }
                }

                is ComboEditEvent.ReorderStrikes -> {
                    strikes = strikes.toMutableList()
                        .apply { add(event.to, removeAt(event.from)) }
                }

                is ComboEditEvent.DurationChanged -> durationSeconds = event.seconds
                is ComboEditEvent.RoundsChanged -> rounds = event.rounds.coerceAtLeast(1)
                is ComboEditEvent.RestChanged -> restSeconds = event.seconds
                is ComboEditEvent.AdvanceModeChanged -> advanceMode = event.mode

                ComboEditEvent.Save -> {
                    if (!validate()) return@remember
                    scope.launch {
                        isSaving = true
                        errorMessage = null

                        val combo = Combo(
                            id = if (mode is CreateEditMode.Edit) mode.id else 0,
                            name = name.trim(),
                            description = description.trim(),
                            discipline = discipline,
                            strikes = strikes,
                            durationSeconds = durationSeconds,
                            rounds = rounds,
                            restBetweenRoundsSeconds = restSeconds,
                            advanceMode = advanceMode,
                        )

                        saveCombo(combo).fold(
                            ifLeft = { failure ->
                                isSaving = false; errorMessage = failure.toUserMessage()
                            },
                            ifRight = { isSaving = false; onSaved() }
                        )
                    }
                }

                ComboEditEvent.Delete -> showDeleteDialog = true
                ComboEditEvent.DismissDelete -> showDeleteDialog = false
                ComboEditEvent.ConfirmDelete -> {
                    scope.launch {
                        if (mode is CreateEditMode.Edit) {
                            deleteCombo(mode.id).fold(
                                ifLeft = { failure -> errorMessage = failure.toUserMessage() },
                                ifRight = { onDeleted() }
                            )
                        }
                    }
                }

                ComboEditEvent.StartTimer -> {
                    if (mode is CreateEditMode.Edit) {
                        onStartTimer?.invoke(ItemType.Combo, mode.id)
                    }
                }
            }
        }
    }

    return ComboEditState(
        mode = mode,
        name = name,
        description = description,
        discipline = discipline,
        strikes = strikes,
        durationSeconds = durationSeconds,
        rounds = rounds,
        restBetweenRoundsSeconds = restSeconds,
        advanceMode = advanceMode,
        isLoading = isLoading,
        isSaving = isSaving,
        nameError = nameError,
        errorMessage = errorMessage,
        showDeleteDialog = showDeleteDialog,
        eventSink = eventSink,
    )
}
