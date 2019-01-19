package com.cristian.cardoso.grintest.viewmodels

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.ViewModel
import com.cristian.cardoso.grintest.interfaces.ToolbarCallback

class ToolbarViewModel(val handler : ToolbarCallback?,
                       val centerItemResource : Int?,
                       val leftItemResource : Int?,
                       val rightItemResource : Int?) : ViewModel(), LifecycleObserver {

    fun onLeftItemClicked() {
        handler?.onClickToolbarLeftButton()
    }

    fun onRightItemClicked() {
        handler?.onClickToolbarRightButton()
    }

    fun onCenterItemClicked(){
        handler?.onClickToolbarCenterButton()
    }
}