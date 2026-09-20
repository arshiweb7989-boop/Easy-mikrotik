package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IspProfile
import com.example.data.model.LoginPageConfig
import com.example.ui.components.HotspotLoginPreview
import com.example.ui.components.parseHexColor
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IspProfileScreen(
    profile: IspProfile,
    loginPageConfig: LoginPageConfig,
    onSaveProfile: (IspProfile) -> Unit,
    onSaveLoginPageConfig: (LoginPageConfig) -> Unit,
    onApplyToRouter: () -> Unit
) {
    var activeSubTab by remember { mutableIntStateOf(0) } // 0: ISP Profile, 1: Hotspot Login Page

    // Local states for Profile
    var ispName by remember(profile) { mutableStateOf(profile.ispName) }
    var logoText by remember(profile) { mutableStateOf(profile.logoText) }
    var phone by remember(profile) { mutableStateOf(profile.phoneNumber) }
    var whatsapp by remember(profile) { mutableStateOf(profile.whatsappNumber) }
    var address by remember(profile) { mutableStateOf(profile.address) }
    var email by remember(profile) { mutableStateOf(profile.email) }
    var website by remember(profile) { mutableStateOf(profile.website) }
    var footerText by remember(profile) { mutableStateOf(profile.footerText) }

    // Local states for Login Page Designer
    var welcomeTitle by remember(loginPageConfig) { mutableStateOf(loginPageConfig.welcomeTitle) }
    var announcement by remember(loginPageConfig) { mutableStateOf(loginPageConfig.announcement) }
    var helpline by remember(loginPageConfig) { mutableStateOf(loginPageConfig.helpline) }
    var termsText by remember(loginPageConfig) { mutableStateOf(loginPageConfig.termsText) }
    var footerMessage by remember(loginPageConfig) { mutableStateOf(loginPageConfig.footerMessage) }
    var primaryColorHex by remember(loginPageConfig) { mutableStateOf(loginPageConfig.primaryColorHex) }
    var backgroundColorHex by remember(loginPageConfig) { mutableStateOf(loginPageConfig.backgroundColorHex) }
    var borderRadiusDp by remember(loginPageConfig) { mutableIntStateOf(loginPageConfig.borderRadiusDp) }
    var fontSizeSp by remember(loginPageConfig) { mutableIntStateOf(loginPageConfig.fontSizeSp) }
    var showLogo by remember(loginPageConfig) { mutableStateOf(loginPageConfig.showLogo) }
    var showSocials by remember(loginPageConfig) { mutableStateOf(loginPageConfig.showSocialLinks) }

    val currentPreviewConfig = LoginPageConfig(
        welcomeTitle = welcomeTitle,
        announcement = announcement,
        helpline = helpline,
        termsText = termsText,
        footerMessage = footerMessage,
        primaryColorHex = primaryColorHex,
        backgroundColorHex = backgroundColorHex,
        buttonColorHex = primaryColorHex,
        borderRadiusDp = borderRadiusDp,
        fontSizeSp = fontSizeSp,
        showLogo = showLogo,
        showSocialLinks = showSocials
    )

    val currentPreviewProfile = IspProfile(
        ispName = ispName,
        logoText = logoText,
        phoneNumber = phone,
        whatsappNumber = whatsapp,
        address = address,
        email = email,
        website = website,
        footerText = footerText
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Sub-Tabs Header
        TabRow(
            selectedTabIndex = activeSubTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = BrandBlue
        ) {
            Tab(
                selected = activeSubTab == 0,
                onClick = { activeSubTab = 0 },
                text = { Text("ISP Profile", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Business, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = activeSubTab == 1,
                onClick = { activeSubTab = 1 },
                text = { Text("Hotspot Login Page", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (activeSubTab == 0) {
            // TAB 1: ISP PROFILE FORM
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "ISP Profile Information",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Saved details automatically brand all generated vouchers and login pages.",
                    fontSize = 12.sp,
                    color = Slate500
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = logoText,
                        onValueChange = { logoText = it },
                        label = { Text("Logo / Icon") },
                        placeholder = { Text("⚡ or 📶") },
                        modifier = Modifier.width(100.dp)
                    )
                    OutlinedTextField(
                        value = ispName,
                        onValueChange = { ispName = it },
                        label = { Text("ISP Name") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = whatsapp,
                        onValueChange = { whatsapp = it },
                        label = { Text("WhatsApp Number") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address / Coverage Area") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Support Email") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = website,
                        onValueChange = { website = it },
                        label = { Text("Website") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = footerText,
                    onValueChange = { footerText = it },
                    label = { Text("Voucher Footer Notice") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onSaveProfile(
                            currentPreviewProfile
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save ISP Profile", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        } else {
            // TAB 2: HOTSPOT LOGIN PAGE DESIGNER
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Hotspot Login Page",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Controls captive portal displayed to connecting Wi-Fi users.",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }

                    Button(
                        onClick = onApplyToRouter,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Apply to Router", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // LIVE PREVIEW ACCORDION / BOX
                Text("Live Preview (Mobile Captive Portal):", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Slate600)
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Slate100)
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    HotspotLoginPreview(
                        profile = currentPreviewProfile,
                        config = currentPreviewConfig
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Appearance Customizer
                Text("Appearance & Colors:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(6.dp))

                // Theme color palettes
                val colorPresets = listOf(
                    "#0284C7" to "Brand Blue",
                    "#0D9488" to "Teal",
                    "#4F46E5" to "Indigo",
                    "#E11D48" to "Ruby",
                    "#16A34A" to "Green",
                    "#0F172A" to "Dark Slate"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    colorPresets.forEach { (hex, _) ->
                        val isSelected = primaryColorHex.equals(hex, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(parseHexColor(hex))
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) Color.Black else Slate200,
                                    shape = CircleShape
                                )
                                .clickable { primaryColorHex = hex }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = primaryColorHex,
                        onValueChange = { primaryColorHex = it },
                        label = { Text("Primary / Button Color") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = backgroundColorHex,
                        onValueChange = { backgroundColorHex = it },
                        label = { Text("Background Color") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Show ISP Logo", fontSize = 13.sp)
                    Switch(checked = showLogo, onCheckedChange = { showLogo = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Show Social Links", fontSize = 13.sp)
                    Switch(checked = showSocials, onCheckedChange = { showSocials = it })
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Content Customizer
                Text("Login Page Content:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = welcomeTitle,
                    onValueChange = { welcomeTitle = it },
                    label = { Text("Welcome Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = announcement,
                    onValueChange = { announcement = it },
                    label = { Text("Announcement / Instructions") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = helpline,
                    onValueChange = { helpline = it },
                    label = { Text("Help Line / WhatsApp Notice") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = termsText,
                    onValueChange = { termsText = it },
                    label = { Text("Terms & Conditions Text") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = footerMessage,
                    onValueChange = { footerMessage = it },
                    label = { Text("Footer Message") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onSaveLoginPageConfig(currentPreviewConfig)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Login Page Design", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
