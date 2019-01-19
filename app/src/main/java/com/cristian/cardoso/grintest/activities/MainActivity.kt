package com.cristian.cardoso.grintest.activities

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.cristian.cardoso.grintest.R
import com.cristian.cardoso.grintest.databinding.ActivityMainBinding
import com.cristian.cardoso.grintest.utils.BluetoothManager
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

        mainViewModel.bluetoothTurnOn.observe(this, Observer { isBluetoothOn ->

            isBluetoothOn?.let {

                if (!it) {

                    BluetoothManager.requestEnableBluetooth(this)
                }
            }
        })

        mainViewModel.permissionLocationGranted.observe(this, Observer { request ->

            request?.let { it ->

                if (!it) {

                    val arrayPermissions = Array(size = 1, init = { Manifest.permission.ACCESS_COARSE_LOCATION })

                    ActivityCompat.requestPermissions(this, arrayPermissions, BluetoothManager.REQUEST_LOCATION_PERMISSION)
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
