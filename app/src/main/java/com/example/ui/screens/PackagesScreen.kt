package com.example.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HotspotPackage
import com.example.ui.theme.*
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackagesScreen(
    packages: List<HotspotPackage>,
    onBack: () -> Unit,
    onCreatePackage: (HotspotPackage) -> Unit,
    onDeletePackage: (HotspotPackage) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Inventory2, contentDescription = null, tint = BrandBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Hotspot Profiles / Packages", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = BrandBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Package")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Reusable profiles synced with MikroTik '/ip hotspot user profile'.",
                fontSize = 12.sp,
                color = Slate500
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(packages, key = { it.id }) { pkg ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = pkg.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (pkg.isDefault) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "DEFAULT",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BrandTeal
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${pkg.formattedData} • ${pkg.formattedValidity} • Rate: ${pkg.rateLimitStr}",
                                    fontSize = 12.sp,
                                    color = Slate600
                                )
                                Text(
                                    text = "Price: $${pkg.price} • Shared Users: ${pkg.sharedUsers}",
                                    fontSize = 11.sp,
                                    color = Slate500
                                )
                            }

                            if (!pkg.isDefault) {
                                IconButton(onClick = { onDeletePackage(pkg) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = StatusRed, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var name by remember { mutableStateOf("") }
        var dataMbText by remember { mutableStateOf("2048") }
        var validityMinsText by remember { mutableStateOf("1440") }
        var downSpeedText by remember { mutableStateOf("5") }
        var upSpeedText by remember { mutableStateOf("2") }
        var priceText by remember { mutableStateOf("3.0") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Create Reusable Package", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Package Name (e.g. Daily 2GB)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = dataMbText,
                            onValueChange = { dataMbText = it },
                            label = { Text("Data (MB, 0=Unlim)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = validityMinsText,
                            onValueChange = { validityMinsText = it },
                            label = { Text("Validity (Minutes)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = downSpeedText,
                            onValueChange = { downSpeedText = it },
                            label = { Text("Download (Mbps)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = upSpeedText,
                            onValueChange = { upSpeedText = it },
                            label = { Text("Upload (Mbps)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("Price ($)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            onCreatePackage(
                                HotspotPackage(
                                    id = UUID.randomUUID().toString(),
                                    name = name.trim(),
                                    dataLimitMb = dataMbText.toLongOrNull() ?: 0L,
                                    timeLimitMins = validityMinsText.toLongOrNull() ?: 1440L,
                                    downloadSpeedMbps = downSpeedText.toDoubleOrNull() ?: 5.0,
                                    uploadSpeedMbps = upSpeedText.toDoubleOrNull() ?: 2.0,
                                    price = priceText.toDoubleOrNull() ?: 0.0
                                )
                            )
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
                ) {
                    Text("Create Profile")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
