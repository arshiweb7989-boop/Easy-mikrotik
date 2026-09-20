package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.service.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Random
import java.util.UUID

enum class NavigationTab {
    DASHBOARD,
    VOUCHERS,
    USERS,
    ISP_PROFILE,
    ROUTER
}

class MikroEasyViewModel(application: Application) : AndroidViewModel(application) {

    private val storageService = StorageService(application)
    private val apiService = MikrotikApiService()
    private val discoveryService = RouterDiscoveryService()

    // Tabs & Navigation
    private val _currentTab = MutableStateFlow(NavigationTab.DASHBOARD)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    // Connection & Router state
    private val _currentRouter = MutableStateFlow<RouterDevice?>(MockRouterEngine.sampleRouters.first())
    val currentRouter: StateFlow<RouterDevice?> = _currentRouter.asStateFlow()

    private val _connectionStatus = MutableStateFlow<ConnectionStatus>(
        ConnectionStatus.Connected(MockRouterEngine.sampleRouters.first())
    )
    val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()

    private val _savedRouters = MutableStateFlow<List<RouterDevice>>(emptyList())
    val savedRouters: StateFlow<List<RouterDevice>> = _savedRouters.asStateFlow()

    private val _discoveredRouters = MutableStateFlow<List<DiscoveryResult>>(emptyList())
    val discoveredRouters: StateFlow<List<DiscoveryResult>> = _discoveredRouters.asStateFlow()

    private val _isDiscovering = MutableStateFlow(false)
    val isDiscovering: StateFlow<Boolean> = _isDiscovering.asStateFlow()

    // Vouchers & Users & Sessions
    private val _vouchers = MutableStateFlow<List<Voucher>>(emptyList())
    val vouchers: StateFlow<List<Voucher>> = _vouchers.asStateFlow()

    private val _users = MutableStateFlow<List<HotspotUser>>(emptyList())
    val users: StateFlow<List<HotspotUser>> = _users.asStateFlow()

    private val _activeSessions = MutableStateFlow<List<ActiveSession>>(emptyList())
    val activeSessions: StateFlow<List<ActiveSession>> = _activeSessions.asStateFlow()

    private val _packages = MutableStateFlow<List<HotspotPackage>>(MockRouterEngine.samplePackages)
    val packages: StateFlow<List<HotspotPackage>> = _packages.asStateFlow()

    // ISP Profile & Captive Portal
    private val _ispProfile = MutableStateFlow(IspProfile())
    val ispProfile: StateFlow<IspProfile> = _ispProfile.asStateFlow()

    private val _loginPageConfig = MutableStateFlow(LoginPageConfig())
    val loginPageConfig: StateFlow<LoginPageConfig> = _loginPageConfig.asStateFlow()

    // Search and filters
    private val _voucherSearchQuery = MutableStateFlow("")
    val voucherSearchQuery: StateFlow<String> = _voucherSearchQuery.asStateFlow()

    private val _voucherFilter = MutableStateFlow<VoucherStatus?>(null)
    val voucherFilter: StateFlow<VoucherStatus?> = _voucherFilter.asStateFlow()

    private val _userFilter = MutableStateFlow<UserStatus?>(null)
    val userFilter: StateFlow<UserStatus?> = _userFilter.asStateFlow()

    // Feedback notification banner
    private val _notification = MutableStateFlow<Pair<String, Boolean>?>(null)
    val notification: StateFlow<Pair<String, Boolean>?> = _notification.asStateFlow()

    // Dialog flags
    val showCreateVoucherDialog = MutableStateFlow(false)
    val showBulkGenDialog = MutableStateFlow(false)
    val showActiveUsersScreen = MutableStateFlow(false)
    val showPackagesScreen = MutableStateFlow(false)
    val showVoucherPrintModal = MutableStateFlow(false)
    val voucherForPrint = MutableStateFlow<Voucher?>(null)
    val showApplyLoginModal = MutableStateFlow(false)
    val isApplyingLoginPage = MutableStateFlow(false)

    init {
        // Load persisted or sample data
        _ispProfile.value = storageService.loadIspProfile()
        _loginPageConfig.value = storageService.loadLoginPageConfig()
        val loadedRouters = storageService.loadRouters()
        _savedRouters.value = loadedRouters
        _currentRouter.value = loadedRouters.firstOrNull() ?: MockRouterEngine.sampleRouters.first()
        _vouchers.value = MockRouterEngine.createInitialVouchers()
        _users.value = MockRouterEngine.createInitialUsers()
        _activeSessions.value = MockRouterEngine.createInitialActiveSessions()
    }

    fun selectTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    fun showMessage(message: String, isSuccess: Boolean = true) {
        _notification.value = Pair(message, isSuccess)
    }

    fun clearNotification() {
        _notification.value = null
    }

    fun setVoucherSearch(query: String) {
        _voucherSearchQuery.value = query
    }

    fun setVoucherFilter(status: VoucherStatus?) {
        _voucherFilter.value = status
    }

