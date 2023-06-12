package com.jm.tools.audio.audiocutter.util.extension

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.LocaleList
import android.telephony.TelephonyManager
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import com.jm.tools.audio.audiocutter.BuildConfig
import com.jm.tools.audio.audiocutter.data.model.AdsConfigModel
import com.jm.tools.audio.audiocutter.data.model.AppSettingsModel
import com.jm.tools.audio.audiocutter.util.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


inline val Context.ctx: Context
    get() = this

inline val Context.sharedPref: SharedPreferences
    get() = MyApplication.instance.sharedPref

inline val Context.appSettingsModel: AppSettingsModel
    get() = MyApplication.instance.appSettingsModel

inline val Context.adsConfigModel: AdsConfigModel
    get() = MyApplication.instance.adsConfigModel

inline val Context.eventTracking: EventTrackingManager
    get() = MyApplication.instance.eventTracking

inline val Context.adsManager: AdsManager
    get() = MyApplication.instance.adsManager

inline val Context.currentTimeInSecond: Int
    get() = (System.currentTimeMillis() / 1000).toInt()

inline val Context.dateFormat: String
    get() = (DateFormat.getDateFormat(this) as SimpleDateFormat).toLocalizedPattern()

inline val Context.timeFormat: String
    get() = (DateFormat.getTimeFormat(this) as SimpleDateFormat).toLocalizedPattern()

inline val Context.dateTimeFormat: String
    get() = "$dateFormat $timeFormat"

inline val Context.hasFrontCamera: Boolean
    get() = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)

inline val Context.manufacturer: String
    get() = Build.MANUFACTURER

inline val Context.isXiaomiDevice: Boolean
    get() = manufacturer.equals("Xiaomi", true)

inline val Context.isSamsungDevice: Boolean
    get() = manufacturer.equals("Samsung", true)

inline val Context.isOppoDevice: Boolean
    get() = manufacturer.equals("Oppo", true)

inline val Context.isVivoDevice: Boolean
    get() = manufacturer.equals("Vivo", true)

fun Context.isRtlLayout(): Boolean {
    val langCode = Locale.getDefault().language
    return langCode.equals("ar", true) || langCode.equals("fa", true) || langCode.equals("ur", true)
}

fun Context.isLowMemory(targetMemory: Int): Boolean {
    // Declaring and Initializing the ActivityManager
    val actManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager

    // Declaring MemoryInfo object
    val memInfo = ActivityManager.MemoryInfo()

    // Fetching the data from the ActivityManager
    actManager.getMemoryInfo(memInfo)

    // Fetching the available and total memory and converting into Giga Bytes
//    val availMemory = memInfo.availMem.toDouble() / (1024 * 1024 * 1024)
    val totalMemory = memInfo.totalMem.toDouble() / (1024 * 1024 * 1024)

    logE("totalMemory = $totalMemory")
    return totalMemory < targetMemory // GB
}

fun Context.shouldShowAds(): Boolean {
    return adsConfigModel.isAdsEnabled && !appSettingsModel.didRemoveAds
            && currentTimeInSecond > appSettingsModel.lastTimeAdClicked
}

fun Context.networkIsConnected(): Boolean {
    try {
        val conMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return conMgr?.let {
            return if (PermissionUtil.isApi29orHigher()) {
                val capabilities = it.getNetworkCapabilities(it.activeNetwork)
                capabilities?.run {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                } ?: false
            } else {
                it.activeNetworkInfo?.isConnected ?: false
            }
        } ?: false
    } catch (e: Exception) {
        logE("$e")
    }

    return false
}

fun Context.updateLocale(context: Context, languageCode: String?): ContextWrapper {
    var ctx = context
    languageCode?.run {
        val resources: Resources? = ctx.resources
        val configuration: Configuration? = resources?.configuration
        val localeToSwitchTo = if (languageCode != "sys") {
            if (languageCode == "zh-rTW") {
                Locale.TAIWAN
            } else {
                Locale(languageCode)
            }
        } else {
            Locale(appSettingsModel.defaultLanguage ?: Locale.getDefault().language)
        }
        if (PermissionUtil.isApi24orHigher()) {
            val localeList = LocaleList(localeToSwitchTo)
            LocaleList.setDefault(localeList)
            configuration?.setLocales(localeList)
        } else {
            configuration?.locale = localeToSwitchTo
        }

        configuration?.run {
            configuration.setLayoutDirection(localeToSwitchTo)
            if (PermissionUtil.isApi25orHigher()) {
                ctx = ctx.createConfigurationContext(configuration)
            } else {
                resources.updateConfiguration(configuration, resources.displayMetrics)
            }
        }
    }
    return ContextWrapper(ctx)
}

fun Context.isNotReachMaxInterAdInSession(): Boolean {
    return appSettingsModel.numberOfInterAdShownInSession < appSettingsModel.maxInterAdShownInSession
}

fun Context.resetInterAdSession(forceReset: Boolean = false) {
    appSettingsModel.apply {
        if (forceReset) {
            numberOfInterAdShownInSession = 0
            interAdSessionStartedAt = 0
            CommonUtil.saveAppSettingsModel(ctx, appSettingsModel)
        } else if (currentTimeInSecond - interAdSessionStartedAt > interAdSession) {
            numberOfInterAdShownInSession = 0
            CommonUtil.saveAppSettingsModel(ctx, appSettingsModel)
        }
    }
}

fun Context.openOtherPermissionsPageOnXiaomiDevice() {
    try {
        Intent("miui.intent.action.APP_PERM_EDITOR").apply {
            setClassName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity"
            )
            putExtra("extra_pkgname", packageName)
        }.run {
            startActivity(this)
        }
    } catch (e: Exception) {
    }
}

fun Context.getCountryCode(): String? {
    val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
    val countryCode = telephonyManager?.run {
        when {
            simCountryIso != null && simCountryIso.length == 2 -> {
                simCountryIso
            }
            phoneType == TelephonyManager.PHONE_TYPE_CDMA -> {
                CommonUtil.getCDMACountryIso()
            }
            else -> {
                networkCountryIso
            }
        }
    }

    return if (countryCode?.length == 2) {
        countryCode
    } else {
        if (PermissionUtil.isApi24orHigher()) {
            try {
                resources.configuration.locales[0].country
            } catch (e: Exception) {
                null
            }
        } else {
            resources.configuration.locale.country
        }
    }?.lowercase()
}

fun Context.logE(msg: Any?) {
    if (BuildConfig.DEBUG) {
        val strMsg = when (msg) {
            is String -> msg
            else -> msg.toString()
        }
        Log.e(javaClass.simpleName, strMsg)
    }
}

fun Context.toast(msg: Any?, isLongToast: Boolean = false) {
    val message = when (msg) {
        is Int -> getString(msg)
        is Char -> msg.toString()
        is CharSequence -> msg.toString()
        is String -> msg
        else -> "Error: message type is not supported"
    }
    val length = if (isLongToast) {
        Toast.LENGTH_LONG
    } else {
        Toast.LENGTH_SHORT
    }
    Toast.makeText(this, message, length).show()
}

fun Context.refreshMediaStore(filePath: String) {
    if (!PermissionUtil.isApi30orHigher()) {
        MediaScannerConnection.scanFile(
            ctx, arrayOf(filePath), null, null
        )
    } else {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val file = File(filePath)
        val contentUri = Uri.fromFile(file)
        mediaScanIntent.data = contentUri
        this.sendBroadcast(mediaScanIntent)
    }
}