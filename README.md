<img width="600" height="600" alt="combat_coach_app_logo" src="https://github.com/user-attachments/assets/feffff96-bdd5-4c5b-9f3d-10410b884475" />

# Combat Coach
> A fully offline Android training companion for martial artists — built without ViewModel.

Combat Coach is a privacy-first, offline Android app that helps martial artists plan, execute, and track training sessions. No account required, no data leaves your device. It's also a working reference implementation of a ViewModel-free Compose architecture using the Presenter pattern.

---

## What makes this architecture different

Most Compose apps use ViewModel as the state holder for every screen. Combat Coach doesn't. Instead, state is managed by a `@Composable` function called a **Presenter** — the same contract as a ViewModel, without the framework coupling.

```
FeedRoute              ← only stateful composable; wires Presenter → Screen
  └── FeedPresenter    ← @Composable state machine; holds state with remember
        └── Screen + components  ← pure functions of State; zero VM references
```

This means every composable below the Route:
- is previewable by constructing a single data class
- is testable with `createComposeRule` and fake state — no mocking needed
- has zero knowledge of use cases, repositories, or the network

---

## Features

- **Training programs** — create structured programs with multiple workout sessions
- **Exercise library** — browse and manage martial arts techniques and drills
- **Session timer** — foreground-service-backed timer with round/rest intervals
- **Offline-first** — Room is always the source of truth; no network required after first load
- **Typed error handling** — failures flow as `Either<Failure, T>` from data layer to UI; no generic try-catch near the UI
- **Optimistic updates** — local writes hit Room in ~20ms; network confirms in the background with rollback on failure

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | Presenter pattern (no ViewModel) |
| DI | Koin |
| Database | Room (via KSP) |
| Error handling | Arrow (`Either`) |
| Serialization | Kotlin Serialization |
| Async | Coroutines + Flow |
| Background | Foreground Service (`TimerServiceController`) |
| Build | Gradle Kotlin DSL + Version Catalogs |

---

## Architecture overview

```
ui/
  Route.kt          ← stateful; injects use cases via koinInject()
  Presenter.kt      ← @Composable; returns a State data class
  Screen.kt         ← stateless; accepts State, emits Events via eventSink
  components/       ← all pure functions; no hidden dependencies

domain/
  ObserveUseCase.kt   ← fun interface; e.g. (Key) -> Flow<Either<Failure, T>>
  Failure.kt          ← sealed interface; pure Kotlin, no Android imports

data/
  RepositoryImpl.kt   ← Room-first; network syncs into Room in background
  local/              ← Room DAOs and entities
  remote/             ← Ktor data source; throws typed exceptions
```

**Error handling contract — three layers, three responsibilities:**

```
Remote data source  →  typed FeedApiException (network / HTTP details)
        ↓
Use case            →  maps to domain Failure via either { } block
        ↓
Presenter           →  receives Either<Failure, T>; sets typed failure on State
        ↓
Screen              →  renders failure.toHumanMessage() — no try-catch in the UI
```

**Use cases are functions, not classes:**

```kotlin
// The entire contract — one line
fun interface ObservePostsUseCase :
    (FeedKey) -> Flow<Either<Failure, List<Post>>>

// The logic — a plain top-level function, no class required
fun observePosts(feedKey: FeedKey, repo: FeedRepository) =
    repo.observePosts(feedKey)

// DI wiring — one line
factory<ObservePostsUseCase> {
    ObservePostsUseCase { key -> observePosts(key, get()) }
}
```

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1) or newer
- JDK 17+
- Android SDK 26+ (minSdk)

### Build

```bash
git clone https://github.com/kenan-karic/CombatCoach.git
cd CombatCoach
./gradlew assembleDebug
```

Run on a device or emulator (API 26+), or install directly:

```
app/build/outputs/apk/debug/app-debug.apk
```

### Run tests

```bash
# Unit tests — use cases and repository logic run on the JVM, no Android environment needed
./gradlew test

# Instrumented tests — Presenter and UI layer via createComposeRule
./gradlew connectedAndroidTest
```

---

## Previews

Because every screen composable is a pure function of its state, previews need no DI setup:

```kotlin
@Preview @Composable
private fun FeedScreenLoadingPreview() = AppTheme {
    FeedScreen(FeedState(isRefreshing = true))
}

@Preview @Composable
private fun FeedScreenPopulatedPreview() = AppTheme {
    FeedScreen(FeedState(posts = PreviewData.posts))
}

@Preview @Composable
private fun FeedScreenErrorPreview() = AppTheme {
    FeedScreen(FeedState(feedError = Failure.NetworkUnavailable))
}
```

---

## Background

This app was built alongside the **Compose Without ViewModel** series — an 8-episode deep dive into replacing ViewModel with the Presenter pattern in a greenfield Compose project. The series covers:

1. Why ViewModel is solving problems Compose already solves
2. The Presenter pattern — one `@Composable`, one `State` data class, one `eventSink`
3. Stateless screens — why every composable below Route should be a pure function
4. Use cases as functions — `fun interface` + top-level function, no class boilerplate
5. Typed error handling — `ApiException` → `Failure` → human message
6. Offline-first repository — Room as source of truth, network syncing in background
7. Optimistic updates — local write in ~20ms, rollback on failure
8. Full feature walkthrough — every layer assembled end to end

---

## Contributing

Contributions are welcome. To get started:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes and open a pull request against `main`

Please follow the existing conventions — Presenter pattern, `fun interface` use cases, `Either`-based error handling — and keep PRs focused on a single concern.

---

## Support

Open an issue on [GitHub](https://github.com/kenan-karic/CombatCoach/issues) for bugs or feature requests.

---

## Maintainer

**Kenan Karic** — Android Engineer  
[github.com/kenan-karic](https://github.com/kenan-karic)

---

## License

Distributed under the [Apache 2.0 License](LICENSE).
