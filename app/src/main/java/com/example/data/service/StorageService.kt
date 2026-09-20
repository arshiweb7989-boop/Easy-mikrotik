package com.example.data.service

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.example.data.model.*
import org.json.JSONArray
import org.json.JSONObject

class StorageService(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("mikroeasy_prefs", Context.MODE_PRIVATE)

    // Security: Simple XOR & Base64 obfuscation for stored router passwords
    private fun encrypt(value: String): String {
        if (value.isEmpty()) return ""
        val key = 0x5A
        val bytes = value.toByteArray(Charsets.UTF_8)
        val masked = ByteArray(bytes.size) { i -> (bytes[i].toInt() xor key).toByte() }
        return Base64.encodeToString(masked, Base64.NO_WRAP)
    }

    private fun decrypt(value: String): String {
        if (value.isEmpty()) return ""
        return try {
            val key = 0x5A
            val bytes = Base64.decode(value, Base64.NO_WRAP)
            val unmasked = ByteArray(bytes.size) { i -> (bytes[i].toInt() xor key).toByte() }
            String(unmasked, Charsets.UTF_8)
        } catch (e: Exception) {
            value
        }
    }

    fun saveIspProfile(profile: IspProfile) {
        val json = JSONObject().apply {
            put("ispName", profile.ispName)
            put("logoText", profile.logoText)
            put("phoneNumber", profile.phoneNumber)
            put("whatsappNumber", profile.whatsappNumber)
            put("address", profile.address)
            put("email", profile.email)
            put("website", profile.website)
            put("footerText", profile.footerText)
            put("currency", profile.currency)
        }
        prefs.edit().putString("isp_profile", json.toString()).apply()
    }

    fun loadIspProfile(): IspProfile {
        val raw = prefs.getString("isp_profile", null) ?: return IspProfile()
        return try {
            val obj = JSONObject(raw)
            IspProfile(
                ispName = obj.optString("ispName", "UltraNet Hotspot"),
                logoText = obj.optString("logoText", "⚡"),
                phoneNumber = obj.optString("phoneNumber", "+1 (555) 345-6789"),
                whatsappNumber = obj.optString("whatsappNumber", "+1 (555) 345-6789"),
                address = obj.optString("address", "742 Evergreen Terrace, Suite 100"),
                email = obj.optString("email", "support@ultranet.local"),
                website = obj.optString("website", "https://ultranet.local"),
                footerText = obj.optString("footerText", "Powered by MikroEasy Hotspot Manager."),
                currency = obj.optString("currency", "$")
            )
        } catch (e: Exception) {
            IspProfile()
        }
    }

    fun saveLoginPageConfig(config: LoginPageConfig) {
        val json = JSONObject().apply {
            put("welcomeTitle", config.welcomeTitle)
            put("announcement", config.announcement)
            put("helpline", config.helpline)
            put("termsText", config.termsText)
            put("footerMessage", config.footerMessage)
            put("primaryColorHex", config.primaryColorHex)
            put("backgroundColorHex", config.backgroundColorHex)
            put("cardBackgroundHex", config.cardBackgroundHex)
            put("textColorHex", config.textColorHex)
            put("buttonColorHex", config.buttonColorHex)
            put("buttonTextColorHex", config.buttonTextColorHex)
            put("borderRadiusDp", config.borderRadiusDp)
            put("fontSizeSp", config.fontSizeSp)
            put("showLogo", config.showLogo)
            put("showSocialLinks", config.showSocialLinks)
            put("socialLinkText", config.socialLinkText)
        }
        prefs.edit().putString("login_page_config", json.toString()).apply()
    }

    fun loadLoginPageConfig(): LoginPageConfig {
        val raw = prefs.getString("login_page_config", null) ?: return LoginPageConfig()
        return try {
            val obj = JSONObject(raw)
            LoginPageConfig(
                welcomeTitle = obj.optString("welcomeTitle", "Welcome to UltraNet WiFi"),
                announcement = obj.optString("announcement", "High-speed internet is available."),
                helpline = obj.optString("helpline", "Need assistance? WhatsApp: +1 (555) 345-6789"),
                termsText = obj.optString("termsText", "By logging in, you agree to our Acceptable Use Policy."),
                footerMessage = obj.optString("footerMessage", "Thank you for connecting with UltraNet!"),
                primaryColorHex = obj.optString("primaryColorHex", "#0284C7"),
                backgroundColorHex = obj.optString("backgroundColorHex", "#F1F5F9"),
                cardBackgroundHex = obj.optString("cardBackgroundHex", "#FFFFFF"),
                textColorHex = obj.optString("textColorHex", "#0F172A"),
                buttonColorHex = obj.optString("buttonColorHex", "#0284C7"),
                buttonTextColorHex = obj.optString("buttonTextColorHex", "#FFFFFF"),
                borderRadiusDp = obj.optInt("borderRadiusDp", 16),
                fontSizeSp = obj.optInt("fontSizeSp", 15),
                showLogo = obj.optBoolean("showLogo", true),
                showSocialLinks = obj.optBoolean("showSocialLinks", true),
                socialLinkText = obj.optString("socialLinkText", "ultranet.local | FB: @ultranethotspot")
            )
        } catch (e: Exception) {
            LoginPageConfig()
        }
    }

    fun saveRouters(routers: List<RouterDevice>) {
        val arr = JSONArray()
        routers.forEach { r ->
            val obj = JSONObject().apply {
                put("id", r.id)
                put("name", r.name)
                put("ip", r.ip)
                put("port", r.port)
                put("username", r.username)
                put("encryptedPassword", encrypt(r.password))
                put("isSsl", r.isSsl)
                put("model", r.model)
                put("rosVersion", r.rosVersion)
                put("isV7", r.isV7)
                put("identity", r.identity)
                put("uptime", r.uptime)
                put("cpuLoad", r.cpuLoad)
                put("totalMemoryMb", r.totalMemoryMb)
                put("freeMemoryMb", r.freeMemoryMb)
                put("isMock", r.isMock)
                put("hotspotServer", r.hotspotServer)
            }
            arr.put(obj)
        }
        prefs.edit().putString("saved_routers", arr.toString()).apply()
    }

    fun loadRouters(): List<RouterDevice> {
        val raw = prefs.getString("saved_routers", null)
        if (raw.isNullOrBlank()) {
            return MockRouterEngine.sampleRouters
        }
        return try {
            val arr = JSONArray(raw)
            val list = mutableListOf<RouterDevice>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    RouterDevice(
                        id = obj.optString("id", "r-$i"),
                        name = obj.optString("name", "MikroTik Router"),
                        ip = obj.optString("ip", "192.168.88.1"),
                        port = obj.optInt("port", 8728),
                        username = obj.optString("username", "admin"),
                        password = decrypt(obj.optString("encryptedPassword", "")),
                        isSsl = obj.optBoolean("isSsl", false),
                        model = obj.optString("model", "MikroTik RB750Gr3"),
                        rosVersion = obj.optString("rosVersion", "7.14.3"),
                        isV7 = obj.optBoolean("isV7", true),
                        identity = obj.optString("identity", "MikroTik-Main"),
                        uptime = obj.optString("uptime", "14d 02h"),
                        cpuLoad = obj.optInt("cpuLoad", 20),
                        totalMemoryMb = obj.optLong("totalMemoryMb", 256),
                        freeMemoryMb = obj.optLong("freeMemoryMb", 180),
                        isConnected = false,
                        isMock = obj.optBoolean("isMock", false),
                        hotspotServer = obj.optString("hotspotServer", "hotspot1")
                    )
                )
            }
            if (list.isEmpty()) MockRouterEngine.sampleRouters else list
        } catch (e: Exception) {
            MockRouterEngine.sampleRouters
        }
    }
}
