package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ConnectionStatus
import com.example.data.model.DiscoveryResult
import com.example.data.model.RouterDevice
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouterScreen(
    currentRouter: RouterDevice?,
    connectionStatus: ConnectionStatus,
    savedRouters: List<RouterDevice>,
    discoveredRouters: List<DiscoveryResult>,
    isDiscovering: Boolean,
    onFindRouterAuto: () -> Unit,
    onConnectManual: (ip: String, port: Int, user: String, pass: String, isSsl: Boolean, isMock: Boolean) -> Unit,
    onSwitchRouter: (RouterDevice) -> Unit,
    onDisconnect: () -> Unit
) {
    var routerIp by remember { mutableStateOf(currentRouter?.ip ?: "192.168.88.1") }
    var username by remember { mutableStateOf(currentRouter?.username ?: "admin") }
    var password by remember { mutableStateOf(currentRouter?.password ?: "") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var portText by remember { mutableStateOf("8728") }
    var isSsl by remember { mutableStateOf(false) }
    var isDemoMode by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Heading
        Text(
            text = "Router Connection",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Connect directly to MikroTik RouterOS API (v6 & v7 supported).",
            fontSize = 12.sp,
            color = Slate500
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Connection Status Banner
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (connectionStatus) {
                    is ConnectionStatus.Connected -> StatusGreenLight
                    is ConnectionStatus.Connecting -> StatusAmberLight
                    is ConnectionStatus.Error -> StatusRedLight
                    else -> Slate100
                }
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(
                            when (connectionStatus) {
                                is ConnectionStatus.Connected -> StatusGreen
                                is ConnectionStatus.Connecting -> StatusAmber
                                is ConnectionStatus.Error -> StatusRed
                                else -> Slate500
                            }
                        )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = when (connectionStatus) {
                            is ConnectionStatus.Connected -> "Connected 🟢 (${connectionStatus.router.identity})"
                            is ConnectionStatus.Connecting -> "Connecting to RouterOS API..."
                            is ConnectionStatus.Error -> "Connection Error"
                            else -> "Not Connected"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = when (connectionStatus) {
                            is ConnectionStatus.Connected -> StatusGreen
                            is ConnectionStatus.Connecting -> StatusAmber
                            is ConnectionStatus.Error -> StatusRed
                            else -> Slate700
                        }
                    )
                    if (connectionStatus is ConnectionStatus.Error) {
                        Text(
                            text = connectionStatus.message,
                            fontSize = 12.sp,
                            color = StatusRed
                        )
                    } else if (connectionStatus is ConnectionStatus.Connected) {
                        Text(
                            text = "${connectionStatus.router.model} • ${connectionStatus.router.rosVersion} • IP: ${connectionStatus.router.ip}",
                            fontSize = 12.sp,
                            color = Slate600
                        )
                    }
                }

                if (connectionStatus is ConnectionStatus.Connected) {
                    TextButton(onClick = onDisconnect) {
                        Text("Disconnect", color = StatusRed, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // OPTION 1: FIND ROUTER AUTOMATICALLY
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Slate200, RoundedCornerShape(14.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(BrandBlueLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.WifiFind, contentDescription = null, tint = BrandBlue)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "1. Find Router Automatically",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Scans Wi-Fi/LAN for MikroTik routers",
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onFindRouterAuto,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
                ) {
                    if (isDiscovering) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Scanning Network...")
                    } else {
                        Icon(Icons.Default.Radar, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Scan Local Network", fontWeight = FontWeight.Bold)
                    }
                }

                // Discovered Routers List
                if (discoveredRouters.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Discovered MikroTik Routers (Tap to select):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate600
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    discoveredRouters.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, Slate200, RoundedCornerShape(8.dp))
                                .background(if (routerIp == item.ip) BrandBlueLight else Color.Transparent)
                                .clickable {
                                    routerIp = item.ip
                                }
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${item.identity} (${item.model})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "IP: ${item.ip} • MAC: ${item.mac} • ${item.version}",
                                    fontSize = 11.sp,
                                    color = Slate500
                                )
                            }
                            if (routerIp == item.ip) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // OPTION 2: MANUAL ROUTER CREDENTIALS & CONNECT
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Slate200, RoundedCornerShape(14.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Router Credentials",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Supports RB750, RB750Gr3, RB4011, CCR series on RouterOS v6 and v7.",
                    fontSize = 11.sp,
                    color = Slate500
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 2. Router IP
                OutlinedTextField(
                    value = routerIp,
                    onValueChange = { routerIp = it },
                    label = { Text("2. Router IP") },
                    placeholder = { Text("192.168.88.1") },
                    leadingIcon = { Icon(Icons.Default.Dns, contentDescription = null, tint = Slate500) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 3. Username
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("3. Username") },
                    placeholder = { Text("admin") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Slate500) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 4. Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("4. Password") },
                    placeholder = { Text("RouterOS password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Slate500) },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = Slate500
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = portText,
                        onValueChange = { portText = it },
                        label = { Text("API Port") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        Checkbox(checked = isSsl, onCheckedChange = { isSsl = it })
                        Text("SSL (8729)", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Simulation / Demo Engine", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text("Allows full hotspot testing without hardware", fontSize = 11.sp, color = Slate500)
                    }
                    Switch(checked = isDemoMode, onCheckedChange = { isDemoMode = it })
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 5. Connect Button
                Button(
                    onClick = {
                        onConnectManual(
                            routerIp.trim(),
                            portText.toIntOrNull() ?: 8728,
                            username.trim(),
                            password,
                            isSsl,
                            isDemoMode
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
                ) {
                    Icon(Icons.Default.Link, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("5. Connect to MikroTik", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SAVED ROUTERS MANAGEMENT
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Slate200, RoundedCornerShape(14.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Saved Routers",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Switch between Main ISP, Area 2, Campus, etc.",
                    fontSize = 11.sp,
                    color = Slate500
                )

                Spacer(modifier = Modifier.height(10.dp))

                savedRouters.forEach { r ->
                    val isCurrent = r.id == currentRouter?.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                width = if (isCurrent) 1.5.dp else 1.dp,
                                color = if (isCurrent) BrandBlue else Slate200,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(if (isCurrent) BrandBlue.copy(alpha = 0.08f) else Color.Transparent)
                            .clickable { onSwitchRouter(r) }
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = r.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                if (isCurrent) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "ACTIVE",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrandBlue
                                    )
                                }
                            }
                            Text(
                                text = "${r.model} • ${r.ip} • ${r.rosVersion}",
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        }

                        Button(
                            onClick = { onSwitchRouter(r) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCurrent) StatusGreen else Slate200,
                                contentColor = if (isCurrent) Color.White else Slate700
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(if (isCurrent) "Connected" else "Select", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}
