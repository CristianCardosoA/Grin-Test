package com.cristian.cardoso.grintest.viewholders

import android.support.v7.widget.RecyclerView
import com.cristian.cardoso.grintest.models.Device

class DeviceViewHolder(val binding : com.cristian.cardoso.grintest.databinding.ViewholderDeviceBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(device : Device){

        binding.device = device
    }
}