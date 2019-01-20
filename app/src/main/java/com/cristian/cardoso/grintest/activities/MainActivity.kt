package com.cristian.cardoso.grintest.activities

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.cristian.cardoso.grintest.R
import com.cristian.cardoso.grintest.databinding.ActivityMainBinding
import com.cristian.cardoso.grintest.models.commands.MainViewModelCommands
import com.cristian.cardoso.grintest.utils.BluetoothManager
import com.cristian.cardoso.grintest.utils.ToastUtil
import com.cristian.cardoso.grintest.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.model = mainViewModel
        binding.setLifecycleOwner(this)
        binding.executePendingBindings()

        lifecycle.addObserver(mainViewModel)

        mainViewModel.command.observe(this, Observer { command ->

            command.let {

                when(it){

                    is MainViewModelCommands.GoToAllDevicestActivity -> {
                        val i = Intent(this, AllDevicesActivity::class.java)
                        startActivity(i)
                    }
                    is MainViewModelCommands.Error -> {
                        ToastUtil.show(this, it.errorString, Toast.LENGTH_LONG)
                    }

                    is MainViewModelCommands.TurnOnBluetooth -> {
                        BluetoothManager.requestEnableBluetooth(this)
                    }

                    is MainViewModelCommands.RequestLocationPermission -> {

                        val arrayPermissions = Array(size = 1, init = { Manifest.permission.ACCESS_COARSE_LOCATION })

                        ActivityCompat.requestPermissions(this, arrayPermissions, BluetoothManager.REQUEST_LOCATION_PERMISSION)
                    }
                }
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        mainViewModel.onRequestPermissionResult(requestCode, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        mainViewModel.handleActivityResult(requestCode, resultCode)
    }
}
