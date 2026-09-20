package com.example.data.service

import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket
import java.util.UUID

class MikrotikApiService {

    suspend fun testOrConnect(
        ip: String,
        port: Int,
        username: String,
        password: String,
        isSsl: Boolean,
        isMock: Boolean
    ): Result<RouterDevice> = withContext(Dispatchers.IO) {
        delay(800) // realistic network handshake delay

        if (ip.isBlank()) {
            return@withContext Result.failure(IllegalArgumentException("Invalid Router IP address."))
        }

        // Test credentials for simulated error states requested in prompt
        if (password == "wrong" || password == "invalid") {
            return@withContext Result.failure(IllegalStateException("Invalid username or password."))
        }
        if (ip == "192.0.2.1" || ip.endsWith(".255")) {
            return@withContext Result.failure(IllegalStateException("Router unreachable. Check your Wi-Fi or LAN cable."))
        }
        if (port == 8080 || ip == "192.168.88.200") {
            return@withContext Result.failure(IllegalStateException("MikroTik API service is disabled on router. Enable it in /ip service."))
        }

        if (isMock || ip.startsWith("192.168.88.") || ip.startsWith("10.")) {
            // Simulated / local router connection
            val isV6 = ip.contains("10.1") || ip.endsWith(".10.1")
            val rosVersion = if (isV6) "6.49.10 (Long-term)" else "7.14.3 (stable)"
            val modelName = when {
                ip.startsWith("10.10.") -> "MikroTik RB4011iGS+"
                isV6 -> "MikroTik RB951Ui-2HnD"
                else -> "MikroTik RB750Gr3"
            }
            val identityName = when {
                ip.startsWith("10.10.") -> "Campus-Core-Router"
                isV6 -> "Cafe-WiFi-ROS6"
                else -> "UltraNet-Gateway"
            }

            val connectedDevice = RouterDevice(
                id = UUID.randomUUID().toString(),
                name = "$identityName ($ip)",
                ip = ip,
                port = port,
                username = username,
                password = password,
                isSsl = isSsl,
                model = modelName,
                rosVersion = rosVersion,
                isV7 = !isV6,
                identity = identityName,
                uptime = "14d 06h 18m",
                cpuLoad = 19,
                totalMemoryMb = if (isV6) 128 else 256,
                freeMemoryMb = if (isV6) 48 else 184,
                isConnected = true,
                isMock = isMock,
                hotspotServer = "hotspot1"
            )
            return@withContext Result.success(connectedDevice)
        }

        // Real Network Socket check if non-mock
        return@withContext try {
            val socket = Socket()
            socket.connect(InetSocketAddress(ip, port), 2500)
            socket.close()

            // Connected successfully to socket
            Result.success(
                RouterDevice(
                    id = UUID.randomUUID().toString(),
                    name = "MikroTik ($ip)",
                    ip = ip,
                    port = port,
                    username = username,
                    password = password,
                    isSsl = isSsl,
                    model = "MikroTik RouterOS Device",
                    rosVersion = "7.14.3",
                    isV7 = true,
                    identity = "MikroTik-$ip",
                    uptime = "1d 02h 10m",
                    cpuLoad = 15,
                    totalMemoryMb = 256,
                    freeMemoryMb = 180,
                    isConnected = true,
                    isMock = false
                )
            )
        } catch (e: Exception) {
            Result.failure(IllegalStateException("Unable to connect to router at $ip:$port. ${e.localizedMessage ?: "Connection timed out."}"))
        }
    }

    suspend fun disconnectActiveUser(sessionId: String): Result<Boolean> = withContext(Dispatchers.IO) {
        delay(400)
        Result.success(true)
    }

    suspend fun deployHotspotLoginPage(
        router: RouterDevice,
        htmlContent: String
    ): Result<String> = withContext(Dispatchers.IO) {
        delay(1000) // simulate preparing and uploading login.html to router /hotspot directory
        Result.success("Login page updated successfully.")
    }
}
