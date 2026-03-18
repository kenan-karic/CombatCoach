package io.aethibo.combatcoach.core.ui.popup

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.theme.Charcoal
import io.aethibo.combatcoach.core.ui.theme.CoralPink
import io.aethibo.combatcoach.core.ui.theme.SurfaceWhite

@Composable
fun defaultPopupContent(
    @StringRes titleResId: Int,
    @StringRes descriptionResId: Int
): @Composable (ColumnScope.() -> Unit) = {
    val titleAccessibility = stringResource(id = titleResId).replace("®", "")
    val descriptionAccessibility = stringResource(id = descriptionResId).replace("®", "")
    val description = stringResource(id = descriptionResId)

    Text(
        text = stringResource(id = titleResId),
        textAlign = TextAlign.Center,
        color = Charcoal,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.semantics {
            contentDescription = titleAccessibility
        }
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = description,
        textAlign = TextAlign.Center,
        color = Charcoal,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier.semantics {
            contentDescription = descriptionAccessibility
        }
    )
}

@Composable
fun defaultPopupButtons(
    @StringRes confirmResId: Int,
    confirmButtonAction: () -> Unit,
    @StringRes cancelResId: Int? = null,
    cancelButtonAction: () -> Unit = {}
): @Composable (RowScope.() -> Unit) = {
    cancelResId?.let { resId ->
        val cancelLabel = stringResource(id = resId)
        TextButton(onClick = cancelButtonAction) {
            Text(
                text = cancelLabel,
                style = MaterialTheme.typography.labelMedium,
                color = CoralPink,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
    }

    Button(
        onClick = { confirmButtonAction() },
        modifier = cancelResId?.let { Modifier }
            ?: Modifier.fillMaxWidth(PopupDefaults.ButtonToBodyRatio),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = Charcoal,
            contentColor = SurfaceWhite,
        )
    ) {
        Text(
            text = stringResource(id = confirmResId),
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
    }
}
