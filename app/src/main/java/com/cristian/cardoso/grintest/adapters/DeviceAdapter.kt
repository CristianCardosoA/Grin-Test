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
import java.text.SimpleDateFormat
import java.util.*

class DeviceAdapter(var allowToSave : Boolean): RecyclerView.Adapter<DeviceViewHolder>() {

    var mItems : List<Device> = listOf()
    private var mRecyclerView: RecyclerView? = null
    private val diffCallback by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DiffCallback() }

    private inner class DiffCallback : DiffUtil.Callback() {
        lateinit var newList: List<Device>
        override fun getOldListSize() = mItems.size
        override fun getNewListSize() = newList.size
        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = true
        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = mItems[oldItemPosition] == newList[newItemPosition]
    }

    private suspend fun internalUpdate(list: List<Device>) {

        val result = DiffUtil.calculateDiff(diffCallback.apply { newList = list }, false)
        CoroutineScope(Dispatchers.Main).launch {
            mItems = list
            result.dispatchUpdatesTo(this@DeviceAdapter)
        }.join()
    }

    @ObsoleteCoroutinesApi
    fun CoroutineScope.counterActor() = actor<List<Device>>(capacity = Channel.UNLIMITED) {
        for (list in channel) internalUpdate(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {

        val binding = ViewholderDeviceBinding.inflate(LayoutInflater.from(parent.context), parent ,false)
        return DeviceViewHolder(binding, allowToSave)
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

    fun clear(){

        mItems = listOf()
        notifyDataSetChanged()
    }

    fun orderByDate(pattern : String, newest : Boolean) {

        val dateTimeStrToLocalDateTime: (String) -> Date = {

            val parser = SimpleDateFormat(pattern, Locale.getDefault())
            parser.parse(it)
        }

        val tmp = mItems.toMutableList()
        tmp.sortWith(Comparator { p1, p2 ->
            when {
                p1.created_at == null || p2.created_at == null -> -1
                dateTimeStrToLocalDateTime(p1.created_at).before(dateTimeStrToLocalDateTime(p2.created_at)) -> {
                    if(newest){-1 } else { 1 }
                }
                else -> -1
            }
        })
        update(tmp)
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