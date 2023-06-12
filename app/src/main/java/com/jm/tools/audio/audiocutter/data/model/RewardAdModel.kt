package com.jm.tools.audio.audiocutter.data.model

import com.google.android.gms.ads.rewarded.RewardedAd

class RewardAdModel : BaseModel() {
    var rewardAd: RewardedAd? = null

    var isLoading: Boolean = false
}