package com.cristian.cardoso.grintest.adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cristian.cardoso.grintest.databinding.ViewholderDeviceBinding
import com.cristian.cardoso.grintest.models.Device
import com.cristian.cardoso.grintest.viewholders.DeviceViewHolder
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor

class DeviceAdapter : RecyclerView.Adapter<DeviceViewHolder>() {

    var mItems : List<Device> = listOf()
    private var mRecyclerView: RecyclerView? = null
    private val diffCallback by lazy(LazyThreadSafetyMode.NONE) { DiffCallback() }

    private inner class DiffCallback : DiffUtil.Callback() {
        lateinit var newList: List<Device>
        override fun getOldListSize() = mItems.size
        override fun getNewListSize() = newList.size
        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = true
        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = mItems[oldItemPosition] == newList[newItemPosition]
    }

    private suspend fun internalUpdate(list: List<Device>) {

        val result = DiffUtil.calculateDiff(diffCallback.apply { newList = list }, false)
        GlobalScope.launch(Dispatchers.Main) {
            mItems = list
            result.dispatchUpdatesTo(this@DeviceAdapter)
        }.join()
    }

    @ObsoleteCoroutinesApi
    fun CoroutineScope.counterActor() = actor<List<Device>>(capacity = Channel.CONFLATED) {
        for (list in channel) internalUpdate(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {

        val binding = ViewholderDeviceBinding.inflate(LayoutInflater.from(parent.context), parent ,false)
        return DeviceViewHolder(binding)
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun getItemId(position: Int): Long {
        return mItems[position].hashCode().toLong()
    }

    fun update(device : Device){

        val deviceAlreadyExists = mItems.any { it.address.equals(device.address) }
        if(!deviceAlreadyExists){
            val newList = mItems.toMutableList()
            newList.add(device)
            update(newList)
        }
    }

    fun update (list: List<Device>) = GlobalScope.counterActor().offer(list)

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {

        mItems[position].let {

            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }
}