package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActiveSession
import com.example.data.model.HotspotUser
import com.example.data.model.UserStatus
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    users: List<HotspotUser>,
    activeSessions: List<ActiveSession>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedFilter: UserStatus?,
    onFilterSelect: (UserStatus?) -> Unit,
    onViewActiveUsers: () -> Unit,
    onDisconnect: (HotspotUser) -> Unit,
    onToggleDisabled: (HotspotUser) -> Unit,
    onResetUsage: (HotspotUser) -> Unit,
    onDeleteUser: (HotspotUser) -> Unit
) {
    val filteredUsers = users.filter { u ->
        val matchesQuery = searchQuery.isBlank() ||
                u.username.contains(searchQuery, ignoreCase = true) ||
                u.profile.contains(searchQuery, ignoreCase = true)
        val matchesFilter = selectedFilter == null || u.status == selectedFilter
        matchesQuery && matchesFilter
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Active Users banner shortcut
        Card(
            onClick = onViewActiveUsers,
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = BrandBlueLight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Sensors, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${activeSessions.size} Active Connected Sessions",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandBlueDark
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("View All", fontSize = 12.sp, color = BrandBlue, fontWeight = FontWeight.SemiBold)
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(18.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search users by username...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Slate500) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = Slate500)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = selectedFilter == null,
                onClick = { onFilterSelect(null) },
                label = { Text("All (${users.size})", fontSize = 11.sp) }
            )
            FilterChip(
                selected = selectedFilter == UserStatus.ACTIVE,
                onClick = { onFilterSelect(UserStatus.ACTIVE) },
                label = { Text("Active", fontSize = 11.sp) }
            )
            FilterChip(
                selected = selectedFilter == UserStatus.OFFLINE,
                onClick = { onFilterSelect(UserStatus.OFFLINE) },
                label = { Text("Offline", fontSize = 11.sp) }
            )
            FilterChip(
                selected = selectedFilter == UserStatus.EXPIRED,
                onClick = { onFilterSelect(UserStatus.EXPIRED) },
                label = { Text("Expired", fontSize = 11.sp) }
            )
            FilterChip(
                selected = selectedFilter == UserStatus.DISABLED,
                onClick = { onFilterSelect(UserStatus.DISABLED) },
                label = { Text("Disabled", fontSize = 11.sp) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // List
        if (filteredUsers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.PersonOff, contentDescription = null, modifier = Modifier.size(48.dp), tint = Slate400)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No users found", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Slate500)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredUsers, key = { it.id }) { user ->
                    UserCardItem(
                        user = user,
                        isActive = user.status == UserStatus.ACTIVE,
                        onDisconnect = { onDisconnect(user) },
                        onToggleDisabled = { onToggleDisabled(user) },
                        onResetUsage = { onResetUsage(user) },
                        onDelete = { onDeleteUser(user) }
                    )
                }
            }
        }
    }
}

@Composable
fun UserCardItem(
    user: HotspotUser,
    isActive: Boolean,
    onDisconnect: () -> Unit,
    onToggleDisabled: () -> Unit,
    onResetUsage: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Slate200, RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header: Username & Status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.username,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                when (user.status) {
                                    UserStatus.ACTIVE -> StatusGreenLight
                                    UserStatus.OFFLINE -> Slate200
                                    UserStatus.EXPIRED -> StatusAmberLight
                                    UserStatus.DISABLED -> StatusRedLight
                                }
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = user.status.name,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (user.status) {
                                UserStatus.ACTIVE -> StatusGreen
                                UserStatus.OFFLINE -> Slate600
                                UserStatus.EXPIRED -> StatusAmber
                                UserStatus.DISABLED -> StatusRed
                            }
                        )
                    }
                }

                Text(
                    text = user.profile,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandBlue
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Stats grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Usage", fontSize = 10.sp, color = Slate500)
                    Text(user.formattedUsage, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Column {
                    Text("Remaining", fontSize = 10.sp, color = Slate500)
                    Text(user.remainingData, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Column {
                    Text("Uptime / Limit", fontSize = 10.sp, color = Slate500)
                    Text("${user.uptime} / ${user.limitUptime}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Speed Limit", fontSize = 10.sp, color = Slate500)
                    Text(user.speedLimit, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            if (user.macAddress != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "MAC: ${user.macAddress} • Last login: ${user.lastLogin}",
                    fontSize = 10.sp,
                    color = Slate500
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isActive) {
                    TextButton(
                        onClick = onDisconnect,
                        colors = ButtonDefaults.textButtonColors(contentColor = StatusAmber),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.PowerSettingsNew, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Disconnect", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                TextButton(
                    onClick = onResetUsage,
                    colors = ButtonDefaults.textButtonColors(contentColor = BrandBlue),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reset", fontSize = 11.sp)
                }

                TextButton(
                    onClick = onToggleDisabled,
                    colors = ButtonDefaults.textButtonColors(contentColor = if (user.disabled) StatusGreen else Slate600),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(if (user.disabled) "Enable" else "Disable", fontSize = 11.sp)
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = StatusRed, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
