package com.cristian.cardoso.grintest.repositories

import android.content.Context
import com.cristian.cardoso.grintest.models.Device
import com.cristian.cardoso.grintest.utils.BluetoothManager

class BluetoothRepository(private val bluetoothManager : BluetoothManager) {

    //Function to obtain devices from server
    private fun obtainBluetoothDevicesFromServer() : List<Device> {

        return emptyList()
    }

    //Function to obtain devices from database
    private fun obtainBluetoothDevicesLocal() : List<Device> {

        return emptyList()
    }

    fun obtainPairedBluetoothDevices() : List<Device>? {

        return bluetoothManager.discoverDevicesPaired()
    }

    //Function to obtain nearest devices from BT
    fun obtainNewBluetoothDevices(context : Context, listener : BluetoothReceiver.BtScanCallback) {

        //Start discovering new devices
        bluetoothManager.startListen(context, listener)
    }
}