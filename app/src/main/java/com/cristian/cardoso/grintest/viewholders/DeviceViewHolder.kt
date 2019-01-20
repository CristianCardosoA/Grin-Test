package com.cristian.cardoso.grintest.viewholders

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cristian.cardoso.grintest.R
import com.cristian.cardoso.grintest.databinding.ViewholderDeviceBinding
import com.cristian.cardoso.grintest.dialogs.DialogOKCancel
import com.cristian.cardoso.grintest.interfaces.DialogCallback
import com.cristian.cardoso.grintest.models.Device
import com.cristian.cardoso.grintest.usecases.BluetoothUseCases
import kotlinx.coroutines.*


class DeviceViewHolder(private val binding : ViewholderDeviceBinding, private val allowToSave : Boolean = false) : RecyclerView.ViewHolder(binding.root){

    private var device : Device? = null

    private var dialogCallback = object : DialogCallback {

        override fun onClickRightButton(dialogFragment: DialogOKCancel) {

            dialogFragment.showProgress()
            dialogFragment.block()

            device?.let {

                CoroutineScope(Dispatchers.IO).launch {

                    try {

                        val device = BluetoothUseCases().saveBluetoothDeviceToAPI(it)

                        if(device != null){

                            withContext(Dispatchers.Main) {

                                dialogFragment.dismiss()
                            }

                        } else {

                            withContext(Dispatchers.Main) {

                                dialogFragment.hideProgress()
                                dialogFragment.desblock()
                            }
                        }

                    } catch (e : Exception){

                        withContext(Dispatchers.Main) {

                            dialogFragment.hideProgress()
                            dialogFragment.desblock()
                        }
                    }
                }
            }
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