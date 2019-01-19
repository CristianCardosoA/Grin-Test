package com.cristian.cardoso.grintest.viewmodels

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import com.cristian.cardoso.grintest.R
import com.cristian.cardoso.grintest.adapters.DeviceAdapter
import com.cristian.cardoso.grintest.interfaces.ToolbarCallback
import com.cristian.cardoso.grintest.models.Device
import com.cristian.cardoso.grintest.repositories.BluetoothReceiver
import com.cristian.cardoso.grintest.utils.BluetoothManager
import com.cristian.cardoso.grintest.usecases.BluetoothUseCases

class MainViewModel(application: Application) : AndroidViewModel(application), ToolbarCallback, LifecycleObserver {

    val toolbarViewModel : ToolbarViewModel = ToolbarViewModel(this, R.drawable.bluetooth, null, R.drawable.ic_refresh_black_24dp)
    val bluetoothTurnOn = MutableLiveData<Boolean>()
    val permissionLocationGranted = MutableLiveData<Boolean>()
    val bluetoothIsDiscovering = MutableLiveData<Boolean>()
    val adapterBondedDevices = DeviceAdapter()
    val adapterNewDevices = DeviceAdapter()
    private val bluetoothUseCase = BluetoothUseCases()

    init {

        bluetoothIsDiscovering.value = false
        adapterBondedDevices.mItems = listOf()
        adapterNewDevices.mItems = listOf()

        listBluetoothDevices()
    }

    private fun validateBtRequirements() : Boolean {

        //Validate if bluetooth is available.
        var result = bluetoothUseCase.bluetoothManager.isBluetoothAvailable()

        if (!result) {

            //Nothing to do, bt is not present on current device
            return false
        }

        result = bluetoothUseCase.bluetoothManager.isBluetoothOn()

        if (!result) {

            bluetoothTurnOn.value = result
            return false
        }

        //Request permission location coarse in order to discover new devices
        //Android 6.0 is necessary coarse permission
        //Requires Manifest.permission.BLUETOOTH and Manifest.permission.ACCESS_COARSE_LOCATION to receive.
        //https://developer.android.com/reference/android/bluetooth/BluetoothDevice#ACTION_FOUND

        if (Build.VERSION.SDK_INT >= 23) {

            result = isLocationPermissionGranted()

            if(!result){
                //Request permission to obtain bt devices
                requestLocationPermission()
                return false
            }
        }

        permissionLocationGranted.value = true
        return true
    }

    private fun listBluetoothDevices() {

        val didRequirementsPassed = validateBtRequirements()

        if (didRequirementsPassed){

            //Clear lists
            reset()

            //List bluetooth devices paired
            bluetoothUseCase.getPairedBluetoothDevices()?.let { devices -> adapterBondedDevices.update(devices) }

            //Start to listen
            bluetoothUseCase.getNewBluetoothDevices(getApplication(), object : BluetoothReceiver.BtScanCallback{
                override fun onScanResult(device: Device) {

                    adapterNewDevices.update(device)
                }

                override fun onStartDiscovering() {

                    bluetoothIsDiscovering.value = true
                }

                override fun onStopDiscovering() {

                    bluetoothIsDiscovering.value = false
                }
            })
        }
    }

    private fun reset(){

        adapterBondedDevices.update(listOf())
        adapterNewDevices.update(listOf())
    }

    private fun isLocationPermissionGranted() : Boolean {

        return ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission() {

        this.permissionLocationGranted.value = false
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

    override fun onClickToolbarRightButton() {  listBluetoothDevices() }

    override fun onClickToolbarCenterButton() {}

    override fun onClickToolbarLeftButton() {}

    override fun onCleared() {

        //Stop discovering bt devices
        bluetoothUseCase.stop(getApplication())

        super.onCleared()
    }
}