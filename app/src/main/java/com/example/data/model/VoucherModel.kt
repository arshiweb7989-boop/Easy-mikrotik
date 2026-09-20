package com.example.data.model

enum class VoucherStatus {
    UNUSED,
    ACTIVE,
    EXPIRED,
    DISABLED
}

data class Voucher(
    val id: String,
    val code: String,
    val password: String,
    val packageName: String,
    val dataLimitMb: Long = 0, // 0 = unlimited
    val timeLimitMins: Long = 0, // 0 = unlimited
    val downloadSpeedMbps: Double = 5.0,
    val uploadSpeedMbps: Double = 2.0,
    val price: Double = 0.0,
    val status: VoucherStatus = VoucherStatus.UNUSED,
    val createdAt: Long = System.currentTimeMillis(),
    val usedAt: Long? = null,
    val expiresAt: Long? = null,
    val usedBytes: Long = 0L,
    val macAddress: String? = null
) {
    val formattedDataLimit: String
        get() = when {
            dataLimitMb <= 0 -> "Unlimited"
            dataLimitMb >= 1024 -> String.format("%.1f GB", dataLimitMb / 1024.0).replace(".0 GB", " GB")
            else -> "$dataLimitMb MB"
        }

    val formattedTimeLimit: String
        get() = when {
            timeLimitMins <= 0 -> "Unlimited"
            timeLimitMins < 60 -> "$timeLimitMins Mins"
            timeLimitMins < 1440 -> "${timeLimitMins / 60} Hours"
            else -> "${timeLimitMins / 1440} Days"
        }

    val formattedSpeed: String
        get() = "${uploadSpeedMbps.toInt()}M / ${downloadSpeedMbps.toInt()}M"
}

data class BulkGenConfig(
    val quantity: Int = 10,
    val prefix: String = "ISP",
    val userLength: Int = 4,
    val passLength: Int = 4,
    val packageId: String = "",
    val packageName: String = "1GB Daily",
    val dataLimitMb: Long = 1024,
    val timeLimitMins: Long = 1440,
    val downloadSpeedMbps: Double = 5.0,
    val uploadSpeedMbps: Double = 2.0,
    val price: Double = 5.0,
    val isNumbersOnly: Boolean = true
)
