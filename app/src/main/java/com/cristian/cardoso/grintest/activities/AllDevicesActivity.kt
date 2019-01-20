package com.cristian.cardoso.grintest.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cristian.cardoso.grintest.R
import com.cristian.cardoso.grintest.databinding.ActivityAllDevicesBinding
import com.cristian.cardoso.grintest.models.commands.AllDevicesViewModelCommands
import com.cristian.cardoso.grintest.viewmodels.AllDevicesViewModel

class AllDevicesActivity : AppCompatActivity() {

    private val allDevicesViewModel: AllDevicesViewModel by lazy {
        ViewModelProviders.of(this).get(AllDevicesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityAllDevicesBinding = DataBindingUtil.setContentView(this, R.layout.activity_all_devices)
        binding.model = allDevicesViewModel
        binding.setLifecycleOwner(this)
        binding.executePendingBindings()

        lifecycle.addObserver(allDevicesViewModel)

        allDevicesViewModel.command.observe(this, Observer { command ->

            command.let {

                when (it) {
                    is AllDevicesViewModelCommands.GoBack -> {
                        finish()
                    }
                }
            }
        })
    }
}
