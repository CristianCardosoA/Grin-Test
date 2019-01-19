package com.cristian.cardoso.grintest.utils

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import com.cristian.cardoso.grintest.models.Device
import com.cristian.cardoso.grintest.repositories.BluetoothDevices
import java.util.*


object BluetoothManager {

    const val REQUEST_ENABLE_BT = 1231
    const val REQUEST_LOCATION_PERMISSION = 1232

    private val bluetoothFilter = BluetoothDevices()
    private val mBluetoothAdapter : BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    fun requestEnableBluetooth(activity: Activity){

        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }

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
        // If there are paired devices
        if (pairedDevices.size > 0) {
            // Loop through paired devices
            for (device in pairedDevices) {
                devices.add(Device(device.uuids.first().uuid.toString(), device.name, device.address, "0.0", Date().toString()))
            }
        }

        return  devices
    }

    fun startListen(context: Context, listener : BluetoothDevices.BtScanCallback){

        bluetoothFilter.listenBtScan(context, listener)
        mBluetoothAdapter?.startDiscovery()
    }

    fun stopListen(context: Context){

        bluetoothFilter.stopListenBt(context)
        mBluetoothAdapter?.cancelDiscovery()
    }

    fun refresh(context: Context, listener : BluetoothDevices.BtScanCallback){

        stopListen(context)
        startListen(context, listener)
    }
}