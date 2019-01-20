package com.cristian.cardoso.grintest.usecases

import android.content.Context
import com.cristian.cardoso.grintest.interfaces.BtScanCallback
import com.cristian.cardoso.grintest.models.Device
import com.cristian.cardoso.grintest.repositories.BluetoothRepository
import com.cristian.cardoso.grintest.utils.BluetoothManager

class BluetoothUseCases {

    private var repository : BluetoothRepository? = null

    val bluetoothManager = BluetoothManager()

    init {

        repository = BluetoothRepository(bluetoothManager)
    }

    suspend fun getBluetoothDevicesAPI() : List<Device>? {

        return repository?.obtainBluetoothDevicesFromServer()
    }

    suspend fun saveBluetoothDeviceToAPI(device : Device) : Device? {

        return repository?.saveBluetoothDevicesToServer(device)
    }

    fun getNewBluetoothDevices(context: Context, callback : BtScanCallback){
        repository?.obtainNewBluetoothDevices(context, callback)
    }

    fun getPairedBluetoothDevices() : List<Device>? {

        return repository?.obtainPairedBluetoothDevices()
    }

    fun stop(context: Context){

        bluetoothManager.stopListen(context)
    }
}