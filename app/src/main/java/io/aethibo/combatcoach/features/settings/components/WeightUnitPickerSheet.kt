package io.aethibo.combatcoach.features.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.shared.user.domain.model.WeightUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightUnitPickerSheet(
    current: WeightUnit,
    onSelect: (WeightUnit) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sp = LocalSpacing.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = sp.screenPadding)
                .padding(bottom = sp.xxxl),
            verticalArrangement = Arrangement.spacedBy(sp.xs),
        ) {
            Text(
                text = stringResource(R.string.settings_label_weight_unit),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(sp.small))
            WeightUnit.entries.forEach { unit ->
                OptionItem(
                    label = unit.label(),
                    icon = Icons.Filled.FitnessCenter,
                    isSelected = unit == current,
                    onSelect = { onSelect(unit); onDismiss() },
                )
            }
        }
    }
}
