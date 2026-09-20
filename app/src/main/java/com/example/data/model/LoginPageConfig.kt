package com.example.data.model

data class LoginPageConfig(
    val welcomeTitle: String = "Welcome to UltraNet WiFi",
    val announcement: String = "High-speed internet is available. Please enter your voucher code below.",
    val helpline: String = "Need assistance? WhatsApp: +1 (555) 345-6789",
    val termsText: String = "By logging in, you agree to our Acceptable Use Policy.",
    val footerMessage: String = "Thank you for connecting with UltraNet Hotspot!",
    val primaryColorHex: String = "#0284C7", // Sky blue
    val backgroundColorHex: String = "#F1F5F9", // Slate 100
    val cardBackgroundHex: String = "#FFFFFF",
    val textColorHex: String = "#0F172A",
    val buttonColorHex: String = "#0284C7",
    val buttonTextColorHex: String = "#FFFFFF",
    val borderRadiusDp: Int = 16,
    val fontSizeSp: Int = 15,
    val showLogo: Boolean = true,
    val showSocialLinks: Boolean = true,
    val socialLinkText: String = "ultranet.local | FB: @ultranethotspot"
)
