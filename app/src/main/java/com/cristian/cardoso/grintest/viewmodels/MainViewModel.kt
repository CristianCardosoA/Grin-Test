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
import com.cristian.cardoso.grintest.interfaces.BtScanCallback
import com.cristian.cardoso.grintest.interfaces.ToolbarCallback
import com.cristian.cardoso.grintest.models.Device
import com.cristian.cardoso.grintest.models.SingleLiveEvent
import com.cristian.cardoso.grintest.models.commands.MainViewModelCommands
import com.cristian.cardoso.grintest.usecases.BluetoothUseCases
import com.cristian.cardoso.grintest.utils.BluetoothManager

class MainViewModel(application: Application) : AndroidViewModel(application), ToolbarCallback, LifecycleObserver {

    val toolbarViewModel : ToolbarViewModel = ToolbarViewModel(this, R.drawable.bluetooth, null, R.drawable.ic_refresh_black_24dp)
    val bluetoothIsDiscovering = MutableLiveData<Boolean>()
    var permissionLocationGranted = MutableLiveData<Boolean>()
    val command = SingleLiveEvent<MainViewModelCommands>()
    val adapterBondedDevices = DeviceAdapter(true)
    val adapterNewDevices = DeviceAdapter(true)
    private val bluetoothUseCase = BluetoothUseCases()

    init {

        permissionLocationGranted.value = false
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
            command.value = MainViewModelCommands.Error(getApplication<Application>().getString(R.string.error_device_does_not_support_bluetooth))
            return false
        }

        result = bluetoothUseCase.bluetoothManager.isBluetoothOn()

        if (!result) {

            command.value = MainViewModelCommands.TurnOnBluetooth
            return false
        }

        //Request permission location coarse in order to discover new devices
        //Android 6.0 is necessary coarse location permission
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

            reset()

            //List bluetooth devices paired
            bluetoothUseCase.getPairedBluetoothDevices()?.let { devices ->
                adapterBondedDevices.update(devices)
            }

            //Start to listen
            bluetoothUseCase.getNewBluetoothDevices(getApplication(), object : BtScanCallback {

                override fun onBtDisconnected() {
                    reset()
                }

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

        adapterBondedDevices.clear()
        adapterNewDevices.clear()
    }

    private fun isLocationPermissionGranted() : Boolean {

        return ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
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


    fun requestLocationPermission(){

        command.value = MainViewModelCommands.RequestLocationPermission
    }

    override fun onClickToolbarRightButton() = if(bluetoothIsDiscovering.value == false){
        listBluetoothDevices()
    } else {
        command.value = MainViewModelCommands.Error(getApplication<Application>().getString(R.string.error_bt_discovering))
    }

    override fun onClickToolbarCenterButton() {
        command.value = MainViewModelCommands.GoToAllDevicestActivity
    }

    override fun onClickToolbarLeftButton() {}

    override fun onCleared() {

        //Stop discovering bt devices
        bluetoothUseCase.stop(getApplication())

        super.onCleared()
    }
}