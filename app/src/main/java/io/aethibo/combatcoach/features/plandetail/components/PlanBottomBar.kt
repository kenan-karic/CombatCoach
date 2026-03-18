package io.aethibo.combatcoach.features.plandetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.plandetail.PlanDetailEvent
import io.aethibo.combatcoach.features.plandetail.PlanDetailState
import io.aethibo.combatcoach.shared.plan.domain.model.Plan
import io.aethibo.combatcoach.shared.plan.domain.model.PlanDay
import io.aethibo.combatcoach.shared.plan.domain.model.PlanType

@Composable
internal fun PlanBottomBar(state: PlanDetailState, modifier: Modifier = Modifier) {
    val sp = LocalSpacing.current
    val plan = state.plan ?: return

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = sp.screenPadding, vertical = sp.medium),
            verticalArrangement = Arrangement.spacedBy(sp.xs),
        ) {
            when {
                state.isActivePlan && state.currentDay != null -> {
                    val day = state.currentDay!!
                    Button(
                        onClick = { state.eventSink(PlanDetailEvent.OpenStartSheet(state.currentDayIndex)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(24.dp),
                        enabled = !day.isRestDay,
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                    ) {
                        Icon(
                            if (day.isRestDay) Icons.Outlined.Hotel else Icons.Filled.PlayArrow,
                            null, Modifier.size(20.dp),
                        )
                        Spacer(Modifier.width(sp.xs))
                        Text(
                            text = stringResource(
                                if (day.isRestDay) R.string.plan_detail_rest_day_label
                                else R.string.plan_detail_start_session
                            ),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }

                    OutlinedButton(
                        onClick = { state.eventSink(PlanDetailEvent.AdvanceDay) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    ) {
                        Icon(
                            if (day.isRestDay) Icons.Filled.SkipNext else Icons.Filled.Check,
                            null, Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(sp.xs))
                        Text(
                            text = stringResource(
                                if (day.isRestDay) R.string.plan_detail_advance_next_day
                                else R.string.plan_detail_mark_done_advance
                            )
                        )
                    }

                    TextButton(
                        onClick = { state.eventSink(PlanDetailEvent.DeactivatePlan) },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    ) {
                        Text(
                            text = stringResource(R.string.plan_detail_stop_plan),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                else -> {
                    Button(
                        onClick = { state.eventSink(PlanDetailEvent.SetAsActive) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(24.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                    ) {
                        Icon(Icons.Filled.PlayCircle, null, Modifier.size(20.dp))
                        Spacer(Modifier.width(sp.xs))
                        Text(
                            text = stringResource(R.string.plan_detail_follow_plan),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    if (plan.planType == PlanType.PROGRAM && plan.days.any { !it.isRestDay }) {
                        OutlinedButton(
                            onClick = { state.eventSink(PlanDetailEvent.OpenStartSheet(0)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        ) {
                            Icon(Icons.Filled.Visibility, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(sp.xs))
                            Text(text = stringResource(R.string.plan_detail_preview_day_one))
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Plan Bottom Bar - Inactive/Preview", showBackground = true)
@Composable
private fun PlanBottomBarInactivePreview() {
    CombatCoachTheme {
        PlanBottomBar(
            state = PlanDetailState(
                plan = Plan(
                    name = "8-Week Boxing Program",
                    planType = PlanType.PROGRAM,
                    days = listOf(PlanDay(dayNumber = 1, isRestDay = false))
                ),
                isActivePlan = false,
                eventSink = {}
            )
        )
    }
}

@Preview(name = "Plan Bottom Bar - Active Session", showBackground = true)
@Composable
private fun PlanBottomBarActivePreview() {
    CombatCoachTheme {
        PlanBottomBar(
            state = PlanDetailState(
                plan = Plan(name = "8-Week Boxing Program"),
                isActivePlan = true,
                eventSink = {
                    // no-op
                }
            )
        )
    }
}
