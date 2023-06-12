package com.jm.tools.audio.audiocutter.data.model

import com.google.android.gms.ads.appopen.AppOpenAd

class OpenAdModel : BaseModel() {
    var openAd: AppOpenAd? = null

    var isLoading: Boolean = false

    var isShowing: Boolean = false

    var loadedAt: Int = 0
}