package com.example.data.model

enum class UserStatus {
    ACTIVE,
    OFFLINE,
    EXPIRED,
    DISABLED
}

data class HotspotUser(
    val id: String,
    val username: String,
    val password: String = "",
    val profile: String = "default",
    val status: UserStatus = UserStatus.OFFLINE,
    val bytesIn: Long = 0L,
    val bytesOut: Long = 0L,
    val limitBytesTotal: Long = 0L, // 0 = unlimited
    val uptime: String = "0s",
    val limitUptime: String = "0s",
    val disabled: Boolean = false,
    val comment: String = "",
    val macAddress: String? = null,
    val lastLogin: String = "Never",
    val speedLimit: String = "5M/2M"
) {
    val totalBytes: Long get() = bytesIn + bytesOut
    
    val formattedUsage: String
        get() = formatBytes(totalBytes)

    val remainingData: String
        get() = if (limitBytesTotal <= 0) "Unlimited"
        else {
            val remain = limitBytesTotal - totalBytes
            if (remain <= 0) "Exhausted" else formatBytes(remain)
        }

    companion object {
        fun formatBytes(bytes: Long): String {
            if (bytes <= 0) return "0 MB"
            val kb = bytes / 1024.0
            val mb = kb / 1024.0
            val gb = mb / 1024.0
            return when {
                gb >= 1.0 -> String.format("%.2f GB", gb)
                mb >= 1.0 -> String.format("%.1f MB", mb)
                else -> String.format("%.0f KB", kb)
            }
        }
    }
}

data class ActiveSession(
    val id: String,
    val username: String,
    val ipAddress: String,
    val macAddress: String,
    val uptime: String,
    val bytesIn: Long,
    val bytesOut: Long,
    val sessionTime: String,
    val loginBy: String = "http-chap"
) {
    val totalBytes: Long get() = bytesIn + bytesOut
    val formattedDownload: String get() = HotspotUser.formatBytes(bytesOut)
    val formattedUpload: String get() = HotspotUser.formatBytes(bytesIn)
}
