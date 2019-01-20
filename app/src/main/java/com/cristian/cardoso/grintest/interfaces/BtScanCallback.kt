package com.cristian.cardoso.grintest.interfaces

import com.cristian.cardoso.grintest.models.Device

interface BtScanCallback {

    fun onScanResult(device : Device)
    fun onStartDiscovering()
    fun onStopDiscovering()
    fun onBtDisconnected()
}