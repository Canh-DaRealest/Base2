package com.jm.tools.audio.audiocutter.util

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.jm.tools.audio.audiocutter.BuildConfig

class EventTrackingManager {

    companion object {
        const val NETWORK_YES = "network_yes"
        const val NETWORK_NO = "network_no"

        private var instance: EventTrackingManager? = null
        private var firebaseAnalytics: FirebaseAnalytics? = null

        fun getInstance(ctx: Context): EventTrackingManager {
            if (instance == null) {
                instance = EventTrackingManager()
            }

            if (!BuildConfig.DEBUG) {
                if (firebaseAnalytics == null) {
                    firebaseAnalytics = FirebaseAnalytics.getInstance(ctx)
                }
            }

            return instance ?: EventTrackingManager()
        }
    }

    fun logEvent(event: String) {
        if (!BuildConfig.DEBUG) {
            firebaseAnalytics?.logEvent(event, null)
        }
    }
}