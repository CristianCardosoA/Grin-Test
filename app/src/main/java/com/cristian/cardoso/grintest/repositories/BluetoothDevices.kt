package com.cristian.cardoso.grintest.repositories

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.cristian.cardoso.grintest.models.Device
import java.util.*


class BluetoothDevices : BroadcastReceiver() {

    var callback : BtScanCallback? = null

    fun listenBtScan(context: Context, callback : BtScanCallback){

        if (this.callback == null){
            this.callback = callback
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED)
        context.registerReceiver(this, filter)
    }

    fun stopListenBt(context: Context){

        context.unregisterReceiver(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        // When discovery finds a device
        if (BluetoothDevice.ACTION_FOUND == action) {
            // Get the BluetoothDevice object from the Intent
            val device :  BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            // Add the name and address to an array adapter to show in a ListView
            callback?.onScanResult(Device("00001", device.name, device.address, "0.0", Date().toString()))
        }
    }

    interface BtScanCallback {

        fun onScanResult(device : Device)
    }
}