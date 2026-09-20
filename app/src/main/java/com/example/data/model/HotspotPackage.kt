package com.example.data.model

data class HotspotPackage(
    val id: String,
    val name: String,
    val dataLimitMb: Long = 1024, // 0 = unlimited
    val timeLimitMins: Long = 1440, // 0 = unlimited
    val downloadSpeedMbps: Double = 5.0,
    val uploadSpeedMbps: Double = 2.0,
    val sharedUsers: Int = 1,
    val price: Double = 5.0,
    val isDefault: Boolean = false
) {
    val rateLimitStr: String
        get() = "${uploadSpeedMbps.toInt()}M/${downloadSpeedMbps.toInt()}M"

    val formattedData: String
        get() = when {
            dataLimitMb <= 0 -> "Unlimited"
            dataLimitMb >= 1024 -> String.format("%.1f GB", dataLimitMb / 1024.0).replace(".0 GB", " GB")
            else -> "$dataLimitMb MB"
        }

    val formattedValidity: String
        get() = when {
            timeLimitMins <= 0 -> "Unlimited"
            timeLimitMins < 60 -> "$timeLimitMins Mins"
            timeLimitMins < 1440 -> "${timeLimitMins / 60} Hours"
            else -> "${timeLimitMins / 1440} Days"
        }
}
