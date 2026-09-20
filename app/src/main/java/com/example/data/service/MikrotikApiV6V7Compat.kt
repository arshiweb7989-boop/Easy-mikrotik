package com.example.data.service

import com.example.data.model.HotspotPackage
import com.example.data.model.IspProfile
import com.example.data.model.LoginPageConfig
import com.example.data.model.Voucher

object MikrotikApiV6V7Compat {

    fun isV7(versionString: String): Boolean {
        val trimmed = versionString.trim().removePrefix("v")
        return trimmed.startsWith("7")
    }

    // Format bytes for MikroTik limits
    fun formatBytesLimit(mb: Long): String {
        return if (mb <= 0) "0" else (mb * 1024L * 1024L).toString()
    }

    // Format uptime limit for MikroTik v6 & v7
    fun formatUptimeLimit(minutes: Long): String {
        if (minutes <= 0) return "0s"
        val days = minutes / 1440
        val remainingMinutes = minutes % 1440
        val hours = remainingMinutes / 60
        val mins = remainingMinutes % 60

        val builder = StringBuilder()
        if (days > 0) builder.append("${days}d")
        if (hours > 0) builder.append("${hours}h")
        if (mins > 0) builder.append("${mins}m")
        return if (builder.isEmpty()) "0s" else builder.toString()
    }

    // Format rate limit (upload/download) e.g., "2M/5M"
    fun formatRateLimit(uploadMbps: Double, downloadMbps: Double): String {
        val up = if (uploadMbps < 1.0) "${(uploadMbps * 1024).toInt()}k" else "${uploadMbps.toInt()}M"
        val down = if (downloadMbps < 1.0) "${(downloadMbps * 1024).toInt()}k" else "${downloadMbps.toInt()}M"
        return "$up/$down"
    }

    // Generate RouterOS command for adding a Hotspot User / Voucher
    fun buildAddUserCommand(voucher: Voucher, isV7: Boolean): Map<String, String> {
        val cmd = mutableMapOf<String, String>()
        cmd["name"] = voucher.code
        cmd["password"] = voucher.password
        cmd["profile"] = if (voucher.packageName.isNotBlank()) voucher.packageName else "default"
        
        if (voucher.dataLimitMb > 0) {
            // limit-bytes-total is supported across both v6 and v7
            cmd["limit-bytes-total"] = formatBytesLimit(voucher.dataLimitMb)
        }
        if (voucher.timeLimitMins > 0) {
            cmd["limit-uptime"] = formatUptimeLimit(voucher.timeLimitMins)
        }
        cmd["comment"] = "MikroEasy|${voucher.packageName}|${System.currentTimeMillis()}"
        return cmd
    }

    // Generate RouterOS command for adding a Hotspot Profile / Package
    fun buildAddProfileCommand(pkg: HotspotPackage, isV7: Boolean): Map<String, String> {
        val cmd = mutableMapOf<String, String>()
        cmd["name"] = pkg.name
        cmd["shared-users"] = pkg.sharedUsers.toString()
        cmd["rate-limit"] = formatRateLimit(pkg.uploadSpeedMbps, pkg.downloadSpeedMbps)
        cmd["keepalive-timeout"] = "2m"
        cmd["status-autorefresh"] = "1m"
        return cmd
    }

    // Generate complete, modern, mobile-responsive MikroTik Hotspot login.html
    fun generateHotspotLoginHtml(profile: IspProfile, config: LoginPageConfig): String {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
              <title>${profile.ispName} - Hotspot Login</title>
              <style>
                * { box-sizing: border-box; margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; }
                body {
                  background-color: ${config.backgroundColorHex};
                  color: ${config.textColorHex};
                  min-height: 100vh;
                  display: flex;
                  align-items: center;
                  justify-content: center;
                  padding: 16px;
                }
                .login-card {
                  background: ${config.cardBackgroundHex};
                  width: 100%;
                  max-width: 380px;
                  border-radius: ${config.borderRadiusDp}px;
                  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.05);
                  padding: 28px 24px;
                  border: 1px solid rgba(0,0,0,0.06);
                  text-align: center;
                }
                .logo-circle {
                  width: 54px;
                  height: 54px;
                  border-radius: 16px;
                  background: ${config.primaryColorHex};
                  color: #ffffff;
                  display: inline-flex;
                  align-items: center;
                  justify-content: center;
                  font-size: 26px;
                  margin-bottom: 12px;
                }
                h1 { font-size: 20px; font-weight: 700; margin-bottom: 6px; }
                p.announcement { font-size: 13px; color: #64748B; margin-bottom: 20px; line-height: 1.4; }
                .input-group { margin-bottom: 14px; text-align: left; }
                label { display: block; font-size: 12px; font-weight: 600; margin-bottom: 5px; color: #475569; }
                input[type="text"], input[type="password"] {
                  width: 100%;
                  padding: 12px 14px;
                  border-radius: 10px;
                  border: 1px solid #CBD5E1;
                  font-size: ${config.fontSizeSp}px;
                  outline: none;
                  transition: border-color 0.2s;
                }
                input:focus { border-color: ${config.primaryColorHex}; ring: 2px ${config.primaryColorHex}; }
                button[type="submit"] {
                  width: 100%;
                  background: ${config.buttonColorHex};
                  color: ${config.buttonTextColorHex};
                  font-weight: 600;
                  padding: 13px;
                  border-radius: ${config.borderRadiusDp / 1.5}px;
                  border: none;
                  font-size: 15px;
                  cursor: pointer;
                  margin-top: 8px;
                  transition: opacity 0.2s;
                }
                button:hover { opacity: 0.92; }
                .helpline {
                  margin-top: 20px;
                  font-size: 12px;
                  color: #0284C7;
                  font-weight: 500;
                }
                .terms {
                  margin-top: 14px;
                  font-size: 11px;
                  color: #94A3B8;
                  line-height: 1.3;
                }
                .footer {
                  margin-top: 16px;
                  font-size: 11px;
                  color: #64748B;
                  border-top: 1px dashed #E2E8F0;
                  padding-top: 12px;
                }
              </style>
            </head>
            <body>
              <div class="login-card">
                ${if (config.showLogo) """<div class="logo-circle">${profile.logoText}</div>""" else ""}
                <h1>${config.welcomeTitle}</h1>
                <p class="announcement">${config.announcement}</p>

                <form name="login" action="${"$"}(link-login-only)" method="post">
                  <input type="hidden" name="dst" value="${"$"}(link-orig)">
                  <input type="hidden" name="popup" value="true">

                  <div class="input-group">
                    <label>Voucher Username / Code</label>
                    <input type="text" name="username" placeholder="e.g. ISP-1024" required autofocus>
                  </div>

                  <div class="input-group">
                    <label>Password</label>
                    <input type="password" name="password" placeholder="Enter password" required>
                  </div>

                  <button type="submit">CONNECT TO WIFI</button>
                </form>

                <div class="helpline">${config.helpline}</div>
                <div class="terms">${config.termsText}</div>
                <div class="footer">${config.footerMessage}</div>
              </div>
            </body>
            </html>
        """.trimIndent()
    }
}
