package io.aethibo.combatcoach.features.main.navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.aethibo.combatcoach.core.ui.navigation.Destination

private data class NavItem(
    val destination: Destination,
    val icon: ImageVector,
    val contentDescription: String,
)

private val navItems = listOf(
    NavItem(Home, Icons.Outlined.Home, "Home"),
    NavItem(Plans, Icons.Outlined.CalendarMonth, "Plans"),
    NavItem(Progress, Icons.Outlined.BarChart, "Progress"),
    NavItem(Achievements, Icons.Outlined.EmojiEvents, "Achievements"),
    NavItem(Settings, Icons.Outlined.Settings, "Settings"),
)

@Composable
fun BottomNavigationBar(
    currentRoute: Destination?,
    onNavigate: (Destination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            navItems.forEach { item ->
                MinimalNavItem(
                    item = item,
                    selected = currentRoute == item.destination,
                    onClick = { onNavigate(item.destination) },
                )
            }
        }
    }
}

@Composable
private fun MinimalNavItem(
    item: NavItem,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val iconAlpha by animateFloatAsState(
        targetValue = if (selected) 1f else 0.4f,
        animationSpec = tween(200),
        label = "iconAlpha",
    )
    val dotSize by animateDpAsState(
        targetValue = if (selected) 4.dp else 0.dp,
        animationSpec = tween(200),
        label = "dotSize",
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (selected)
            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.06f)
        else
            MaterialTheme.colorScheme.surface,
        modifier = Modifier.width(52.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 8.dp),
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.contentDescription,
                modifier = Modifier
                    .size(22.dp)
                    .alpha(iconAlpha),
                tint = if (selected)
                    MaterialTheme.colorScheme.onBackground
                else
                    MaterialTheme.colorScheme.onBackground,
            )

            Spacer(Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .size(dotSize)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                    )
            )
        }
    }
}
