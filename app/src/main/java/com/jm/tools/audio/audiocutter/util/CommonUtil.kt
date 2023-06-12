package com.jm.tools.audio.audiocutter.util

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Environment
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.jm.tools.audio.audiocutter.BuildConfig
import com.jm.tools.audio.audiocutter.R
import com.jm.tools.audio.audiocutter.data.model.AppSettingsModel
import com.jm.tools.audio.audiocutter.util.extension.sharedPref
import com.jm.tools.audio.audiocutter.util.SharedPreferencesUtil.get
import com.jm.tools.audio.audiocutter.util.SharedPreferencesUtil.set
import com.jm.tools.audio.audiocutter.util.extension.toast
import java.util.*

object CommonUtil {

    private const val APP_SETTINGS_MODEL = "app_settings_model"

    fun getAppID(): String {
        return BuildConfig.APPLICATION_ID
    }

    fun isStoragePermissionGranted(ctx: Context): Boolean {
        return if (PermissionUtil.isApi30orHigher()) {
            Environment.isExternalStorageManager()
        } else {
            PermissionUtil.isGranted(
                ctx,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
    }

    fun showKeyboard(ctx: Context) {
        val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    fun closeKeyboard(activity: AppCompatActivity) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }

    fun closeKeyboardWhileClickOutSide(activity: AppCompatActivity, view: View?) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view?.setOnTouchListener { _, _ ->
                closeKeyboard(activity)
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                closeKeyboardWhileClickOutSide(activity, innerView)
            }
        }
    }

    fun getHeightOfStatusBar(activity: AppCompatActivity): Int {
        val resId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resId > 0) {
            activity.resources.getDimensionPixelSize(resId)
        } else {
            0
        }
    }

    fun getHeightOfNavigationBar(activity: AppCompatActivity): Int {
        val resId = activity.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resId > 0) {
            activity.resources.getDimensionPixelSize(resId)
        } else {
            0
        }
    }

    fun getRealScreenSizeAsPixels(activity: AppCompatActivity): Point {
        val display = if (PermissionUtil.isApi30orHigher()) {
            activity.display
        } else {
            activity.windowManager.defaultDisplay
        }

        val outPoint = Point()
        display?.getRealSize(outPoint)
        return outPoint
    }

    fun getRealScreenWidthAsPixel(activity: AppCompatActivity): Int {
        return if (PermissionUtil.isApi31orHigher()) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

    fun getRealScreenHeightAsPixel(activity: AppCompatActivity): Int {
        return if (PermissionUtil.isApi31orHigher()) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.bottom - insets.top
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }

    fun convertDpToPixel(ctx: Context?, vararg dimensionIds: Int): Int {
        var result = 0
        ctx?.run {
            for (id in dimensionIds) {
                result += resources.getDimension(id).toInt()
            }
        }

        return result
    }

    fun sendEmail(
        ctx: Context?, email: String, subject: String, content: String, bccEmail: String? = null
    ) {
        ctx?.also {
            if (email.isNotEmpty() && email != "null") {
                Intent(Intent.ACTION_SEND).apply {
                    type = "message/rfc822"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                    bccEmail?.run {
                        putExtra(Intent.EXTRA_BCC, arrayOf(bccEmail))
                    }
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, content)
                }.run {
                    try {
                        ctx.startActivity(
                            Intent.createChooser(
                                this,
                                ctx.getString(R.string.app_name)
                            )
                        )
                    } catch (ex: ActivityNotFoundException) {
                        ctx.toast("Error, no email composer found")
                    }
                }
            } else {
                ctx.toast("Invalid email")
            }
        }
    }

    fun shareText(ctx: Context?, body: String) {
        ctx?.also {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, ctx.getString(R.string.app_name))
                putExtra(Intent.EXTRA_TEXT, body)
            }.run {
                ctx.startActivity(Intent.createChooser(this, ctx.getString(R.string.share)))
            }
        }
    }

    fun openBrowser(ctx: Context?, url: String) {
        ctx?.run {
            val callIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (callIntent.resolveActivity(ctx.packageManager) != null) {
                ctx.startActivity(callIntent)
            } else {
                ctx.toast("No browser found")
            }
        }
        /* val intent = Intent(Intent.ACTION_VIEW).apply {
             data = Uri.parse(
                 "https://play.google.com/store/apps/details?id=com.example.android")
             setPackage("com.android.vending")
         }
         ctx.startActivity(intent)*/
    }

    fun setDefaultLanguage(ctx: Context) {
        with(ctx.resources) {
            configuration.setLocale(Locale.getDefault())
            ctx.createConfigurationContext(configuration)
        }
    }

    fun openAppInPlayStore(ctx: Context) {
        val uri = Uri.parse("market://details?id=${getAppID()}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )

        if (intent.resolveActivity(ctx.packageManager) != null) {
            ctx.startActivity(intent)
        } else {
            openBrowser(ctx, Constant.LINK_APP)
        }
    }

    fun getCDMACountryIso(): String? {
        try {
            val systemProperties = Class.forName("android.os.SystemProperties")
            val get = systemProperties.getMethod("get", String::class.java)

            // Get homeOperator that contain MCC + MNC
            val homeOperator =
                get.invoke(systemProperties, "ro.cdma.home.operator.numeric") as String?

            // First three characters (MCC) from homeOperator represents the country code
            val mcc = homeOperator?.let {
                try {
                    it.substring(0, 3).toInt()
                } catch (e: Exception) {
                    0
                }
            }

            val code = when (mcc) {
                330 -> {
                    "PR"
                }
                310 -> {
                    "US"
                }
                311 -> {
                    "US"
                }
                312 -> {
                    "US"
                }
                316 -> {
                    "US"
                }
                283 -> {
                    "AM"
                }
                460 -> {
                    "CN"
                }
                455 -> {
                    "MO"
                }
                414 -> {
                    "MM"
                }
                619 -> {
                    "SL"
                }
                450 -> {
                    "KR"
                }
                634 -> {
                    "SD"
                }
                434 -> {
                    "UZ"
                }
                232 -> {
                    "AT"
                }
                204 -> {
                    "NL"
                }
                262 -> {
                    "DE"
                }
                247 -> {
                    "LV"
                }
                255 -> {
                    "UA"
                }
                else -> null
            }

            return code
        } catch (e: Exception) {
            return null
        }
    }

    fun saveAppSettingsModel(ctx: Context?, model: AppSettingsModel) {
        ctx?.run {
            sharedPref[APP_SETTINGS_MODEL] = model.toJson()
        }
    }

    fun getAppSettingsModel(ctx: Context): AppSettingsModel {
        val json: String? = ctx.sharedPref[APP_SETTINGS_MODEL]
        return json?.run { Gson().fromJson(json, AppSettingsModel::class.java) }
            ?: AppSettingsModel()
    }
}