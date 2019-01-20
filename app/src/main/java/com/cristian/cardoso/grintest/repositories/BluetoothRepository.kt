package com.cristian.cardoso.grintest.repositories

import android.content.Context
import com.cristian.cardoso.grintest.interfaces.DevicesAPIService
import com.cristian.cardoso.grintest.models.Device
import com.cristian.cardoso.grintest.utils.BluetoothManager

class BluetoothRepository(private val bluetoothManager : BluetoothManager) {

    //Function to obtain devices from server
    suspend fun saveBluetoothDevicesToServer(device : Device) : Device {

        val apiService = DevicesAPIService.create()

        return apiService.saveBTDevice(device).await()
    }

    //Function to obtain devices from server
    suspend fun obtainBluetoothDevicesFromServer() : List<Device> {

        val apiService = DevicesAPIService.create()

        return apiService.getAllBTDevices().await()
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