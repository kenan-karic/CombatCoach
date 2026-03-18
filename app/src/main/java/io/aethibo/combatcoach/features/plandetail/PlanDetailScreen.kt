package io.aethibo.combatcoach.features.plandetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import io.aethibo.combatcoach.core.ui.theme.LocalSpacing
import io.aethibo.combatcoach.features.plandetail.components.PlanBottomBar
import io.aethibo.combatcoach.features.plandetail.components.PlanDetailHeader
import io.aethibo.combatcoach.features.plandetail.components.PlanProgressCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDetailScreen(
    state: PlanDetailState,
    onBack: () -> Unit,
) {
    val sp = LocalSpacing.current

    if (state.isLoading || state.plan == null) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            Alignment.Center,
        ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
        return
    }

    val plan = state.plan   // smart-cast after null check

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        plan.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { state.eventSink(PlanDetailEvent.Edit) }) {
                        Icon(Icons.Outlined.Edit, "Edit plan")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
        bottomBar = { PlanBottomBar(state = state) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = sp.xxl),
        ) {
            item { PlanDetailHeader(plan = plan, state = state) }

            if (state.isActivePlan && state.activePlan != null) {
                item {
                    PlanProgressCard(
                        state = state,
                        modifier = Modifier.padding(horizontal = sp.screenPadding),
                    )
                    Spacer(Modifier.height(sp.sectionGap))
                }
            }
        }
    }
}