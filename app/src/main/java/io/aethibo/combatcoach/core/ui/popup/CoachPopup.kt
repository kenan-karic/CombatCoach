package io.aethibo.combatcoach.core.ui.popup

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.Charcoal
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme

/**
 * Coach modular popup.
 * @param content populates main area of the popup. It is slotted in a [Column] layout
 * for the sake of avoiding too much abstraction.
 * See [defaultPopupContent].
 * @param buttons populates the button area of the popup. It is slotted in a [Row] layout
 * for the sake of avoiding too much abstraction.
 * See [defaultPopupButtons].
 * @param popupType contains designated popup color and icon. See [PopupTypes].
 * @param onDismissRequest action to be executed whenever user tries to dismiss the popup.
 * @param modifier modifier to be applied to the popup surface.
 * @param background background color of the popup surface.
 */
@Composable
fun CoachPopup(
    content: @Composable ColumnScope.() -> Unit,
    buttons: @Composable RowScope.() -> Unit,
    popupType: PopupType,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.surface,
) {
    val closeButtonAccessibility = stringResource(R.string.close_button_accessibility)

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier,
            shape = PopupDefaults.DefaultPopupShape,
            color = background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PopupDefaults.DefaultPopupPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = null,
                        modifier = Modifier
                            .sizeIn(PopupDefaults.DefaultDismissIconSize)
                            .clickable {
                                onDismissRequest()
                            }
                            .semantics {
                                contentDescription = closeButtonAccessibility
                                traversalIndex = 1F
                            }
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .size(PopupDefaults.IconCircleBackgroundSize)
                        .clip(CircleShape)
                        .background(popupType.background),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = popupType.resId),
                        contentDescription = null,
                        modifier = Modifier.size(PopupDefaults.MaxIconSize),
                        tint = Color.White
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = content
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    content = buttons
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun GenericPopupPreview() {
    CombatCoachTheme {
        CoachPopup(
            content = defaultPopupContent(
                titleResId = R.string.generic_popup_title,
                descriptionResId = R.string.generic_popup_description
            ),
            buttons = defaultPopupButtons(
                confirmResId = R.string.action_ok,
                confirmButtonAction = { },
                cancelResId = R.string.action_cancel,
                cancelButtonAction = { }
            ),
            popupType = PopupTypes.Generic,
            onDismissRequest = { }
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun GenericPopupDarkPreview() {
    CombatCoachTheme {
        CoachPopup(
            content = defaultPopupContent(
                titleResId = R.string.generic_popup_title,
                descriptionResId = R.string.generic_popup_description
            ),
            buttons = defaultPopupButtons(
                confirmResId = R.string.action_ok,
                confirmButtonAction = { },
                cancelResId = R.string.action_cancel,
                cancelButtonAction = { }
            ),
            popupType = PopupTypes.Generic,
            onDismissRequest = { }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun InfoPopupPreview() {
    CombatCoachTheme {
        CoachPopup(
            content = defaultPopupContent(
                titleResId = R.string.info_popup_title,
                descriptionResId = R.string.info_popup_description
            ),
            buttons = defaultPopupButtons(
                confirmResId = R.string.action_got_it, // "Got it"
                confirmButtonAction = { }
            ),
            popupType = PopupTypes.Info,
            onDismissRequest = { }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun SuccessPopupPreview() {
    CombatCoachTheme {
        CoachPopup(
            content = defaultPopupContent(
                titleResId = R.string.success_popup_title,
                descriptionResId = R.string.success_popup_description
            ),
            buttons = defaultPopupButtons(
                confirmResId = R.string.action_continue,
                confirmButtonAction = { }
            ),
            popupType = PopupTypes.Success,
            onDismissRequest = { }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun WarningPopupPreview() {
    CombatCoachTheme {
        CoachPopup(
            content = defaultPopupContent(
                titleResId = R.string.warning_popup_title,
                descriptionResId = R.string.warning_popup_description
            ),
            buttons = defaultPopupButtons(
                confirmResId = R.string.action_proceed,
                confirmButtonAction = { },
                cancelResId = R.string.action_cancel,
                cancelButtonAction = { }
            ),
            popupType = PopupTypes.Warning,
            onDismissRequest = { }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ErrorPopupPreview() {
    CombatCoachTheme {
        CoachPopup(
            content = defaultPopupContent(
                titleResId = R.string.error_popup_title,
                descriptionResId = R.string.error_popup_description
            ),
            buttons = defaultPopupButtons(
                confirmResId = R.string.action_retry,
                confirmButtonAction = { },
                cancelResId = R.string.action_dismiss,
                cancelButtonAction = { }
            ),
            popupType = PopupTypes.Error,
            onDismissRequest = { }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun CustomPopupPreview() {
    CombatCoachTheme {
        CoachPopup(
            content = {
                Text(
                    text = "Custom Content",
                    textAlign = TextAlign.Center,
                    color = Charcoal,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This popup demonstrates custom content with multiple text elements and custom styling.",
                    textAlign = TextAlign.Center,
                    color = Charcoal,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.1f))
                ) {
                    Text(
                        text = "Additional details can be displayed in cards or other components.",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = Charcoal
                    )
                }
            },
            buttons = {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Secondary",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Primary",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            },
            popupType = PopupTypes.Info,
            onDismissRequest = { }
        )
    }
}

// Single button examples
@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun SingleButtonPopupPreview() {
    CombatCoachTheme {
        CoachPopup(
            content = defaultPopupContent(
                titleResId = R.string.single_button_title,
                descriptionResId = R.string.single_button_description
            ),
            buttons = defaultPopupButtons(
                confirmResId = R.string.action_acknowledge,
                confirmButtonAction = { }
                // No cancel button - will use full width
            ),
            popupType = PopupTypes.Info,
            onDismissRequest = { }
        )
    }
}
