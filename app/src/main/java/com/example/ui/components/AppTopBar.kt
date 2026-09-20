package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ConnectionStatus
import com.example.data.model.RouterDevice
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentRouter: RouterDevice?,
    connectionStatus: ConnectionStatus,
    savedRouters: List<RouterDevice>,
    onSwitchRouter: (RouterDevice) -> Unit,
    onNavigateToRouterScreen: () -> Unit,
    isDarkTheme: Boolean = false,
    onToggleDarkTheme: (() -> Unit)? = null
) {
    var showRouterDropdown by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(BrandBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = "MikroEasy Logo",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "MikroEasy",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = currentRouter?.let { "${it.identity} (${if (it.isV7) "ROS v7" else "ROS v6"})" } ?: "No Router Selected",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            // Status Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        when (connectionStatus) {
                            is ConnectionStatus.Connected -> StatusGreenLight
                            is ConnectionStatus.Connecting -> StatusAmberLight
                            else -> StatusRedLight
                        }
                    )
                    .clickable { showRouterDropdown = true }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                when (connectionStatus) {
                                    is ConnectionStatus.Connected -> StatusGreen
                                    is ConnectionStatus.Connecting -> StatusAmber
                                    else -> StatusRed
                                }
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when (connectionStatus) {
                            is ConnectionStatus.Connected -> "Connected"
                            is ConnectionStatus.Connecting -> "Connecting..."
                            else -> "Offline"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = when (connectionStatus) {
                            is ConnectionStatus.Connected -> StatusGreen
                            is ConnectionStatus.Connecting -> StatusAmber
                            else -> StatusRed
                        }
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Switch",
                        modifier = Modifier.size(16.dp),
                        tint = when (connectionStatus) {
                            is ConnectionStatus.Connected -> StatusGreen
                            is ConnectionStatus.Connecting -> StatusAmber
                            else -> StatusRed
                        }
                    )
                }

                DropdownMenu(
                    expanded = showRouterDropdown,
                    onDismissRequest = { showRouterDropdown = false }
                ) {
                    Text(
                        text = "Switch Saved Router",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate500,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                    savedRouters.forEach { router ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = router.name,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "${router.ip} • ${router.rosVersion}",
                                        fontSize = 11.sp,
                                        color = Slate500
                                    )
                                }
                            },
                            onClick = {
                                onSwitchRouter(router)
                                showRouterDropdown = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Router,
                                    contentDescription = null,
                                    tint = if (router.id == currentRouter?.id) BrandBlue else Slate400
                                )
                            }
                        )
                    }
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Manage Routers...", fontSize = 13.sp, color = BrandBlue) },
                        onClick = {
                            showRouterDropdown = false
                            onNavigateToRouterScreen()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Settings, contentDescription = null, tint = BrandBlue)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}
