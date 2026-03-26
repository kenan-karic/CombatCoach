package io.aethibo.combatcoach.features.main

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.aethibo.combatcoach.R
import io.aethibo.combatcoach.core.ui.popup.CoachPopup
import io.aethibo.combatcoach.core.ui.popup.PopupTypes
import io.aethibo.combatcoach.core.ui.theme.CombatCoachTheme
import io.aethibo.combatcoach.features.main.navigation.AppNavigation
import io.aethibo.combatcoach.features.main.navigation.Dashboard
import io.aethibo.combatcoach.features.onboarding.navigation.OnboardingRoute
import io.aethibo.combatcoach.shared.user.domain.model.ThemeMode
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    private val splashStateHolder = SplashStateHolder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupSplashScreen()

        setContent {
            NotificationPermissionEffect()

            CombatCoachApp()
        }
    }

    @Composable
    private fun CombatCoachApp() {
        val state = mainPresenter(
            splashStateHolder = splashStateHolder,
            loadPrefs = koinInject()
        )

        val darkTheme = when (state.themeMode) {
            ThemeMode.DARK -> true
            ThemeMode.LIGHT -> false
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
        }

        CombatCoachTheme(darkTheme = darkTheme) {
            when {
                state.isLoading -> Unit
                else -> AppNavigation(startDestination = startDestinationFor(state))
            }
        }
    }

    private fun startDestinationFor(state: MainState): Any = when {
        !state.isOnboardingCompleted -> OnboardingRoute
        else -> Dashboard
    }

    private fun setupSplashScreen() {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { splashStateHolder.isLoading.value }
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            ObjectAnimator.ofFloat(splashScreenView.view, View.ALPHA, 1f, 0f).apply {
                duration = 300L
                doOnEnd { splashScreenView.remove() }
                start()
            }
        }
    }
}

@Composable
private fun NotificationPermissionEffect() {
    val context = LocalContext.current
    var showRationale by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { }

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED -> Unit

            (context as? ComponentActivity)
                ?.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
                    == true -> showRationale = true

            else -> launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    if (showRationale) {
        NotificationRationaleCoachPopup(
            onAllow = {
                showRationale = false; launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            },
            onDismiss = { showRationale = false },
        )
    }
}

@Composable
fun NotificationRationaleCoachPopup(
    onAllow: () -> Unit,
    onDismiss: () -> Unit,
) {
    CoachPopup(
        popupType = PopupTypes.Generic,
        onDismissRequest = onDismiss,
        content = {
            Text(
                text = stringResource(R.string.notification_popup_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.notification_popup_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        },
        buttons = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onAllow,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(text = stringResource(R.string.action_allow))
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.action_not_now),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    )
}
