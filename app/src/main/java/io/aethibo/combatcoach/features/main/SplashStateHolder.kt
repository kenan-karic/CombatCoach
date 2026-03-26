package io.aethibo.combatcoach.features.main

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * A minimal, non-ViewModel state holder that exists solely to give
 * the splash screen a synchronous `isLoading` value before composition
 * starts.
 *
 * WHY THIS EXISTS:
 *   SplashScreen.setKeepOnScreenCondition { } runs on the main thread
 *   before setContent { } is called. A Presenter lives inside composition
 *   and cannot be read at that point. A ViewModel survives pre-composition,
 *   but it introduces Hilt/Dagger setup and a full ViewModel lifecycle for
 *   a single Boolean.
 *
 *   This class is the lightest possible bridge: a plain StateFlow that the
 *   Activity reads for the splash condition, and the Presenter writes to
 *   once it has loaded its data.
 *
 * LIFECYCLE:
 *   Created in MainActivity.onCreate() before setContent {}.
 *   Written to by MainPresenter via the onLoadingComplete callback.
 *   Not injected — owned directly by the Activity.
 */
@Stable
class SplashStateHolder {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun onLoadingComplete() {
        _isLoading.update { false }
    }
}
