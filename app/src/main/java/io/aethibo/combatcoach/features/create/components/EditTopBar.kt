package io.aethibo.combatcoach.features.create.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTopBar(
    title: String,
    onBack: () -> Unit,
    onDelete: (() -> Unit)? = null,
    onStart: (() -> Unit)? = null,
    startLabel: String = "Start",
) {
    TopAppBar(
        title = { Text(title, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, "Back")
            }
        },
        actions = {
            if (onStart != null) {
                TextButton(onClick = onStart) {
                    Icon(Icons.Filled.PlayArrow, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(startLabel)
                }
            }
            if (onDelete != null) {
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Filled.DeleteOutline,
                        "Delete",
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Top Bar Variations", showBackground = true)
@Composable
private fun EditTopBarPreview() {
    CombatCoachTheme {
        Column {
            // 1. Standard Edit Mode (Title + Back + Delete)
            EditTopBar(
                title = "Edit Workout",
                onBack = {},
                onDelete = {}
            )

            Spacer(Modifier.height(20.dp))

            // 2. Playable Mode (Title + Back + Start Action)
            EditTopBar(
                title = "Heavy Bag Pro",
                onBack = {},
                onStart = {},
                startLabel = "Begin"
            )

            Spacer(Modifier.height(20.dp))

            // 3. Full Mode (Title + Back + Start + Delete)
            EditTopBar(
                title = "Muay Thai Combo",
                onBack = {},
                onStart = {},
                onDelete = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Top Bar Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EditTopBarDarkPreview() {
    CombatCoachTheme {
        EditTopBar(
            title = "Circuit Training",
            onBack = {},
            onDelete = {}
        )
    }
}