    fun setUserFilter(status: UserStatus?) {
        _userFilter.value = status
    }

    // Router Connection
    fun connectRouter(
        ip: String,
        port: Int = 8728,
        username: String = "admin",
        password: String = "",
        isSsl: Boolean = false,
        isMock: Boolean = true
    ) {
        viewModelScope.launch {
            _connectionStatus.value = ConnectionStatus.Connecting
            val result = apiService.testOrConnect(ip, port, username, password, isSsl, isMock)
            result.onSuccess { router ->
                _currentRouter.value = router
                _connectionStatus.value = ConnectionStatus.Connected(router)

                // Save or update in saved routers
                val currentList = _savedRouters.value.toMutableList()
                val existingIndex = currentList.indexOfFirst { it.ip == router.ip }
                if (existingIndex >= 0) {
                    currentList[existingIndex] = router
                } else {
                    currentList.add(router)
                }
                _savedRouters.value = currentList
                storageService.saveRouters(currentList)

                showMessage("Connected to ${router.identity} 🟢")
            }.onFailure { err ->
                _connectionStatus.value = ConnectionStatus.Error(
                    message = err.message ?: "Connection failed",
                    technicalDetail = err.localizedMessage
                )
                showMessage(err.message ?: "Connection failed", isSuccess = false)
            }
        }
    }

    fun disconnectRouter() {
        _currentRouter.value = null
        _connectionStatus.value = ConnectionStatus.Disconnected
        showMessage("Router disconnected.")
    }

    fun switchRouter(router: RouterDevice) {
        _currentRouter.value = router
        _connectionStatus.value = ConnectionStatus.Connected(router)
        showMessage("Switched to ${router.identity} 🟢")
    }

    fun discoverRouters() {
        viewModelScope.launch {
            _isDiscovering.value = true
            try {
                val results = discoveryService.discoverLocalRouters()
                _discoveredRouters.value = results
                if (results.isEmpty()) {
                    showMessage("Unable to find MikroTik router on local network.", isSuccess = false)
                } else {
                    showMessage("Found ${results.size} MikroTik routers!")
                }
            } catch (e: Exception) {
                showMessage("Scan failed: ${e.message}", isSuccess = false)
            } finally {
                _isDiscovering.value = false
            }
        }
    }

    // Voucher Management
    fun createVoucher(
        packageName: String,
        dataLimitMb: Long,
        timeLimitMins: Long,
        downloadSpeedMbps: Double,
        uploadSpeedMbps: Double,
        price: Double,
        customCode: String? = null,
        customPassword: String? = null
    ) {
        val random = Random()
        val code = customCode?.takeIf { it.isNotBlank() } ?: "ISP-${1000 + random.nextInt(9000)}"
        val pass = customPassword?.takeIf { it.isNotBlank() } ?: (100 + random.nextInt(900)).toString()

        val newVoucher = Voucher(
            id = UUID.randomUUID().toString(),
            code = code,
            password = pass,
            packageName = packageName,
            dataLimitMb = dataLimitMb,
            timeLimitMins = timeLimitMins,
            downloadSpeedMbps = downloadSpeedMbps,
            uploadSpeedMbps = uploadSpeedMbps,
            price = price,
            status = VoucherStatus.UNUSED,
            createdAt = System.currentTimeMillis()
        )

        val updatedVouchers = listOf(newVoucher) + _vouchers.value
        _vouchers.value = updatedVouchers

        // Also reflect in Hotspot users list
        val newUser = HotspotUser(
            id = "*${10 + _users.value.size}",
            username = code,
            password = pass,
            profile = packageName,
            status = UserStatus.OFFLINE,
            limitBytesTotal = if (dataLimitMb > 0) dataLimitMb * 1024L * 1024L else 0L,
            limitUptime = MikrotikApiV6V7Compat.formatUptimeLimit(timeLimitMins),
            comment = "MikroEasy|$packageName"
        )
        _users.value = listOf(newUser) + _users.value

        showMessage("Voucher created successfully.")
    }

