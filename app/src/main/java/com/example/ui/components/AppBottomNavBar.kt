package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BrandBlue
import com.example.viewmodel.NavigationTab

data class NavItem(
    val tab: NavigationTab,
    val label: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
)

@Composable
fun AppBottomNavBar(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    activeVouchersCount: Int = 0,
    activeUsersCount: Int = 0
) {
    val items = listOf(
        NavItem(NavigationTab.DASHBOARD, "Dashboard", Icons.Default.Dashboard),
        NavItem(NavigationTab.VOUCHERS, "Vouchers", Icons.Default.ConfirmationNumber, activeVouchersCount),
        NavItem(NavigationTab.USERS, "Users", Icons.Default.People, activeUsersCount),
        NavItem(NavigationTab.ISP_PROFILE, "ISP Profile", Icons.Default.Business),
        NavItem(NavigationTab.ROUTER, "Router", Icons.Default.Router)
    )

    NavigationBar(
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEach { item ->
            val selected = currentTab == item.tab
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(item.tab) },
                icon = {
                    if (item.badgeCount > 0) {
                        BadgedBox(badge = {
                            Badge { Text(item.badgeCount.toString()) }
                        }) {
                            Icon(item.icon, contentDescription = item.label)
                        }
                    } else {
                        Icon(item.icon, contentDescription = item.label)
                    }
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BrandBlue,
                    selectedTextColor = BrandBlue,
                    indicatorColor = BrandBlue.copy(alpha = 0.12f)
                )
            )
        }
    }
}
