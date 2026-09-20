package com.example.data.model

data class RouterDevice(
    val id: String,
    val name: String,
    val ip: String,
    val port: Int = 8728,
    val username: String = "admin",
    val password: String = "",
    val isSsl: Boolean = false,
    val model: String = "MikroTik RB750Gr3",
    val rosVersion: String = "7.14.3",
    val isV7: Boolean = true,
    val identity: String = "MikroTik-Main",
    val uptime: String = "12d 04h 32m",
    val cpuLoad: Int = 18,
    val totalMemoryMb: Long = 256,
    val freeMemoryMb: Long = 184,
    val isConnected: Boolean = false,
    val isMock: Boolean = false,
    val hotspotServer: String = "hotspot1",
    val lastConnected: Long = System.currentTimeMillis()
)

data class DiscoveryResult(
    val ip: String,
    val mac: String,
    val identity: String,
    val model: String,
    val version: String,
    val isMikrotik: Boolean = true
)

sealed class ConnectionStatus {
    object Disconnected : ConnectionStatus()
    object Connecting : ConnectionStatus()
    data class Connected(val router: RouterDevice) : ConnectionStatus()
    data class Error(val message: String, val technicalDetail: String? = null) : ConnectionStatus()
}

data class DashboardStats(
    val activeUsersCount: Int = 0,
    val totalHotspotUsersCount: Int = 0,
    val activeVouchersCount: Int = 0,
    val totalVouchersCount: Int = 0,
    val totalDownloadBytes: Long = 0L,
    val totalUploadBytes: Long = 0L,
    val routerCpuUsage: Int = 0,
    val routerMemoryUsagePercent: Int = 0
)
