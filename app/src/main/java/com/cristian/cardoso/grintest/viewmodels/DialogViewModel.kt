package com.cristian.cardoso.grintest.viewmodels

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.ViewModel
import com.cristian.cardoso.grintest.dialogs.DialogOKCancel
import com.cristian.cardoso.grintest.interfaces.DialogCallback

class DialogViewModel(private val dialog : DialogOKCancel,
                      private val handler : DialogCallback?,
                      val title : String?,
                      val rightTextButton : String,
                      val leftTextButton : String) : ViewModel(), LifecycleObserver {

    fun onLeftItemClicked() {
        handler?.onClickLeftButton(dialog)
    }

    fun onRightItemClicked() {
        handler?.onClickRightButton(dialog)
    }
}