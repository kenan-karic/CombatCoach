package io.aethibo.combatcoach.features.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.theme.CoralPink
import io.aethibo.combatcoach.core.ui.theme.MintGreen
import io.aethibo.combatcoach.core.ui.theme.Periwinkle
import io.aethibo.combatcoach.features.onboarding.model.IllustrationKey
import io.aethibo.combatcoach.features.onboarding.model.OnboardingPage
import io.aethibo.combatcoach.features.shared.user.domain.usecase.LoadUserPrefsUseCase
import io.aethibo.combatcoach.features.shared.user.domain.model.UserPrefs
import io.aethibo.combatcoach.features.shared.user.domain.usecase.SaveUserPrefsUseCase
import kotlinx.coroutines.launch

@Composable
fun onboardingPresenter(
    savePrefs: SaveUserPrefsUseCase,
    loadPrefs: LoadUserPrefsUseCase,
    onFinished: () -> Unit,
): OnboardingState {
    val scope = rememberCoroutineScope()
    val pages = remember { buildOnboardingPages() }

    // Extracted as a suspend function — no navigation side effects inside
    suspend fun markOnboardingComplete() {
        loadPrefs().fold(
            ifRight = { prefs ->
                savePrefs(prefs.copy(onboardingComplete = true))
                // onFinished() NOT called here — called once in the outer launch block
            },
            ifLeft = {
                // Fail-open: save defaults with onboardingComplete = true
                // so the user never sees onboarding again even if load fails
                savePrefs(UserPrefs(onboardingComplete = true))
            }
        )
    }

    val eventSink: (OnboardingEvent) -> Unit = remember {
        { event ->
            when (event) {
                OnboardingEvent.Finish,
                OnboardingEvent.Skip -> scope.launch {
                    markOnboardingComplete()
                    onFinished() // called exactly once, after save completes
                }
            }
        }
    }

    return OnboardingState(
        pages = pages,
        eventSink = eventSink,
    )
}

private fun buildOnboardingPages(): List<OnboardingPage> {
    return listOf(
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
            discipline = Discipline.STRIKING,
        ),
        OnboardingPage(
            titleRes = R.string.onboarding_timer_title,
            subtitleRes = R.string.onboarding_timer_subtitle,
            descriptionRes = R.string.onboarding_timer_description,
            illustrationKey = IllustrationKey.Timer,
            accentColor = MintGreen,
        ),
        OnboardingPage(
            titleRes = R.string.onboarding_progress_title,
            subtitleRes = R.string.onboarding_progress_subtitle,
            descriptionRes = R.string.onboarding_progress_description,
            illustrationKey = IllustrationKey.Progress,
            accentColor = Periwinkle,
        ),
        OnboardingPage(
            titleRes = R.string.onboarding_achievements_title,
            subtitleRes = R.string.onboarding_achievements_subtitle,
            descriptionRes = R.string.onboarding_achievements_description,
            illustrationKey = IllustrationKey.Achievements,
            accentColor = CoralPink,
        ),
    )
}

