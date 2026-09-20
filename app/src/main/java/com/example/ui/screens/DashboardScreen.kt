package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ConnectionStatus
import com.example.data.model.HotspotUser
import com.example.data.model.RouterDevice
import com.example.data.model.Voucher
import com.example.data.model.VoucherStatus
import com.example.ui.components.StatCard
import com.example.ui.theme.*
import com.example.viewmodel.NavigationTab

@Composable
fun DashboardScreen(
    currentRouter: RouterDevice?,
    connectionStatus: ConnectionStatus,
    vouchers: List<Voucher>,
    users: List<HotspotUser>,
    activeCount: Int,
    onCreateVoucherClick: () -> Unit,
    onBulkGenClick: () -> Unit,
    onUsersClick: () -> Unit,
    onActiveUsersClick: () -> Unit,
    onPackagesClick: () -> Unit,
    onNavigateToRouter: () -> Unit
) {
    val activeVouchersCount = vouchers.count { it.status == VoucherStatus.ACTIVE }
    val totalVouchersCount = vouchers.size
    val totalUsersCount = users.size

    val totalDownloadBytes = users.sumOf { it.bytesOut }
    val totalUploadBytes = users.sumOf { it.bytesIn }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Router Status Header Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Slate200, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = currentRouter?.name ?: "No Router Connected",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = currentRouter?.let { "${it.model} • ${it.ip}" } ?: "Connect to start hotspot management",
                            fontSize = 12.sp,
                            color = Slate500
                        )
                    }

                    // Connection Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                when (connectionStatus) {
                                    is ConnectionStatus.Connected -> StatusGreenLight
                                    is ConnectionStatus.Connecting -> StatusAmberLight
                                    else -> StatusRedLight
                                }
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
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
                                is ConnectionStatus.Connected -> "Connected 🟢"
                                is ConnectionStatus.Connecting -> "Connecting"
                                else -> "Disconnected 🔴"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = when (connectionStatus) {
                                is ConnectionStatus.Connected -> StatusGreen
                                is ConnectionStatus.Connecting -> StatusAmber
                                else -> StatusRed
                            }
                        )
                    }
                }

                if (currentRouter != null) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Slate200)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("RouterOS", fontSize = 11.sp, color = Slate500)
                            Text(
                                text = currentRouter.rosVersion,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Column {
                            Text("Uptime", fontSize = 11.sp, color = Slate500)
                            Text(
                                text = currentRouter.uptime,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Column {
                            Text("CPU Load", fontSize = 11.sp, color = Slate500)
                            Text(
                                text = "${currentRouter.cpuLoad}%",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (currentRouter.cpuLoad > 75) StatusRed else BrandBlue
                            )
                        }
                        Column {
                            Text("Free RAM", fontSize = 11.sp, color = Slate500)
                            Text(
                                text = "${currentRouter.freeMemoryMb} MB",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section Title: Overview
        Text(
            text = "Hotspot Overview",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 6 Cards Grid (2 columns)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = "Active Users",
                value = activeCount.toString(),
                subtitle = "Live connected now",
                icon = Icons.Default.Wifi,
                iconTint = BrandBlue,
                iconBgColor = BrandBlueLight,
                modifier = Modifier.weight(1f),
                onClick = onActiveUsersClick
            )
            StatCard(
                title = "Total Users",
                value = totalUsersCount.toString(),
                subtitle = "Provisioned in router",
                icon = Icons.Default.People,
                iconTint = BrandTeal,
                iconBgColor = BrandTealLight,
                modifier = Modifier.weight(1f),
                onClick = onUsersClick
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = "Active Vouchers",
                value = activeVouchersCount.toString(),
                subtitle = "Currently in use",
                icon = Icons.Default.ConfirmationNumber,
                iconTint = StatusGreen,
                iconBgColor = StatusGreenLight,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Total Vouchers",
                value = totalVouchersCount.toString(),
                subtitle = "Printed & active pool",
                icon = Icons.Default.Receipt,
                iconTint = Slate700,
                iconBgColor = Slate200,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = "Download Usage",
                value = HotspotUser.formatBytes(totalDownloadBytes),
                subtitle = "Transferred to clients",
                icon = Icons.Default.Download,
                iconTint = Color(0xFF7C3AED),
                iconBgColor = Color(0xFFEDE9FE),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Upload Usage",
                value = HotspotUser.formatBytes(totalUploadBytes),
                subtitle = "Received from clients",
                icon = Icons.Default.Upload,
                iconTint = Color(0xFFD97706),
                iconBgColor = Color(0xFFFEF3C7),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Actions Section
        Text(
            text = "Quick Actions",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onCreateVoucherClick,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Create Voucher", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onBulkGenClick,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandTeal)
            ) {
                Icon(Icons.Default.Layers, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Generate Bulk", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onActiveUsersClick,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Sensors, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Active Users ($activeCount)", fontSize = 12.sp)
            }

            OutlinedButton(
                onClick = onPackagesClick,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Inventory2, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Packages", fontSize = 12.sp)
            }
        }
    }
}
