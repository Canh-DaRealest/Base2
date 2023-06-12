package com.jm.tools.audio.audiocutter.util

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.jm.tools.audio.audiocutter.R
import com.jm.tools.audio.audiocutter.util.extension.gone
import com.jm.tools.audio.audiocutter.util.extension.setOnSafeClickListener

object DialogUtil {

    fun showConfirmationDialog(
        ctx: Context?,
        textTitle: Any? = null,
        textOk: Any = ctx?.getString(R.string.ok) ?: "",
        textCancel: Any? = null,
        okListener: (() -> Unit)? = null,
        cancelListener: (() -> Unit)? = null,
        cancelable: Boolean = true
    ) {
        ctx?.run {
            createDialog(ctx, R.layout.dialog_confirmation, cancelable).run {
                val lblTitle = findViewById<TextView>(R.id.lbl_title)
                val btnOk = findViewById<TextView>(R.id.btn_ok)
                val btnCancel = findViewById<TextView>(R.id.btn_cancel)

                textTitle?.let {
                    lblTitle.text = when (it) {
                        is String -> it
                        is CharSequence -> it
                        is Int -> ctx.getString(it)
                        else -> ""
                    }
                }

                btnOk.text = when (textOk) {
                    is String -> textOk
                    is CharSequence -> textOk
                    is Int -> ctx.getString(textOk)
                    else -> ""
                }
                btnOk.setOnSafeClickListener {
                    if (isShowing) {
                        dismiss()
                        okListener?.invoke()
                    }
                }

                val strCancel = when (textCancel) {
                    is String -> textCancel
                    is CharSequence -> textCancel
                    is Int -> ctx.getString(textCancel)
                    else -> ""
                }
                if (strCancel.isEmpty() || strCancel.isBlank()) {
                    btnCancel.gone()
                } else {
                    btnCancel.text = strCancel
                    btnCancel.setOnSafeClickListener {
                        if (isShowing) {
                            dismiss()
                            cancelListener?.invoke()
                        }
                    }
                }

                if (!isShowing) {
                    show()
                }
            }
        }
    }

    fun showStoragePermissionManualDialog(ctx: Context?, okListener: () -> Unit) {
        ctx?.run {
            createDialog(ctx, R.layout.dialog_ask_manual_storage_permission, false).run {
                val btnOpenSettings = findViewById<TextView>(R.id.btn_open_settings)
                val btnCancel = findViewById<TextView>(R.id.btn_cancel)

                btnOpenSettings.setOnSafeClickListener {
                    if (isShowing) {
                        dismiss()
                    }
                    okListener()
                }

                btnCancel.setOnSafeClickListener {
                    if (isShowing) {
                        dismiss()
                    }
                }

                if (!isShowing) {
                    show()
                }
            }
        }
    }

    private fun createDialog(ctx: Context, resLayout: Int, cancelable: Boolean): Dialog {
        return Dialog(ctx).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawableResource(R.color.transparent)
            setContentView(resLayout)
            window?.setLayout(
                CommonUtil.convertDpToPixel(ctx, R.dimen.dimen_340),
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setCancelable(cancelable)
        }
    }

}