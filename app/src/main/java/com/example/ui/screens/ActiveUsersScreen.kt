package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActiveSession
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveUsersScreen(
    activeSessions: List<ActiveSession>,
    onBack: () -> Unit,
    onDisconnect: (ActiveSession) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Sensors, contentDescription = null, tint = BrandBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Active Hotspot Sessions (${activeSessions.size})", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (activeSessions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Sensors, contentDescription = null, modifier = Modifier.size(52.dp), tint = Slate400)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("No active users right now", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate600)
                        Text("When clients log into Wi-Fi, their sessions appear here.", fontSize = 12.sp, color = Slate400)
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(activeSessions, key = { it.id }) { session ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = session.username,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            color = BrandBlue
                                        )
                                        Text(
                                            text = "IP: ${session.ipAddress} • MAC: ${session.macAddress}",
                                            fontSize = 11.sp,
                                            color = Slate500
                                        )
                                    }

                                    Button(
                                        onClick = { onDisconnect(session) },
                                        colors = ButtonDefaults.buttonColors(containerColor = StatusRedLight, contentColor = StatusRed),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Icon(Icons.Default.PowerSettingsNew, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Disconnect", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Slate200)

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Uptime", fontSize = 10.sp, color = Slate500)
                                        Text(session.uptime, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                    Column {
                                        Text("Download", fontSize = 10.sp, color = Slate500)
                                        Text(session.formattedDownload, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BrandBlue)
                                    }
                                    Column {
                                        Text("Upload", fontSize = 10.sp, color = Slate500)
                                        Text(session.formattedUpload, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BrandTeal)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("Login Method", fontSize = 10.sp, color = Slate500)
                                        Text(session.loginBy, fontSize = 11.sp, color = Slate600)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
