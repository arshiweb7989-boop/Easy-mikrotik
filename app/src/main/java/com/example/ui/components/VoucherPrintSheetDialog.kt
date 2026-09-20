package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.IspProfile
import com.example.data.model.Voucher
import com.example.ui.theme.*

enum class PrintLayoutMode {
    SINGLE_CARD,
    THERMAL_SLIP,
    A4_GRID
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoucherPrintSheetDialog(
    vouchers: List<Voucher>,
    ispProfile: IspProfile,
    onDismiss: () -> Unit,
    onPrint: () -> Unit,
    onSharePdf: () -> Unit
) {
    var layoutMode by remember { mutableStateOf(PrintLayoutMode.SINGLE_CARD) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Voucher Print & Export",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Auto-branded with ${ispProfile.ispName}",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate500)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Layout switcher tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = layoutMode == PrintLayoutMode.SINGLE_CARD,
                        onClick = { layoutMode = PrintLayoutMode.SINGLE_CARD },
                        label = { Text("Card") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = layoutMode == PrintLayoutMode.THERMAL_SLIP,
                        onClick = { layoutMode = PrintLayoutMode.THERMAL_SLIP },
                        label = { Text("Thermal") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = layoutMode == PrintLayoutMode.A4_GRID,
                        onClick = { layoutMode = PrintLayoutMode.A4_GRID },
                        label = { Text("Sheet (${vouchers.size})") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Preview Area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Slate100)
                        .padding(10.dp)
                ) {
                    when (layoutMode) {
                        PrintLayoutMode.SINGLE_CARD -> {
                            val v = vouchers.firstOrNull()
                            if (v != null) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    SingleVoucherCard(voucher = v, ispProfile = ispProfile)
                                }
                            }
                        }
                        PrintLayoutMode.THERMAL_SLIP -> {
                            val v = vouchers.firstOrNull()
                            if (v != null) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ThermalReceiptSlip(voucher = v, ispProfile = ispProfile)
                                }
                            }
                        }
                        PrintLayoutMode.A4_GRID -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(vouchers) { voucher ->
                                    CompactVoucherCard(voucher = voucher, ispProfile = ispProfile)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onSharePdf,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export PDF", fontSize = 13.sp)
                    }

                    Button(
                        onClick = onPrint,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Print Now", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SingleVoucherCard(voucher: Voucher, ispProfile: IspProfile) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .border(1.dp, Slate200, RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with ISP branding
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(BrandBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = ispProfile.logoText, fontSize = 18.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = ispProfile.ispName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Slate900
                        )
                        Text(
                            text = "Hotspot Access Voucher",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(BrandTealLight)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = voucher.packageName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandTeal
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Slate200)

            // Credentials Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Slate50)
                    .border(1.dp, Slate200, RoundedCornerShape(10.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("USERNAME", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Slate500)
                    Text(
                        text = voucher.code,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = BrandBlue
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("PASSWORD", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Slate500)
                    Text(
                        text = voucher.password,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = Slate900
                    )
                }

                // QR code representation
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(1.dp, Slate200, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = "QR Code",
                        modifier = Modifier.size(56.dp),
                        tint = Slate900
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Limits row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Data Limit", fontSize = 10.sp, color = Slate500)
                    Text(voucher.formattedDataLimit, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Column {
                    Text("Time Limit", fontSize = 10.sp, color = Slate500)
                    Text(voucher.formattedTimeLimit, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Column {
                    Text("Speed", fontSize = 10.sp, color = Slate500)
                    Text(voucher.formattedSpeed, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                if (voucher.price > 0) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Price", fontSize = 10.sp, color = Slate500)
                        Text("$${voucher.price}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandBlue)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footer
            Text(
                text = "WhatsApp Help: ${ispProfile.whatsappNumber} • ${ispProfile.website}",
                fontSize = 10.sp,
                color = Slate500,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ThermalReceiptSlip(voucher: Voucher, ispProfile: IspProfile) {
    Card(
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .width(260.dp)
            .border(1.dp, Slate200, RoundedCornerShape(6.dp))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = ispProfile.ispName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(text = "HIGH-SPEED WIFI", fontSize = 11.sp, color = Slate500)
            Text(text = "--------------------------------", fontSize = 10.sp, color = Slate400)

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = voucher.packageName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BrandBlue)
            Spacer(modifier = Modifier.height(6.dp))

            Text("USER: ${voucher.code}", fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            Text("PASS: ${voucher.password}", fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)

            Spacer(modifier = Modifier.height(6.dp))
            Icon(Icons.Default.QrCode2, contentDescription = null, modifier = Modifier.size(54.dp), tint = Slate800)

            Text("--------------------------------", fontSize = 10.sp, color = Slate400)
            Text("Data: ${voucher.formattedDataLimit} | Time: ${voucher.formattedTimeLimit}", fontSize = 10.sp)
            Text("Speed: ${voucher.formattedSpeed}", fontSize = 10.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Tel/WA: ${ispProfile.whatsappNumber}", fontSize = 10.sp, color = Slate600)
        }
    }
}

@Composable
fun CompactVoucherCard(voucher: Voucher, ispProfile: IspProfile) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Slate200, RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(ispProfile.ispName, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(voucher.packageName, fontSize = 9.sp, color = BrandBlue, fontWeight = FontWeight.SemiBold)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = Slate200)
            Text("Code: ${voucher.code}", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace)
            Text("Pass: ${voucher.password}", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(4.dp))
            Text("${voucher.formattedDataLimit} • ${voucher.formattedTimeLimit}", fontSize = 9.sp, color = Slate500)
        }
    }
}
