package com.cristian.cardoso.grintest.repositories

import android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED
import android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_STARTED
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.cristian.cardoso.grintest.models.Device

class BluetoothReceiver : BroadcastReceiver() {

    var callback : BtScanCallback? = null

    fun listenBtScan(context: Context, callback : BtScanCallback){

        if (this.callback == null){
            this.callback = callback
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED)
        filter.addAction(ACTION_DISCOVERY_STARTED)
        filter.addAction(ACTION_DISCOVERY_FINISHED)
        context.registerReceiver(this, filter)
    }

    fun stopListenBt(context: Context){

        context.unregisterReceiver(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        when {
            BluetoothDevice.ACTION_FOUND == action -> {
                val device :  BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                if(!device.name.isNullOrEmpty() && !device.address.isNullOrEmpty()){
                    val dBm = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, java.lang.Short.MIN_VALUE)
                    callback?.onScanResult(Device(null, device.name, device.address, dBm, null))
                }
            }
            ACTION_DISCOVERY_FINISHED == action -> callback?.onStopDiscovering()
            ACTION_DISCOVERY_STARTED == action -> callback?.onStartDiscovering()
        }
    }

    interface BtScanCallback {

        fun onScanResult(device : Device)
        fun onStartDiscovering()
        fun onStopDiscovering()
    }
}