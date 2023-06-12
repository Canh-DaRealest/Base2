package com.jm.tools.audio.audiocutter.util

import android.app.Application
import com.jm.tools.audio.audiocutter.data.model.AdsConfigModel
import com.jm.tools.audio.audiocutter.util.extension.ctx

class MyApplication : Application() {

    val sharedPref by lazy { SharedPreferencesUtil.customPrefs(ctx) }
    val appSettingsModel by lazy { CommonUtil.getAppSettingsModel(ctx) }
    val adsConfigModel by lazy { AdsConfigModel() }
    val eventTracking by lazy { EventTrackingManager.getInstance(this) }
    val adsManager by lazy { AdsManager(ctx) }

    companion object {
        lateinit var instance: MyApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Init mobile ads SDK
        adsManager.initMobileAdSdk()
    }
}