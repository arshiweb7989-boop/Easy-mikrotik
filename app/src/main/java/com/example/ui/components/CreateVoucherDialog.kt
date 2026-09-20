package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.HotspotPackage
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateVoucherDialog(
    packages: List<HotspotPackage>,
    onDismiss: () -> Unit,
    onCreate: (packageName: String, dataLimitMb: Long, timeLimitMins: Long, downloadSpeedMbps: Double, uploadSpeedMbps: Double, price: Double, customCode: String?, customPass: String?) -> Unit
) {
    var selectedPackage by remember { mutableStateOf(packages.firstOrNull()) }
    var isCustomPackage by remember { mutableStateOf(false) }

    // Custom fields
    var customName by remember { mutableStateOf("Custom Voucher") }
    var selectedDataMb by remember { mutableStateOf(1024L) }
    var selectedTimeMins by remember { mutableStateOf(1440L) }
    var downloadSpeed by remember { mutableStateOf(5.0) }
    var uploadSpeed by remember { mutableStateOf(2.0) }
    var priceText by remember { mutableStateOf("2.0") }
    var customCodeText by remember { mutableStateOf("") }
    var customPassText by remember { mutableStateOf("") }

    val dataOptions = listOf(
        Pair("500 MB", 500L),
        Pair("1 GB", 1024L),
        Pair("2 GB", 2048L),
        Pair("5 GB", 5120L),
        Pair("10 GB", 10240L),
        Pair("Unlimited", 0L)
    )

    val timeOptions = listOf(
        Pair("1 Hour", 60L),
        Pair("6 Hours", 360L),
        Pair("12 Hours", 720L),
        Pair("1 Day", 1440L),
        Pair("3 Days", 4320L),
        Pair("7 Days", 10080L),
        Pair("30 Days", 43200L)
    )

    val speedOptions = listOf(
        Pair("512K/512K", Pair(0.5, 0.5)),
        Pair("1M/1M", Pair(1.0, 1.0)),
        Pair("2M/2M", Pair(2.0, 2.0)),
        Pair("2M/5M", Pair(2.0, 5.0)),
        Pair("5M/10M", Pair(5.0, 10.0)),
        Pair("10M/20M", Pair(10.0, 20.0))
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Create Hotspot Voucher",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate500)
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))

                // Mode Selector: Existing Package vs Custom
                Row(modifier = Modifier.fillMaxWidth()) {
                    FilterChip(
                        selected = !isCustomPackage,
                        onClick = { isCustomPackage = false },
                        label = { Text("Select Package") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = isCustomPackage,
                        onClick = { isCustomPackage = true },
                        label = { Text("Custom Limits") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (!isCustomPackage) {
                    Text(
                        text = "Choose a Profile / Package:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate500
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    packages.forEach { pkg ->
                        val isSelected = selectedPackage?.id == pkg.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) BrandBlue else Slate200,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .background(if (isSelected) BrandBlue.copy(alpha = 0.08f) else Color.Transparent)
                                .clickable { selectedPackage = pkg }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = pkg.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "${pkg.formattedData} • ${pkg.formattedValidity} • ${pkg.rateLimitStr}",
                                    fontSize = 12.sp,
                                    color = Slate500
                                )
                            }
                            Text(
                                text = "$${pkg.price}",
                                fontWeight = FontWeight.Bold,
                                color = BrandBlue,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    // Custom limits
                    OutlinedTextField(
                        value = customName,
                        onValueChange = { customName = it },
                        label = { Text("Package Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Data Limit:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate500)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        dataOptions.take(3).forEach { (label, mb) ->
                            FilterChip(
                                selected = selectedDataMb == mb,
                                onClick = { selectedDataMb = mb },
                                label = { Text(label, fontSize = 11.sp) }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        dataOptions.drop(3).forEach { (label, mb) ->
                            FilterChip(
                                selected = selectedDataMb == mb,
                                onClick = { selectedDataMb = mb },
                                label = { Text(label, fontSize = 11.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Time Limit:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate500)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        timeOptions.take(4).forEach { (label, mins) ->
                            FilterChip(
                                selected = selectedTimeMins == mins,
                                onClick = { selectedTimeMins = mins },
                                label = { Text(label, fontSize = 11.sp) }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        timeOptions.drop(4).forEach { (label, mins) ->
                            FilterChip(
                                selected = selectedTimeMins == mins,
                                onClick = { selectedTimeMins = mins },
                                label = { Text(label, fontSize = 11.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Speed Limit (Upload/Download):", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate500)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        speedOptions.take(3).forEach { (label, pair) ->
                            FilterChip(
                                selected = uploadSpeed == pair.first && downloadSpeed == pair.second,
                                onClick = {
                                    uploadSpeed = pair.first
                                    downloadSpeed = pair.second
                                },
                                label = { Text(label, fontSize = 11.sp) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                // Optional custom code & password
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = customCodeText,
                        onValueChange = { customCodeText = it },
                        label = { Text("Code (Optional)") },
                        placeholder = { Text("Auto") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = customPassText,
                        onValueChange = { customPassText = it },
                        label = { Text("Pass (Optional)") },
                        placeholder = { Text("Auto") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (!isCustomPackage && selectedPackage != null) {
                            val pkg = selectedPackage!!
                            onCreate(
                                pkg.name,
                                pkg.dataLimitMb,
                                pkg.timeLimitMins,
                                pkg.downloadSpeedMbps,
                                pkg.uploadSpeedMbps,
                                pkg.price,
                                customCodeText.ifBlank { null },
                                customPassText.ifBlank { null }
                            )
                        } else {
                            onCreate(
                                customName,
                                selectedDataMb,
                                selectedTimeMins,
                                downloadSpeed,
                                uploadSpeed,
                                priceText.toDoubleOrNull() ?: 0.0,
                                customCodeText.ifBlank { null },
                                customPassText.ifBlank { null }
                            )
                        }
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
                ) {
                    Text("Generate Voucher", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}
