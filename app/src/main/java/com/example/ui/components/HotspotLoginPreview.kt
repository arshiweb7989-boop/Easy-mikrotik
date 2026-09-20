package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IspProfile
import com.example.data.model.LoginPageConfig
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800

fun parseHexColor(hex: String, fallback: Color = Color.Black): Color {
    return try {
        val clean = hex.removePrefix("#")
        val full = if (clean.length == 6) "FF$clean" else clean
        Color(full.toLong(16))
    } catch (e: Exception) {
        fallback
    }
}

@Composable
fun HotspotLoginPreview(
    profile: IspProfile,
    config: LoginPageConfig,
    modifier: Modifier = Modifier
) {
    val bgColor = parseHexColor(config.backgroundColorHex, Color(0xFFF1F5F9))
    val cardColor = parseHexColor(config.cardBackgroundHex, Color.White)
    val primaryColor = parseHexColor(config.primaryColorHex, Color(0xFF0284C7))
    val textColor = parseHexColor(config.textColorHex, Color(0xFF0F172A))
    val buttonColor = parseHexColor(config.buttonColorHex, primaryColor)
    val buttonTextColor = parseHexColor(config.buttonTextColorHex, Color.White)

    // Simulated Phone Mockup frame
    Box(
        modifier = modifier
            .width(320.dp)
            .clip(RoundedCornerShape(28.dp))
            .border(3.dp, Slate800, RoundedCornerShape(28.dp))
            .background(bgColor)
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Simulated Phone Status Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("9:41", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate500)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Wifi, contentDescription = null, modifier = Modifier.size(12.dp), tint = Slate500)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("100%", fontSize = 9.sp, color = Slate500)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Captive Portal Login Card
            Card(
                shape = RoundedCornerShape(config.borderRadiusDp.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (config.showLogo) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(primaryColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = profile.logoText,
                                fontSize = 22.sp,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Text(
                        text = config.welcomeTitle,
                        fontSize = (config.fontSizeSp + 2).sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = config.announcement,
                        fontSize = (config.fontSizeSp - 3).sp,
                        color = Slate500,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Username Input mock
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Slate200, RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(horizontal = 10.dp, vertical = 9.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = Slate400)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Voucher Code", fontSize = 12.sp, color = Slate400)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Password Input mock
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Slate200, RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(horizontal = 10.dp, vertical = 9.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp), tint = Slate400)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Password", fontSize = 12.sp, color = Slate400)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Login Button Mock
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape((config.borderRadiusDp / 2).dp))
                            .background(buttonColor)
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "LOGIN",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = buttonTextColor
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Helpline
                    Text(
                        text = config.helpline,
                        fontSize = 10.sp,
                        color = primaryColor,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Terms
                    Text(
                        text = config.termsText,
                        fontSize = 9.sp,
                        color = Slate400,
                        textAlign = TextAlign.Center
                    )

                    if (config.showSocialLinks) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = config.socialLinkText,
                            fontSize = 9.sp,
                            color = Slate500,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = config.footerMessage,
                        fontSize = 9.sp,
                        color = Slate400,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
