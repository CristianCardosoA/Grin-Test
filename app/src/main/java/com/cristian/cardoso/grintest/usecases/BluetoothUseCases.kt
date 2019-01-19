package com.cristian.cardoso.grintest.usecases

import android.content.Context
import com.cristian.cardoso.grintest.models.Device
import com.cristian.cardoso.grintest.repositories.BluetoothReceiver
import com.cristian.cardoso.grintest.repositories.BluetoothRepository
import com.cristian.cardoso.grintest.utils.BluetoothManager

class BluetoothUseCases {

    private var repository : BluetoothRepository? = null

    val bluetoothManager = BluetoothManager()

    init {

        repository = BluetoothRepository(bluetoothManager)
    }

    fun getNewBluetoothDevices(context: Context, callback : BluetoothReceiver.BtScanCallback){
        repository?.obtainNewBluetoothDevices(context, callback)
    }

    fun getPairedBluetoothDevices() : List<Device>? {

        return repository?.obtainPairedBluetoothDevices()
    }

    fun stop(context: Context){

        bluetoothManager.stopListen(context)
    }
}