package io.aethibo.combatcoach.features.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.CoralPink
import io.aethibo.combatcoach.core.ui.theme.DevicesPreview
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.core.ui.theme.Periwinkle
import io.aethibo.combatcoach.features.onboarding.components.OnboardingButton
import io.aethibo.combatcoach.features.onboarding.components.OnboardingPageContent
import io.aethibo.combatcoach.features.onboarding.components.PageIndicator
import io.aethibo.combatcoach.features.onboarding.model.IllustrationKey
import io.aethibo.combatcoach.features.onboarding.model.OnboardingPage
import kotlinx.coroutines.launch
import kotlin.collections.getOrNull
import kotlin.collections.lastIndex

@Composable
fun OnboardingScreen(
    state: OnboardingState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val spacing = LocalSpacing.current

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { state.pages.size },
    )
    val currentPage = pagerState.currentPage
    val isLastPage = currentPage == state.pages.lastIndex
    val currentAccentColor = state.pages.getOrNull(currentPage)?.accentColor
        ?: Color.Unspecified

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // ── Pager ──────────────────────────────────────────────────────────
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) { pageIndex ->
            OnboardingPageContent(
                page = state.pages[pageIndex],
                pagerState = pagerState,
                pageIndex = pageIndex,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            )
        }

        // ── Bottom controls ────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.screenPadding)
                .padding(top = spacing.medium, bottom = spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.small),
        ) {
            PageIndicator(
                pageCount = state.pages.size,
                currentPage = currentPage,
                activeColor = currentAccentColor,
            )

            Spacer(Modifier.height(spacing.xs))

            OnboardingButton(
                isLastPage = isLastPage,
                color = currentAccentColor,
                onClick = {
                    if (isLastPage) {
                        state.eventSink(OnboardingEvent.Finish)
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(currentPage + 1)
                        }
                    }
                },
            )

            AnimatedVisibility(visible = !isLastPage) {
                TextButton(onClick = { state.eventSink(OnboardingEvent.Skip) }) {
                    Text(
                        text = stringResource(R.string.skip),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@DevicesPreview
@Composable
private fun OnboardingScreenPreview() {
    CombatCoachTheme {
        val state = OnboardingState(
            pages = listOf(
                OnboardingPage(
                    titleRes = R.string.onboarding_welcome_title,
                    subtitleRes = R.string.onboarding_welcome_subtitle,
                    descriptionRes = R.string.onboarding_welcome_description,
                    illustrationKey = IllustrationKey.Welcome,
                    accentColor = Periwinkle,
                ),
                OnboardingPage(
                    titleRes = R.string.onboarding_workouts_title,
                    subtitleRes = R.string.onboarding_workouts_subtitle,
                    descriptionRes = R.string.onboarding_workouts_description,
                    illustrationKey = IllustrationKey.Workouts,
                    accentColor = CoralPink,
                )
            ),
            eventSink = {
                // no-op
            }
        )

        OnboardingScreen(state = state)
    }
}
