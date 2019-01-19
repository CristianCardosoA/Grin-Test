package com.cristian.cardoso.grintest.viewmodels

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.cristian.cardoso.grintest.R
import com.cristian.cardoso.grintest.adapters.DeviceAdapter
import com.cristian.cardoso.grintest.interfaces.ToolbarCallback
import com.cristian.cardoso.grintest.models.Device
import com.cristian.cardoso.grintest.repositories.BluetoothDevices
import com.cristian.cardoso.grintest.utils.BluetoothManager

class MainViewModel(application: Application) : AndroidViewModel(application), ToolbarCallback, LifecycleObserver {

    var toolbarViewModel : ToolbarViewModel = ToolbarViewModel(this, R.drawable.bluetooth, null, R.drawable.ic_refresh_black_24dp)
    val errors = MutableLiveData<String>()
    val bluetoothTurnOn = MutableLiveData<Boolean>()
    val permissionLocationGranted = MutableLiveData<Boolean>()
    val adapterBondedDevices = DeviceAdapter()
    val adapterNewDevices = DeviceAdapter()

    val bluetoothListener = object : BluetoothDevices.BtScanCallback {

        override fun onScanResult(device: Device) {

            val devices = adapterNewDevices.mItems.toMutableList()
            if(!devices.contains(device)){
                devices.add(device)
                adapterNewDevices.update(devices)
            }
        }
    }

    init {

        adapterBondedDevices.mItems = listOf()
        adapterNewDevices.mItems = listOf()

        listBluetoothDevices()
    }

    private fun listBluetoothDevices() {

        var result = BluetoothManager.isBluetoothAvailable()

        if (result) {

            result = BluetoothManager.isBluetoothOn()

            bluetoothTurnOn.value = result

            if (result) {


                //Bluetooth is enabled
                //list bluetooth devices paired

                listBondedDevicesPaired()

                //Request permission location coarse in order to discover new devices
                //Android 6.0 is necessary coarse permission
                val locationPermissionGranted = requestLocationPermission()

                if (locationPermissionGranted){

                    listenAndStartExploringBt()
                }
            }

        } else {

            //Device does not support bluetooth
            errors.value = getApplication<Application>().getString(com.cristian.cardoso.grintest.R.string.error_device_does_not_support_bluetooth)
        }
    }

    fun requestLocationPermission() : Boolean {

        val granted = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        this.permissionLocationGranted.value = granted

        return granted
    }

    fun onRequestPermissionResult(requestCode: Int, grantResults: IntArray){

        when (requestCode) {

            BluetoothManager.REQUEST_LOCATION_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    permissionLocationGranted.value = true
                    listBluetoothDevices()
                }
                return
            }
        }
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int) {

        if (requestCode == BluetoothManager.REQUEST_ENABLE_BT && resultCode == RESULT_OK) {

            //User accept to turn on the bluetooth, list devices.
            listBluetoothDevices()
        }
    }

    private fun listenAndStartExploringBt(){

        BluetoothManager.startListen(getApplication(), bluetoothListener)
    }

    private fun stopListenBt(){

        BluetoothManager.stopListen(getApplication())
    }

    private fun listBondedDevicesPaired(){

        BluetoothManager.discoverDevicesPaired()?.let { devices -> adapterBondedDevices.update(devices) }
    }

    override fun onClickToolbarRightButton() {

        adapterNewDevices.update(listOf())
        BluetoothManager.refresh(getApplication(), bluetoothListener)
    }

    override fun onClickToolbarCenterButton() {

    }

    override fun onClickToolbarLeftButton() {

    }

    override fun onCleared() {

        stopListenBt()
        super.onCleared()
    }
}