package com.example.data.service

import com.example.data.model.*
import java.util.UUID

object MockRouterEngine {

    val sampleRouters = listOf(
        RouterDevice(
            id = "mock-rb750gr3",
            name = "Main Gateway (RB750Gr3)",
            ip = "192.168.88.1",
            port = 8728,
            username = "admin",
            password = "",
            isSsl = false,
            model = "MikroTik RB750Gr3 (hEX)",
            rosVersion = "7.14.3",
            isV7 = true,
            identity = "UltraNet-Gateway",
            uptime = "18d 07h 42m",
            cpuLoad = 24,
            totalMemoryMb = 256,
            freeMemoryMb = 178,
            isConnected = true,
            isMock = true,
            hotspotServer = "hotspot1"
        ),
        RouterDevice(
            id = "mock-rb4011",
            name = "Branch Campus (RB4011)",
            ip = "10.10.1.1",
            port = 8729,
            username = "admin",
            password = "",
            isSsl = true,
            model = "MikroTik RB4011iGS+",
            rosVersion = "7.12.1",
            isV7 = true,
            identity = "Campus-Core-Router",
            uptime = "45d 12h 10m",
            cpuLoad = 12,
            totalMemoryMb = 1024,
            freeMemoryMb = 840,
            isConnected = false,
            isMock = true,
            hotspotServer = "hs-campus"
        ),
        RouterDevice(
            id = "mock-rb951-v6",
            name = "Legacy Cafe Router (v6)",
            ip = "192.168.10.1",
            port = 8728,
            username = "admin",
            password = "",
            isSsl = false,
            model = "MikroTik RB951Ui-2HnD",
            rosVersion = "6.49.10 (Long-term)",
            isV7 = false,
            identity = "Cafe-WiFi-ROS6",
            uptime = "89d 22h 05m",
            cpuLoad = 38,
            totalMemoryMb = 128,
            freeMemoryMb = 48,
            isConnected = false,
            isMock = true,
            hotspotServer = "hotspot-cafe"
        )
    )

    val samplePackages = listOf(
        HotspotPackage(
            id = "pkg-1",
            name = "1GB Daily",
            dataLimitMb = 1024,
            timeLimitMins = 1440,
            downloadSpeedMbps = 5.0,
            uploadSpeedMbps = 2.0,
            price = 2.0,
            isDefault = true
        ),
        HotspotPackage(
            id = "pkg-2",
            name = "5GB Weekly",
            dataLimitMb = 5120,
            timeLimitMins = 10080,
            downloadSpeedMbps = 8.0,
            uploadSpeedMbps = 4.0,
            price = 8.0
        ),
        HotspotPackage(
            id = "pkg-3",
            name = "Monthly Unlimited",
            dataLimitMb = 0,
            timeLimitMins = 43200,
            downloadSpeedMbps = 15.0,
            uploadSpeedMbps = 5.0,
            price = 25.0
        ),
        HotspotPackage(
            id = "pkg-4",
            name = "Quick 2 Hours (500MB)",
            dataLimitMb = 500,
            timeLimitMins = 120,
            downloadSpeedMbps = 3.0,
            uploadSpeedMbps = 1.0,
            price = 1.0
        )
    )

