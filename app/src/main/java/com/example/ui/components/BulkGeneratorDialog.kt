package com.example.ui.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.BulkGenConfig
import com.example.data.model.HotspotPackage
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.Slate500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BulkGeneratorDialog(
    packages: List<HotspotPackage>,
    onDismiss: () -> Unit,
    onGenerate: (BulkGenConfig) -> Unit
) {
    var selectedQuantity by remember { mutableIntStateOf(50) }
    var prefix by remember { mutableStateOf("ISP01") }
    var selectedPackage by remember { mutableStateOf(packages.firstOrNull()) }
    var numbersOnly by remember { mutableStateOf(true) }

    val quantityOptions = listOf(1, 10, 50, 100, 500, 1000)

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
                        text = "Bulk Voucher Generator",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate500)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Quantity selector
                Text(
                    text = "Select Quantity:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate500
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quantityOptions.take(3).forEach { qty ->
                        FilterChip(
                            selected = selectedQuantity == qty,
                            onClick = { selectedQuantity = qty },
                            label = { Text("$qty pcs") }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quantityOptions.drop(3).forEach { qty ->
                        FilterChip(
                            selected = selectedQuantity == qty,
                            onClick = { selectedQuantity = qty },
                            label = { Text("$qty pcs") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Username Prefix
                OutlinedTextField(
                    value = prefix,
                    onValueChange = { prefix = it },
                    label = { Text("Username Prefix") },
                    placeholder = { Text("e.g. ISP01") },
                    supportingText = { Text("Example: ${prefix.ifBlank { "ISP" }}-1001, ${prefix.ifBlank { "ISP" }}-1002...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Select Package
                Text(
                    text = "Package / Profile:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate500
                )
                Spacer(modifier = Modifier.height(6.dp))
                packages.forEach { pkg ->
                    val isSelected = selectedPackage?.id == pkg.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedPackage = pkg },
                        label = { Text("${pkg.name} (${pkg.formattedData}, ${pkg.formattedValidity})") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Numeric Passwords Only (Easy to type)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Switch(checked = numbersOnly, onCheckedChange = { numbersOnly = it })
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val pkg = selectedPackage ?: packages.first()
                        onGenerate(
                            BulkGenConfig(
                                quantity = selectedQuantity,
                                prefix = prefix.trim(),
                                packageId = pkg.id,
                                packageName = pkg.name,
                                dataLimitMb = pkg.dataLimitMb,
                                timeLimitMins = pkg.timeLimitMins,
                                downloadSpeedMbps = pkg.downloadSpeedMbps,
                                uploadSpeedMbps = pkg.uploadSpeedMbps,
                                price = pkg.price,
                                isNumbersOnly = numbersOnly
                            )
                        )
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
                ) {
                    Text(
                        text = "Generate $selectedQuantity Vouchers Now",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
