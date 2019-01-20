package com.cristian.cardoso.grintest.dialogs

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.cristian.cardoso.grintest.R
import com.cristian.cardoso.grintest.databinding.DialogSaveDeviceBinding
import com.cristian.cardoso.grintest.interfaces.DialogCallback
import com.cristian.cardoso.grintest.viewmodels.DialogViewModel


class DialogOKCancel : DialogFragment() {

    private var rightButtonText : String? = null
    private var leftButtonText: String? = null
    private var titleText: String? = null
    private var callback : DialogCallback? = null
    var binding : DialogSaveDeviceBinding? = null

    companion object {

        const val TAG = "com.cristian.cardoso.grintest.viewmodels.DialogOKCancel"
        private const val RIGHT_BUTTON_TEXT = "com.cristian.cardoso.grintest.viewmodels.DialogOKCancel.rightButtonText"
        private const val LEFT_BUTTON_TEXT = "com.cristian.cardoso.grintest.viewmodels.DialogOKCancel.leftButtonText"
        private const val TITLE_TEXT = "com.cristian.cardoso.grintest.viewmodels.DialogOKCancel.title"
        private const val CALLBACK = "com.cristian.cardoso.grintest.viewmodels.DialogOKCancel.callback"

        fun newInstance(titleText : String? = "", rightButtonText : String, leftButtonText: String, callback: DialogCallback) : DialogOKCancel {
            val fragment = DialogOKCancel()
            val args = Bundle()
            args.putString(RIGHT_BUTTON_TEXT, rightButtonText)
            args.putString(LEFT_BUTTON_TEXT, leftButtonText)
            args.putString(TITLE_TEXT, titleText)
            args.putSerializable(CALLBACK, callback)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rightButtonText = arguments?.getString(RIGHT_BUTTON_TEXT, "")
        leftButtonText = arguments?.getString(LEFT_BUTTON_TEXT, "")
        titleText =  arguments?.getString(TITLE_TEXT, "")
        callback = arguments?.getSerializable(CALLBACK) as DialogCallback
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater,
                              @Nullable container: ViewGroup?,
                              @Nullable savedInstanceState: Bundle?): View? {

        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.BLACK))

        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_save_device, container,false)
        binding?.model = DialogViewModel(this, callback, titleText, rightButtonText ?: "", leftButtonText ?: "")
        return binding?.root
    }

    fun block(){

        binding?.button2?.isEnabled = false
        binding?.button3?.isEnabled = false
    }

    fun desblock(){

        binding?.button2?.isEnabled = true
        binding?.button3?.isEnabled = true
    }

    fun showProgress(){

        binding?.progressBar2?.visibility = View.VISIBLE
    }

    fun hideProgress(){

        binding?.progressBar2?.visibility = View.GONE

    }
}