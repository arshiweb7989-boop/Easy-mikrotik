package com.example.data.service

import com.example.data.model.DiscoveryResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class RouterDiscoveryService {

    suspend fun discoverLocalRouters(): List<DiscoveryResult> = withContext(Dispatchers.IO) {
        // Simulate network UDP broadcast / MNDP scan & ARP check
        delay(1200) // Realistic scanning delay
        MockRouterEngine.simulatedDiscoveredRouters
    }
}
