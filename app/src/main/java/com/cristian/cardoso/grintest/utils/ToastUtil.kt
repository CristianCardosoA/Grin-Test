package com.cristian.cardoso.grintest.utils

import android.content.Context
import android.widget.Toast

object ToastUtil {

    private var mToast: Toast? = null

    fun show(context: Context, text: String, duration: Int) {
        if (mToast != null){
            mToast?.cancel()
        }
        mToast = Toast.makeText(context, text, duration)
        mToast?.show()
    }
}