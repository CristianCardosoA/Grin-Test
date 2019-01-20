package com.cristian.cardoso.grintest.receivers

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.*
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.cristian.cardoso.grintest.interfaces.BtScanCallback
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
        filter.addAction(ACTION_STATE_CHANGED)
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
                    callback?.onScanResult(Device(null, device.name, device.address, dBm.toString(), null))
                }
            }
            ACTION_DISCOVERY_FINISHED == action -> callback?.onStopDiscovering()
            ACTION_DISCOVERY_STARTED == action -> callback?.onStartDiscovering()
            ACTION_STATE_CHANGED == action -> {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                if (state == BluetoothAdapter.STATE_OFF){
                    callback?.onBtDisconnected()
                }
            }
        }
    }
}