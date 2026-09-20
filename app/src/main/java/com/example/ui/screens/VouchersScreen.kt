package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.data.model.Voucher
import com.example.data.model.VoucherStatus
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VouchersScreen(
    vouchers: List<Voucher>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedFilter: VoucherStatus?,
    onFilterSelect: (VoucherStatus?) -> Unit,
    onCreateClick: () -> Unit,
    onBulkClick: () -> Unit,
    onPrintVoucher: (Voucher) -> Unit,
    onPrintAll: () -> Unit,
    onToggleStatus: (Voucher) -> Unit,
    onDeleteVoucher: (Voucher) -> Unit
) {
    val filteredVouchers = vouchers.filter { v ->
        val matchesQuery = searchQuery.isBlank() ||
                v.code.contains(searchQuery, ignoreCase = true) ||
                v.packageName.contains(searchQuery, ignoreCase = true)
        val matchesFilter = selectedFilter == null || v.status == selectedFilter
        matchesQuery && matchesFilter
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateClick,
                containerColor = BrandBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Voucher")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Search field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Search by code or package...") },
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

            // Action row: Bulk generate & Print All
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onBulkClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                    modifier = Modifier.weight(1f).height(40.dp)
                ) {
                    Icon(Icons.Default.Layers, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Bulk Generate", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = onPrintAll,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).height(40.dp)
                ) {
                    Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Print Sheet (${filteredVouchers.size})", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { onFilterSelect(null) },
                    label = { Text("All (${vouchers.size})", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedFilter == VoucherStatus.ACTIVE,
                    onClick = { onFilterSelect(VoucherStatus.ACTIVE) },
                    label = { Text("Active", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedFilter == VoucherStatus.UNUSED,
                    onClick = { onFilterSelect(VoucherStatus.UNUSED) },
                    label = { Text("Unused", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedFilter == VoucherStatus.EXPIRED,
                    onClick = { onFilterSelect(VoucherStatus.EXPIRED) },
                    label = { Text("Expired", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedFilter == VoucherStatus.DISABLED,
                    onClick = { onFilterSelect(VoucherStatus.DISABLED) },
                    label = { Text("Disabled", fontSize = 11.sp) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Voucher List
            if (filteredVouchers.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.ConfirmationNumber,
                            contentDescription = null,
                            modifier = Modifier.size(54.dp),
                            tint = Slate400
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No vouchers found", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Slate500)
                        Text("Tap '+' or Bulk Generate to create vouchers.", fontSize = 12.sp, color = Slate400)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredVouchers, key = { it.id }) { voucher ->
                        VoucherListItem(
                            voucher = voucher,
                            onPrint = { onPrintVoucher(voucher) },
                            onToggleStatus = { onToggleStatus(voucher) },
                            onDelete = { onDeleteVoucher(voucher) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VoucherListItem(
    voucher: Voucher,
    onPrint: () -> Unit,
    onToggleStatus: () -> Unit,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Code & Status
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = voucher.code,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = BrandBlue
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                when (voucher.status) {
                                    VoucherStatus.ACTIVE -> StatusGreenLight
                                    VoucherStatus.UNUSED -> BrandBlueLight
                                    VoucherStatus.EXPIRED -> StatusAmberLight
                                    VoucherStatus.DISABLED -> StatusRedLight
                                }
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = voucher.status.name,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (voucher.status) {
                                VoucherStatus.ACTIVE -> StatusGreen
                                VoucherStatus.UNUSED -> BrandBlue
                                VoucherStatus.EXPIRED -> StatusAmber
                                VoucherStatus.DISABLED -> StatusRed
                            }
                        )
                    }
                }

                // Password preview
                Text(
                    text = "Pass: ${voucher.password}",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate700
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${voucher.packageName} • ${voucher.formattedDataLimit} • ${voucher.formattedTimeLimit}",
                    fontSize = 12.sp,
                    color = Slate600
                )
                Text(
                    text = voucher.formattedSpeed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Slate500
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Actions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPrint, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Print, contentDescription = "Print", tint = BrandBlue, modifier = Modifier.size(18.dp))
                }

                IconButton(onClick = onToggleStatus, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = if (voucher.status == VoucherStatus.DISABLED) Icons.Default.CheckCircle else Icons.Default.Block,
                        contentDescription = "Toggle",
                        tint = if (voucher.status == VoucherStatus.DISABLED) StatusGreen else StatusAmber,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = StatusRed, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
