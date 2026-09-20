package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Voucher
import com.example.data.model.VoucherStatus
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.MikroEasyViewModel
import com.example.viewmodel.NavigationTab
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val viewModel: MikroEasyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MikroEasyApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MikroEasyApp(viewModel: MikroEasyViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val currentRouter by viewModel.currentRouter.collectAsState()
    val connectionStatus by viewModel.connectionStatus.collectAsState()
    val savedRouters by viewModel.savedRouters.collectAsState()
    val discoveredRouters by viewModel.discoveredRouters.collectAsState()
    val isDiscovering by viewModel.isDiscovering.collectAsState()

    val vouchers by viewModel.vouchers.collectAsState()
    val users by viewModel.users.collectAsState()
    val activeSessions by viewModel.activeSessions.collectAsState()
    val packages by viewModel.packages.collectAsState()
    val ispProfile by viewModel.ispProfile.collectAsState()
    val loginPageConfig by viewModel.loginPageConfig.collectAsState()

    val voucherSearchQuery by viewModel.voucherSearchQuery.collectAsState()
    val voucherFilter by viewModel.voucherFilter.collectAsState()
    val userFilter by viewModel.userFilter.collectAsState()
    val notification by viewModel.notification.collectAsState()

    val showCreateVoucher by viewModel.showCreateVoucherDialog.collectAsState()
    val showBulkGen by viewModel.showBulkGenDialog.collectAsState()
    val showActiveUsers by viewModel.showActiveUsersScreen.collectAsState()
    val showPackages by viewModel.showPackagesScreen.collectAsState()
    val showPrintModal by viewModel.showVoucherPrintModal.collectAsState()
    val voucherForPrint by viewModel.voucherForPrint.collectAsState()
    val showApplyLogin by viewModel.showApplyLoginModal.collectAsState()
    val isApplyingLogin by viewModel.isApplyingLoginPage.collectAsState()

    // Auto clear notification banner after 3.5 seconds
    LaunchedEffect(notification) {
        if (notification != null) {
            delay(3500)
            viewModel.clearNotification()
        }
    }

    Scaffold(
        topBar = {
            if (!showActiveUsers && !showPackages) {
                AppTopBar(
                    currentRouter = currentRouter,
                    connectionStatus = connectionStatus,
                    savedRouters = savedRouters,
                    onSwitchRouter = { viewModel.switchRouter(it) },
                    onNavigateToRouterScreen = { viewModel.selectTab(NavigationTab.ROUTER) }
                )
            }
        },
        bottomBar = {
            if (!showActiveUsers && !showPackages) {
                AppBottomNavBar(
                    currentTab = currentTab,
                    onTabSelected = { viewModel.selectTab(it) },
                    activeVouchersCount = vouchers.count { it.status == VoucherStatus.ACTIVE },
                    activeUsersCount = activeSessions.size
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Main view routing
            when {
                showActiveUsers -> {
                    ActiveUsersScreen(
                        activeSessions = activeSessions,
                        onBack = { viewModel.showActiveUsersScreen.value = false },
                        onDisconnect = { viewModel.disconnectUser(it) }
                    )
                }
                showPackages -> {
                    PackagesScreen(
                        packages = packages,
                        onBack = { viewModel.showPackagesScreen.value = false },
                        onCreatePackage = { viewModel.createPackage(it) },
                        onDeletePackage = { viewModel.deletePackage(it) }
                    )
                }
                else -> {
                    when (currentTab) {
                        NavigationTab.DASHBOARD -> {
                            DashboardScreen(
                                currentRouter = currentRouter,
                                connectionStatus = connectionStatus,
                                vouchers = vouchers,
                                users = users,
                                activeCount = activeSessions.size,
                                onCreateVoucherClick = { viewModel.showCreateVoucherDialog.value = true },
                                onBulkGenClick = { viewModel.showBulkGenDialog.value = true },
                                onUsersClick = { viewModel.selectTab(NavigationTab.USERS) },
                                onActiveUsersClick = { viewModel.showActiveUsersScreen.value = true },
                                onPackagesClick = { viewModel.showPackagesScreen.value = true },
                                onNavigateToRouter = { viewModel.selectTab(NavigationTab.ROUTER) }
                            )
                        }
                        NavigationTab.VOUCHERS -> {
                            VouchersScreen(
                                vouchers = vouchers,
                                searchQuery = voucherSearchQuery,
                                onSearchChange = { viewModel.setVoucherSearch(it) },
                                selectedFilter = voucherFilter,
                                onFilterSelect = { viewModel.setVoucherFilter(it) },
                                onCreateClick = { viewModel.showCreateVoucherDialog.value = true },
                                onBulkClick = { viewModel.showBulkGenDialog.value = true },
                                onPrintVoucher = {
                                    viewModel.voucherForPrint.value = it
                                    viewModel.showVoucherPrintModal.value = true
                                },
                                onPrintAll = {
                                    viewModel.voucherForPrint.value = null
                                    viewModel.showVoucherPrintModal.value = true
                                },
                                onToggleStatus = { viewModel.toggleVoucherStatus(it) },
                                onDeleteVoucher = { viewModel.deleteVoucher(it) }
                            )
                        }
                        NavigationTab.USERS -> {
                            UsersScreen(
                                users = users,
                                activeSessions = activeSessions,
                                searchQuery = voucherSearchQuery,
                                onSearchChange = { viewModel.setVoucherSearch(it) },
                                selectedFilter = userFilter,
                                onFilterSelect = { viewModel.setUserFilter(it) },
                                onViewActiveUsers = { viewModel.showActiveUsersScreen.value = true },
                                onDisconnect = { user ->
                                    val session = activeSessions.find { it.username == user.username }
                                    if (session != null) viewModel.disconnectUser(session)
                                },
                                onToggleDisabled = { viewModel.toggleUserDisabled(it) },
                                onResetUsage = { viewModel.resetUserCounters(it) },
                                onDeleteUser = { viewModel.deleteUser(it) }
                            )
                        }
                        NavigationTab.ISP_PROFILE -> {
                            IspProfileScreen(
                                profile = ispProfile,
                                loginPageConfig = loginPageConfig,
                                onSaveProfile = { viewModel.updateIspProfile(it) },
                                onSaveLoginPageConfig = { viewModel.updateLoginPageConfig(it) },
                                onApplyToRouter = { viewModel.showApplyLoginModal.value = true }
                            )
                        }
                        NavigationTab.ROUTER -> {
                            RouterScreen(
                                currentRouter = currentRouter,
                                connectionStatus = connectionStatus,
                                savedRouters = savedRouters,
                                discoveredRouters = discoveredRouters,
                                isDiscovering = isDiscovering,
                                onFindRouterAuto = { viewModel.discoverRouters() },
                                onConnectManual = { ip, port, user, pass, isSsl, isMock ->
                                    viewModel.connectRouter(ip, port, user, pass, isSsl, isMock)
                                },
                                onSwitchRouter = { viewModel.switchRouter(it) },
                                onDisconnect = { viewModel.disconnectRouter() }
                            )
                        }
                    }
                }
            }

            // Notification / Feedback Banner
            notification?.let { (msg, isSuccess) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.TopCenter)
                ) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSuccess) StatusGreen else StatusRed
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = msg,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal Dialogs
    if (showCreateVoucher) {
        CreateVoucherDialog(
            packages = packages,
            onDismiss = { viewModel.showCreateVoucherDialog.value = false },
            onCreate = { pkgName, dataMb, timeMins, down, up, price, customCode, customPass ->
                viewModel.createVoucher(pkgName, dataMb, timeMins, down, up, price, customCode, customPass)
            }
        )
    }

    if (showBulkGen) {
        BulkGeneratorDialog(
            packages = packages,
            onDismiss = { viewModel.showBulkGenDialog.value = false },
            onGenerate = { config ->
                viewModel.generateBulkVouchers(config)
            }
        )
    }

    if (showPrintModal) {
        val printList = if (voucherForPrint != null) listOf(voucherForPrint!!) else vouchers
        VoucherPrintSheetDialog(
            vouchers = printList,
            ispProfile = ispProfile,
            onDismiss = { viewModel.showVoucherPrintModal.value = false },
            onPrint = {
                viewModel.showMessage("Sent ${printList.size} voucher(s) to printer.")
                viewModel.showVoucherPrintModal.value = false
            },
            onSharePdf = {
                viewModel.showMessage("Exported ${printList.size} voucher(s) to PDF.")
                viewModel.showVoucherPrintModal.value = false
            }
        )
    }

    if (showApplyLogin) {
        ApplyLoginPageDialog(
            router = currentRouter,
            isLoading = isApplyingLogin,
            onDismiss = { viewModel.showApplyLoginModal.value = false },
            onApply = {
                viewModel.applyLoginPageToRouter()
            }
        )
    }
}
