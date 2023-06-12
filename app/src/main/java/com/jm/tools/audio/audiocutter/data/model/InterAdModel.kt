package com.jm.tools.audio.audiocutter.data.model

import com.google.android.gms.ads.interstitial.InterstitialAd

class InterAdModel : BaseModel() {
    var interstitialAd: InterstitialAd? = null

    var isLoading: Boolean = false

    var loadedAt: Int = 0
}