    fun generateBulkVouchers(config: BulkGenConfig) {
        val random = Random()
        val newVouchers = mutableListOf<Voucher>()
        val newUsers = mutableListOf<HotspotUser>()
        val existingCodes = _vouchers.value.map { it.code }.toSet()

        var generated = 0
        var counter = 1000 + random.nextInt(5000)

        while (generated < config.quantity) {
            val code = if (config.prefix.isNotBlank()) {
                "${config.prefix}-${counter}"
            } else {
                counter.toString()
            }
            counter++

            if (!existingCodes.contains(code)) {
                val pass = if (config.isNumbersOnly) {
                    (100 + random.nextInt(900)).toString()
                } else {
                    UUID.randomUUID().toString().take(4).uppercase()
                }

                val voucher = Voucher(
                    id = UUID.randomUUID().toString(),
                    code = code,
                    password = pass,
                    packageName = config.packageName,
                    dataLimitMb = config.dataLimitMb,
                    timeLimitMins = config.timeLimitMins,
                    downloadSpeedMbps = config.downloadSpeedMbps,
                    uploadSpeedMbps = config.uploadSpeedMbps,
                    price = config.price,
                    status = VoucherStatus.UNUSED,
                    createdAt = System.currentTimeMillis()
                )
                newVouchers.add(voucher)

                newUsers.add(
                    HotspotUser(
                        id = "*${10 + _users.value.size + generated}",
                        username = code,
                        password = pass,
                        profile = config.packageName,
                        status = UserStatus.OFFLINE,
                        limitBytesTotal = if (config.dataLimitMb > 0) config.dataLimitMb * 1024L * 1024L else 0L,
                        limitUptime = MikrotikApiV6V7Compat.formatUptimeLimit(config.timeLimitMins),
                        comment = "MikroEasy|Bulk|${config.packageName}"
                    )
                )
                generated++
            }
        }

        _vouchers.value = newVouchers + _vouchers.value
        _users.value = newUsers + _users.value
        showMessage("Generated ${config.quantity} vouchers successfully!")
    }

    fun deleteVoucher(voucher: Voucher) {
        _vouchers.value = _vouchers.value.filter { it.id != voucher.id }
        _users.value = _users.value.filter { it.username != voucher.code }
        showMessage("Voucher ${voucher.code} deleted.")
    }

    fun toggleVoucherStatus(voucher: Voucher) {
        val newStatus = if (voucher.status == VoucherStatus.DISABLED) VoucherStatus.UNUSED else VoucherStatus.DISABLED
        _vouchers.value = _vouchers.value.map {
            if (it.id == voucher.id) it.copy(status = newStatus) else it
        }
        _users.value = _users.value.map {
            if (it.username == voucher.code) it.copy(disabled = (newStatus == VoucherStatus.DISABLED)) else it
        }
        showMessage("Voucher status updated.")
    }

    // User Operations
    fun disconnectUser(session: ActiveSession) {
        viewModelScope.launch {
            val result = apiService.disconnectActiveUser(session.id)
            if (result.isSuccess) {
                _activeSessions.value = _activeSessions.value.filter { it.id != session.id }
                // update user status to offline
                _users.value = _users.value.map {
                    if (it.username == session.username) it.copy(status = UserStatus.OFFLINE) else it
                }
                showMessage("User ${session.username} disconnected successfully.")
            } else {
                showMessage("Failed to disconnect user.", isSuccess = false)
            }
        }
    }

    fun deleteUser(user: HotspotUser) {
        _users.value = _users.value.filter { it.id != user.id }
        _vouchers.value = _vouchers.value.filter { it.code != user.username }
        _activeSessions.value = _activeSessions.value.filter { it.username != user.username }
        showMessage("User ${user.username} deleted.")
    }

    fun resetUserCounters(user: HotspotUser) {
        _users.value = _users.value.map {
            if (it.id == user.id) it.copy(bytesIn = 0L, bytesOut = 0L, uptime = "0s") else it
        }
        showMessage("Usage counters reset for ${user.username}.")
    }

    fun toggleUserDisabled(user: HotspotUser) {
        val newDisabled = !user.disabled
        val newStatus = if (newDisabled) UserStatus.DISABLED else UserStatus.OFFLINE
        _users.value = _users.value.map {
            if (it.id == user.id) it.copy(disabled = newDisabled, status = newStatus) else it
        }
        showMessage(if (newDisabled) "User disabled." else "User enabled.")
    }

    // Package Management
    fun createPackage(pkg: HotspotPackage) {
        _packages.value = _packages.value + pkg
        showMessage("Package '${pkg.name}' created.")
    }

    fun deletePackage(pkg: HotspotPackage) {
        _packages.value = _packages.value.filter { it.id != pkg.id }
        showMessage("Package '${pkg.name}' removed.")
    }

    // ISP Profile & Login Page
    fun updateIspProfile(profile: IspProfile) {
        _ispProfile.value = profile
        storageService.saveIspProfile(profile)
        showMessage("ISP Profile saved successfully.")
    }

    fun updateLoginPageConfig(config: LoginPageConfig) {
        _loginPageConfig.value = config
        storageService.saveLoginPageConfig(config)
        showMessage("Login page design saved.")
    }

    fun applyLoginPageToRouter() {
        val router = _currentRouter.value
        if (router == null) {
            showMessage("Router is offline. Please connect first.", isSuccess = false)
            return
        }

        viewModelScope.launch {
            isApplyingLoginPage.value = true
            val html = MikrotikApiV6V7Compat.generateHotspotLoginHtml(_ispProfile.value, _loginPageConfig.value)
            val result = apiService.deployHotspotLoginPage(router, html)
            isApplyingLoginPage.value = false
            showApplyLoginModal.value = false

            result.onSuccess { msg ->
                showMessage(msg)
            }.onFailure { err ->
                showMessage("Failed to update login page: ${err.message}", isSuccess = false)
            }
        }
    }
}
