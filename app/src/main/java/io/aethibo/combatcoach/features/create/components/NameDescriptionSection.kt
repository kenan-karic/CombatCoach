package io.aethibo.combatcoach.features.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.components.ccTextFieldColors
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing

@Composable
fun NameDescriptionSection(
    name: String,
    description: String,
    nameError: String?,
    onNameChange: (String) -> Unit,
    onDescChange: (String) -> Unit,
) {
    val sp = LocalSpacing.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = sp.screenPadding),
        verticalArrangement = Arrangement.spacedBy(sp.small),
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text(stringResource(R.string.create_label_name)) },
            isError = nameError != null,
            supportingText = nameError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = ccTextFieldColors(),
        )
        OutlinedTextField(
            value = description,
            onValueChange = onDescChange,
            label = { Text(stringResource(R.string.create_label_description)) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 80.dp),
            shape = RoundedCornerShape(12.dp),
            maxLines = 4,
            colors = ccTextFieldColors(),
        )
    }
}

@Preview(name = "Name/Desc States - Light", showBackground = true)
@Composable
private fun NameDescriptionSectionPreview() {
    CombatCoachTheme {
        Column {
            // Case 1: Filled correctly
            NameDescriptionSection(
                name = "Boxing Power Hour",
                description = "Focusing on heavy bag drills and explosive footwork. Includes a 10-minute warm-up.",
                nameError = null,
                onNameChange = {},
                onDescChange = {}
            )

            Spacer(Modifier.height(24.dp))

            // Case 2: Validation Error
            NameDescriptionSection(
                name = "",
                description = "",
                nameError = "Name cannot be empty",
                onNameChange = {},
                onDescChange = {}
            )
        }
    }
}