    fun createInitialVouchers(): MutableList<Voucher> {
        val now = System.currentTimeMillis()
        return mutableListOf(
            Voucher(
                id = UUID.randomUUID().toString(),
                code = "ISP-8821",
                password = "729",
                packageName = "1GB Daily",
                dataLimitMb = 1024,
                timeLimitMins = 1440,
                downloadSpeedMbps = 5.0,
                uploadSpeedMbps = 2.0,
                price = 2.0,
                status = VoucherStatus.ACTIVE,
                createdAt = now - 3600_000L * 4,
                usedAt = now - 3600_000L * 3,
                usedBytes = 412_000_000L,
                macAddress = "D4:61:9D:41:8A:12"
            ),
            Voucher(
                id = UUID.randomUUID().toString(),
                code = "ISP-8822",
                password = "441",
                packageName = "5GB Weekly",
                dataLimitMb = 5120,
                timeLimitMins = 10080,
                downloadSpeedMbps = 8.0,
                uploadSpeedMbps = 4.0,
                price = 8.0,
                status = VoucherStatus.ACTIVE,
                createdAt = now - 3600_000L * 24,
                usedAt = now - 3600_000L * 20,
                usedBytes = 1_850_000_000L,
                macAddress = "AC:87:A3:99:1C:FE"
            ),
            Voucher(
                id = UUID.randomUUID().toString(),
                code = "ISP-8823",
                password = "908",
                packageName = "1GB Daily",
                dataLimitMb = 1024,
                timeLimitMins = 1440,
                downloadSpeedMbps = 5.0,
                uploadSpeedMbps = 2.0,
                price = 2.0,
                status = VoucherStatus.UNUSED,
                createdAt = now - 3600_000L * 2
            ),
            Voucher(
                id = UUID.randomUUID().toString(),
                code = "ISP-8824",
                password = "635",
                packageName = "5GB Weekly",
                dataLimitMb = 5120,
                timeLimitMins = 10080,
                downloadSpeedMbps = 8.0,
                uploadSpeedMbps = 4.0,
                price = 8.0,
                status = VoucherStatus.UNUSED,
                createdAt = now - 3600_000L * 1
            ),
            Voucher(
                id = UUID.randomUUID().toString(),
                code = "ISP-8825",
                password = "319",
                packageName = "Monthly Unlimited",
                dataLimitMb = 0,
                timeLimitMins = 43200,
                downloadSpeedMbps = 15.0,
                uploadSpeedMbps = 5.0,
                price = 25.0,
                status = VoucherStatus.UNUSED,
                createdAt = now - 3600_000L * 5
            ),
            Voucher(
                id = UUID.randomUUID().toString(),
                code = "ISP-8826",
                password = "104",
                packageName = "Quick 2 Hours (500MB)",
                dataLimitMb = 500,
                timeLimitMins = 120,
                downloadSpeedMbps = 3.0,
                uploadSpeedMbps = 1.0,
                price = 1.0,
                status = VoucherStatus.EXPIRED,
                createdAt = now - 3600_000L * 48,
                usedAt = now - 3600_000L * 46,
                usedBytes = 500_000_000L,
                macAddress = "38:F9:D3:5B:21:77"
            ),
            Voucher(
                id = UUID.randomUUID().toString(),
                code = "ISP-8827",
                password = "883",
                packageName = "1GB Daily",
                dataLimitMb = 1024,
                timeLimitMins = 1440,
                downloadSpeedMbps = 5.0,
                uploadSpeedMbps = 2.0,
                price = 2.0,
                status = VoucherStatus.DISABLED,
                createdAt = now - 3600_000L * 12
            )
        )
    }

