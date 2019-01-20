package com.cristian.cardoso.grintest.viewholders

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cristian.cardoso.grintest.R
import com.cristian.cardoso.grintest.databinding.ViewholderDeviceBinding
import com.cristian.cardoso.grintest.dialogs.DialogOKCancel
import com.cristian.cardoso.grintest.interfaces.DialogCallback
import com.cristian.cardoso.grintest.models.Device
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DeviceViewHolder(private val binding : ViewholderDeviceBinding, private val allowToSave : Boolean = false) : RecyclerView.ViewHolder(binding.root){

    private var device : Device? = null

    private var dialogCallback = object : DialogCallback {

        override fun onClickRightButton(dialogFragment: DialogOKCancel) {

            dialogFragment.showProgress()

            /*device?.let {

                CoroutineScope(Dispatchers.IO).launch {

                    //BluetoothUseCases().saveBluetoothDeviceToAPI(it)

                    withContext(Dispatchers.Main) {

                        dialogFragment.dismiss()
                    }
                }
            }*/
        }

        override fun onClickLeftButton(dialogFragment: DialogOKCancel) {

            dialogFragment.dismiss()
        }
    }

    private var onAddButtonClickListener = View.OnClickListener {

        (itemView.context as? AppCompatActivity)?.let { activity ->

            val dialog = DialogOKCancel.newInstance(
                    device?.name,
                    activity.getString(R.string.dialog_save_device_right_button),
                    activity.getString(R.string.dialog_save_device_left_button),
                    dialogCallback
            )

            dialog.show(activity.supportFragmentManager, DialogOKCancel.TAG)
        }
    }

    fun bind(device : Device){

        this.device = device
        binding.device = device
        binding.allowToSave = allowToSave
        binding.callBack = onAddButtonClickListener
    }
}