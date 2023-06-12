package com.jm.tools.audio.audiocutter.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import com.jm.tools.audio.audiocutter.R
import com.jm.tools.audio.audiocutter.util.extension.visible

class MyProgressDialog(ctx: Context) : Dialog(ctx) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawableResource(R.color.transparent)
        setContentView(R.layout.layout_progressbar)
    }

    /*
    * Must call this fun after calling show()
    * */
    fun setMessage(msg: String) {
        val lblMsg = findViewById<TextView>(R.id.lbl_msg)
        lblMsg.apply {
            visible()
            text = msg
        }
    }
}