    fun createInitialUsers(): MutableList<HotspotUser> {
        return mutableListOf(
            HotspotUser(
                id = "*1",
                username = "ISP-8821",
                password = "729",
                profile = "1GB Daily",
                status = UserStatus.ACTIVE,
                bytesIn = 82_450_000L,
                bytesOut = 329_550_000L,
                limitBytesTotal = 1024L * 1024L * 1024L,
                uptime = "3h 12m",
                limitUptime = "1d",
                disabled = false,
                comment = "MikroEasy|1GB Daily",
                macAddress = "D4:61:9D:41:8A:12",
                lastLogin = "Today, 11:20 AM"
            ),
            HotspotUser(
                id = "*2",
                username = "ISP-8822",
                password = "441",
                profile = "5GB Weekly",
                status = UserStatus.ACTIVE,
                bytesIn = 310_200_000L,
                bytesOut = 1_539_800_000L,
                limitBytesTotal = 5120L * 1024L * 1024L,
                uptime = "1d 4h",
                limitUptime = "7d",
                disabled = false,
                comment = "MikroEasy|5GB Weekly",
                macAddress = "AC:87:A3:99:1C:FE",
                lastLogin = "Yesterday, 06:15 PM"
            ),
            HotspotUser(
                id = "*3",
                username = "john.tenant",
                password = "secret",
                profile = "Monthly Unlimited",
                status = UserStatus.ACTIVE,
                bytesIn = 1_450_000_000L,
                bytesOut = 12_800_000_000L,
                limitBytesTotal = 0L,
                uptime = "5d 18h",
                limitUptime = "30d",
                disabled = false,
                comment = "Staff Apartment 4B",
                macAddress = "B8:27:EB:73:9A:10",
                lastLogin = "Sep 14, 08:30 AM"
            ),
            HotspotUser(
                id = "*4",
                username = "ISP-8823",
                password = "908",
                profile = "1GB Daily",
                status = UserStatus.OFFLINE,
                bytesIn = 0L,
                bytesOut = 0L,
                limitBytesTotal = 1024L * 1024L * 1024L,
                uptime = "0s",
                limitUptime = "1d",
                disabled = false,
                comment = "MikroEasy|1GB Daily",
                macAddress = null,
                lastLogin = "Never"
            ),
            HotspotUser(
                id = "*5",
                username = "ISP-8826",
                password = "104",
                profile = "Quick 2 Hours (500MB)",
                status = UserStatus.EXPIRED,
                bytesIn = 45_000_000L,
                bytesOut = 455_000_000L,
                limitBytesTotal = 500L * 1024L * 1024L,
                uptime = "2h 00m",
                limitUptime = "2h",
                disabled = false,
                comment = "Expired voucher",
                macAddress = "38:F9:D3:5B:21:77",
                lastLogin = "2 days ago"
            ),
            HotspotUser(
                id = "*6",
                username = "ISP-8827",
                password = "883",
                profile = "1GB Daily",
                status = UserStatus.DISABLED,
                bytesIn = 12_000_000L,
                bytesOut = 48_000_000L,
                limitBytesTotal = 1024L * 1024L * 1024L,
                uptime = "24m",
                limitUptime = "1d",
                disabled = true,
                comment = "Suspended by admin",
                macAddress = "90:CD:B6:11:44:02",
                lastLogin = "Sep 18, 02:11 PM"
            )
        )
    }

    fun createInitialActiveSessions(): MutableList<ActiveSession> {
        return mutableListOf(
            ActiveSession(
                id = "*a1",
                username = "ISP-8821",
                ipAddress = "192.168.88.105",
                macAddress = "D4:61:9D:41:8A:12",
                uptime = "3h 12m 44s",
                bytesIn = 82_450_000L,
                bytesOut = 329_550_000L,
                sessionTime = "03:12:44",
                loginBy = "http-chap"
            ),
            ActiveSession(
                id = "*a2",
                username = "ISP-8822",
                ipAddress = "192.168.88.142",
                macAddress = "AC:87:A3:99:1C:FE",
                uptime = "1d 04h 18m",
                bytesIn = 310_200_000L,
                bytesOut = 1_539_800_000L,
                sessionTime = "28:18:02",
                loginBy = "mac-cookie"
            ),
            ActiveSession(
                id = "*a3",
                username = "john.tenant",
                ipAddress = "192.168.88.201",
                macAddress = "B8:27:EB:73:9A:10",
                uptime = "5d 18h 31m",
                bytesIn = 1_450_000_000L,
                bytesOut = 12_800_000_000L,
                sessionTime = "138:31:50",
                loginBy = "http-chap"
            )
        )
    }

    val simulatedDiscoveredRouters = listOf(
        DiscoveryResult(
            ip = "192.168.88.1",
            mac = "48:8F:5A:21:40:D1",
            identity = "UltraNet-Gateway",
            model = "RouterBOARD 750Gr3 (hEX)",
            version = "7.14.3 (stable)",
            isMikrotik = true
        ),
        DiscoveryResult(
            ip = "192.168.88.254",
            mac = "C4:AD:34:7E:11:80",
            identity = "AP-Lobby-cAP",
            model = "MikroTik cAP ac",
            version = "7.13 (stable)",
            isMikrotik = true
        ),
        DiscoveryResult(
            ip = "10.0.0.1",
            mac = "B8:69:F4:9A:C3:55",
            identity = "Backbone-CCR",
            model = "CCR2004-16G-2S+",
            version = "7.14.2 (stable)",
            isMikrotik = true
        )
    )
}
