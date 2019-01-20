package com.cristian.cardoso.grintest.interfaces

import com.cristian.cardoso.grintest.dialogs.DialogOKCancel
import java.io.Serializable

interface DialogCallback : Serializable {

    fun onClickRightButton(dialogFragment: DialogOKCancel)
    fun onClickLeftButton(dialogFragment: DialogOKCancel)
}