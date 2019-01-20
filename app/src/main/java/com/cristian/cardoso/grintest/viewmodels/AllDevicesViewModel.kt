package com.cristian.cardoso.grintest.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.support.v4.widget.SwipeRefreshLayout
import com.cristian.cardoso.grintest.R
import com.cristian.cardoso.grintest.adapters.DeviceAdapter
import com.cristian.cardoso.grintest.interfaces.ToolbarCallback
import com.cristian.cardoso.grintest.models.SingleLiveEvent
import com.cristian.cardoso.grintest.models.commands.AllDevicesViewModelCommands
import com.cristian.cardoso.grintest.usecases.BluetoothUseCases
import kotlinx.coroutines.*

class AllDevicesViewModel(application: Application) : AndroidViewModel(application), ToolbarCallback, LifecycleObserver, SwipeRefreshLayout.OnRefreshListener {

    val toolbarViewModel : ToolbarViewModel = ToolbarViewModel(this, null, R.drawable.ic_arrow_back_black_24dp, R.drawable.ic_filter_list_black_24dp)
    val adapterDevices = DeviceAdapter(false)
    private val bluetoothUseCase = BluetoothUseCases()
    val progressDiscovering = MutableLiveData<Boolean>()
    val command = SingleLiveEvent<AllDevicesViewModelCommands>()
    private var jobAPI : Job? = null
    private var filterNewest = true

    init {

        jobAPI = loadData()
        jobAPI?.start()
        adapterDevices.mItems = listOf()
    }

    override fun onClickToolbarCenterButton() {}

    override fun onClickToolbarLeftButton() {

        command.value = AllDevicesViewModelCommands.GoBack
    }

    private fun loadData() : Job {

        return CoroutineScope(Dispatchers.IO).launch {

            adapterDevices.clear()
            progressDiscovering.postValue(true)

            try {

                val devicesAPI = bluetoothUseCase.getBluetoothDevicesAPI()

                withContext(Dispatchers.Main) {

                    devicesAPI?.let {
                        adapterDevices.update(it)
                    }

                    progressDiscovering.value = false
                }

            } catch (e : Exception){

                progressDiscovering.postValue(false)
            }
        }
    }

    override fun onRefresh() {

        jobAPI = loadData()
        jobAPI?.start()
    }

    override fun onClickToolbarRightButton() {

        adapterDevices.orderByDate(getApplication<Application>().getString(R.string.format_date_device), filterNewest)
        filterNewest = !filterNewest
    }

    override fun onCleared() {

        jobAPI?.cancel()
        super.onCleared()
    }
}