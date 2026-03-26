package io.aethibo.combatcoach.features.onboarding.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.core.ui.theme.Periwinkle
import io.aethibo.combatcoach.features.onboarding.model.IllustrationKey
import io.aethibo.combatcoach.features.onboarding.model.OnboardingPage
import kotlin.math.absoluteValue

@Composable
internal fun OnboardingPageContent(
    page: OnboardingPage,
    pagerState: PagerState,
    pageIndex: Int,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier
            .padding(horizontal = spacing.screenPadding)
            .padding(top = spacing.xxxl),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OnboardingIllustration(
            illustrationKey = page.illustrationKey,
            accentColor = page.accentColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .graphicsLayer {
                    val pageOffset = (pagerState.currentPage - pageIndex) +
                            pagerState.currentPageOffsetFraction
                    val absOffset = pageOffset.absoluteValue
                    alpha = 1f - (absOffset * 0.4f).coerceIn(0f, 1f)
                    translationX = pageOffset * 60.dp.toPx()
                    scaleX = 1f - (absOffset * 0.08f).coerceIn(0f, 0.12f)
                    scaleY = scaleX
                },
        )

        Spacer(Modifier.height(spacing.xl))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = stringResource(page.subtitleRes).uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(
                    letterSpacing = TextUnit(1.5f, TextUnitType.Sp),
                ),
                color = page.accentColor,
            )

            Spacer(Modifier.height(spacing.xs))

            Text(
                text = stringResource(page.titleRes),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(Modifier.height(spacing.medium))

            Text(
                text = stringResource(page.descriptionRes),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingPageContentPreview() {
    CombatCoachTheme {
        val pagerState = rememberPagerState(
            initialPage = 0,
            pageCount = { 5 },
        )
        OnboardingPageContent(
            page = OnboardingPage(
                titleRes = R.string.onboarding_welcome_title,
                subtitleRes = R.string.onboarding_welcome_subtitle,
                descriptionRes = R.string.onboarding_welcome_description,
                illustrationKey = IllustrationKey.Welcome,
                accentColor = Periwinkle,
            ),
            pagerState = pagerState,
            pageIndex = 0
        )
    }
}
