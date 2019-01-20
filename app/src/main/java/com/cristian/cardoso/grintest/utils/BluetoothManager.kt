package com.cristian.cardoso.grintest.utils

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import com.cristian.cardoso.grintest.interfaces.BtScanCallback
import com.cristian.cardoso.grintest.models.Device
import com.cristian.cardoso.grintest.receivers.BluetoothReceiver

class BluetoothManager {

    private val bluetoothFilter = BluetoothReceiver()
    private val mBluetoothAdapter : BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    fun isBluetoothAvailable() : Boolean {

        return mBluetoothAdapter != null
    }

    fun isBluetoothOn() : Boolean {

        return mBluetoothAdapter?.isEnabled ?: false
    }

    fun discoverDevicesPaired() : List<Device>? {

        if (mBluetoothAdapter == null) {
            return null
        }

        val devices : ArrayList<Device> = ArrayList()

        val pairedDevices = mBluetoothAdapter.bondedDevices

        if (pairedDevices.size > 0) {

            for (device in pairedDevices) {
                devices.add(Device(null, device.name, device.address, "0.0", null))
            }
        }

        return  devices
    }

    fun startListen(context: Context, listener : BtScanCallback){

        bluetoothFilter.listenBtScan(context, listener)
        mBluetoothAdapter?.startDiscovery()
    }

    fun stopListen(context: Context){

        if(mBluetoothAdapter?.isDiscovering == true){

            bluetoothFilter.stopListenBt(context)
            mBluetoothAdapter.cancelDiscovery()
        }
    }

    companion object {

        const val REQUEST_LOCATION_PERMISSION = 1232
        const val REQUEST_ENABLE_BT = 1231

        fun requestEnableBluetooth(activity: Activity){

            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }
}