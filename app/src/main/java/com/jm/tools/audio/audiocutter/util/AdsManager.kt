package com.jm.tools.audio.audiocutter.util

import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.nativead.NativeAd
import com.jm.tools.audio.audiocutter.data.model.InterAdModel
import com.jm.tools.audio.audiocutter.data.model.OpenAdModel
import com.jm.tools.audio.audiocutter.data.model.RewardAdModel
import com.jm.tools.audio.audiocutter.util.extension.adsConfigModel
import com.jm.tools.audio.audiocutter.util.extension.appSettingsModel
import com.jm.tools.audio.audiocutter.util.extension.currentTimeInSecond
import com.jm.tools.audio.audiocutter.util.extension.logE

class AdsManager(private val ctx: Context) {

    companion object {
        private const val VALID_TIME_TO_SHOW_NATIVE_AD = 60 * 60
        private const val VALID_TIME_TO_SHOW_OPEN_AD = 4 * 60 * 60
        private const val VALID_TIME_TO_SHOW_INTER_AD = 4 * 60 * 60

        private const val NATIVE_AD_UNIT_TEST = "ca-app-pub-3940256099942544/2247696110"
        private const val INTERSTITIAL_AD_UNIT_TEST = "ca-app-pub-3940256099942544/1033173712"
        private const val BANNER_AD_UNIT_TEST = "ca-app-pub-3940256099942544/6300978111"
        private const val OPEN_AD_UNIT_TEST = "ca-app-pub-3940256099942544/3419835294"
        private const val REWARD_INTER_AD_UNIT_TEST = "ca-app-pub-3940256099942544/5354046379"
        private const val REWARD_AD_UNIT_TEST = "ca-app-pub-3940256099942544/5224354917"
    }

    var isJustClickedAd = false

    private val INTER_AD_HOME_ITEM_SELECT_PHOTO = "home_item_select_photo"
    private val INTER_AD_HOME_ITEM_SAVED_PHOTO = "home_item_saved_photo"
    private val INTER_AD_PHOTO_SELECT = "photo_select"
    private val INTER_AD_FONT_LIST_OPEN = "font_list_open"
    private val INTER_AD_FONT_LIST_BACK = "font_list_back"
    private val INTER_AD_STICKER_LIST_OPEN = "sticker_list_open"
    private val INTER_AD_STICKER_LIST_BACK = "sticker_list_back"
    private val INTER_AD_PHOTO_CHANGE = "photo_change"
    private val INTER_AD_TEXT_ADD_BACK = "text_add_back"
    private val INTER_AD_TEXT_ADD_OK = "text_add_ok"
    private val INTER_AD_RESULT_FULLSCREEN_BACK = "result_fullscreen_back"
    private val INTER_AD_PHOTO_VIEWER_BACK = "photo_viewer_back"
    private val INTER_AD_ADS_REMOVE_BACK = "ads_remove_back"
    private val INTER_AD_EDITOR_ADD_TEXT = "editor_add_text"
    private val INTER_AD_EDITOR_DECORATE = "editor_decorate"
    private val INTER_AD_EDITOR_EDIT_IMAGE = "editor_edit_image"
    private val INTER_AD_EDITOR_SAVE = "editor_save"
    private val INTER_AD_EDITOR_BACK = "editor_back"
    private val INTER_AD_SAVED_PHOTO_BACK = "saved_photo_back"
    private val INTER_AD_RESULT_BACK = "result_back"
    private val INTER_AD_OK_WALLPAPER_CLICK = "ok_wallpaper_click"
    private val interstitialAdGroup by lazy { HashMap<String, InterAdModel>() }
    private var isInterAdShowing = false

    private val OPEN_AD_SPLASH = "open_ad_splash"
    private val OPEN_AD_EDITOR = "open_ad_editor"
    private val OPEN_AD_RESUME = "open_ad_resume"
    private val openAdGroup by lazy { HashMap<String, OpenAdModel>() }

    private val REWARD_AD_FONT = "reward_ad_font"
    private val REWARD_AD_TEXT_COLOR = "reward_ad_text_color"
    private val rewardAdGroup by lazy { HashMap<String, RewardAdModel>() }

    fun initMobileAdSdk() {
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(
                    listOf(
                        "5796579D7981F87A7537273034F67B6D",
                        "6DCBB423DD9BBD392D547DBB76F140A7"
                    )
                )
                .build()
        )
        MobileAds.initialize(ctx) {}
    }

    fun destroyNativeAdHome() {
//        destroyNativeAd(nativeAdHome)
//        nativeAdHome = null
    }

    fun destroyNativeAdResult() {
//        destroyNativeAd(nativeAdResult)
//        nativeAdResult = null
    }

    fun release() {
        destroyNativeAdHome()
        destroyNativeAdResult()

        // Clear interstitial ads
        if (interstitialAdGroup.isNotEmpty()) {
            for (model in interstitialAdGroup.values) {
                model.interstitialAd = null
                model.isLoading = false
            }
            interstitialAdGroup.clear()
        }

        // Clear open ads
        if (openAdGroup.isNotEmpty()) {
            for (model in openAdGroup.values) {
                model.openAd = null
                model.isShowing = false
                model.isLoading = false
            }
            openAdGroup.clear()
        }

        // Clear rewarded ads
        if (rewardAdGroup.isNotEmpty()) {
            for (model in rewardAdGroup.values) {
                model.rewardAd = null
                model.isLoading = false
            }
            rewardAdGroup.clear()
        }

        // Clear ads config
        ctx.adsConfigModel.apply {
            isAdsEnabled = false
            adIdOpenSplash = null
            adIdOpenEditor = null
            adIdOpenResume = null
            adIdRewardInterFontList = null
            adIdRewardFont = null
            adIdRewardTextColor = null
            adIdInterAdsRemoveBack = null
            adIdInterEditorAddText = null
            adIdInterEditorDecorate = null
            adIdInterEditorEditImage = null
            adIdInterEditorSave = null
            adIdInterEditorBack = null
            adIdInterPhotoChange = null
            adIdInterPhotoSelect = null
            adIdInterFontListBack = null
            adIdInterFontListOpen = null
            adIdInterHomeItemPhotoSaved = null
            adIdInterHomeItemSelectPhoto = null
            adIdInterSavedPhotoBack = null
            adIdInterStickerListOpen = null
            adIdInterStickerListBack = null
            adIdInterResultFullscreenBack = null
            adIdInterPhotoViewerBack = null
            adIdInterTextAddBack = null
            adIdInterTextAddOk = null
            adIdInterResultBack = null
            adIdInterOkWallpaperClick = null
            adIdNativeHome = null
            adIdNativeResult = null
            adIdBannerResult = null
            adIdBannerPhotoList = null
            adIdBannerAddText = null
            adIdBannerSavedPhoto = null
            adIdBannerPhotoDetail = null
            adIdBannerSampleTextList = null
            adIdBannerFontList = null
            adIdBannerStickerList = null
            adIdBannerEmojiList = null
            adIdBannerLineList = null
            adIdBannerTypoList = null
            adIdBannerBorderList = null
            lastTimeInterAdShown = 0
            minTimeToShowNextInterAd = 0
        }

        ctx.logE("Release ads")
    }

    private fun destroyNativeAd(nativeAd: NativeAd?) {
        nativeAd?.destroy()
    }

    private fun getInterAdModelHomeItemSelectPhoto(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_HOME_ITEM_SELECT_PHOTO)
    }

    private fun getInterAdModelHomeItemSavedPhoto(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_HOME_ITEM_SAVED_PHOTO)
    }

    private fun getInterAdModelPhotoSelect(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_PHOTO_SELECT)
    }

    private fun getInterAdModelFontListOpen(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_FONT_LIST_OPEN)
    }

    private fun getInterAdModelFontListBack(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_FONT_LIST_BACK)
    }

    private fun getInterAdModelStickerListOpen(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_STICKER_LIST_OPEN)
    }

    private fun getInterAdModelStickerListBack(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_STICKER_LIST_BACK)
    }

    private fun getInterAdModelPhotoChange(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_PHOTO_CHANGE)
    }

    private fun getInterAdModelTextAddBack(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_TEXT_ADD_BACK)
    }

    private fun getInterAdModelTextAddOK(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_TEXT_ADD_OK)
    }

    private fun getInterAdModelResultFullscreenBack(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_RESULT_FULLSCREEN_BACK)
    }

    private fun getInterAdModelPhotoViewerBack(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_PHOTO_VIEWER_BACK)
    }

    private fun getInterAdModelAdsRemoveBack(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_ADS_REMOVE_BACK)
    }

    private fun getInterAdModelEditorAddText(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_EDITOR_ADD_TEXT)
    }

    private fun getInterAdModelEditorDecorate(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_EDITOR_DECORATE)
    }

    private fun getInterAdModelEditorEditImage(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_EDITOR_EDIT_IMAGE)
    }

    private fun getInterAdModelEditorSave(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_EDITOR_SAVE)
    }

    private fun getInterAdModelEditorBack(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_EDITOR_BACK)
    }

    private fun getInterAdModelSavedPhotoBack(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_SAVED_PHOTO_BACK)
    }

    private fun getInterAdModelResultBack(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_RESULT_BACK)
    }

    private fun getInterAdModelOkWallpaperClick(): InterAdModel? {
        return getInterAdModelByKey(INTER_AD_OK_WALLPAPER_CLICK)
    }

    private fun getOpenAdModelSplash(): OpenAdModel? {
        return getOpenAdModelByKey(OPEN_AD_SPLASH)
    }

    private fun getOpenAdModelEditor(): OpenAdModel? {
        return getOpenAdModelByKey(OPEN_AD_EDITOR)
    }

    private fun getOpenAdModelResume(): OpenAdModel? {
        return getOpenAdModelByKey(OPEN_AD_RESUME)
    }

    private fun getRewardedAdModelFont(): RewardAdModel? {
        return getRewardAdModelByKey(REWARD_AD_FONT)
    }

    private fun getRewardedAdModelTextColor(): RewardAdModel? {
        return getRewardAdModelByKey(REWARD_AD_TEXT_COLOR)
    }

    private fun getInterAdModelByKey(key: String): InterAdModel? {
        var interAdModel: InterAdModel? = null
        for (strKey in interstitialAdGroup.keys) {
            if (strKey.contains(key)) {
                interAdModel = interstitialAdGroup[strKey]
                break
            }
        }

        return interAdModel
    }

    private fun getOpenAdModelByKey(key: String): OpenAdModel? {
        for (strKey in openAdGroup.keys) {
            if (strKey.contains(key)) {
                return openAdGroup[strKey]
            }
        }

        return null
    }

    private fun getRewardAdModelByKey(key: String): RewardAdModel? {
        for (strKey in rewardAdGroup.keys) {
            if (strKey.contains(key)) {
                return rewardAdGroup[strKey]
            }
        }

        return null
    }

    private fun countAdClicked() {
        ctx.appSettingsModel.run {
            // Reset click number if current click time is out of ad session
            if (ctx.currentTimeInSecond - lastTimeAdClicked >= ctx.adsConfigModel.adClickSession) {
                adClickedNumber = 0
            }

            // Count ad clicked
            adClickedNumber += 1

            // Keep the first clicked time
            if (adClickedNumber == 1) {
                lastTimeAdClicked = ctx.currentTimeInSecond
            }

            // Save to sharedPref
            CommonUtil.saveAppSettingsModel(ctx, this)

            // Reach max click in a session so that all ads will be disabled in a day
            if (adClickedNumber == ctx.adsConfigModel.maxAdClickNumberInSession) {
                // Add a time to the last time clicked var to disable ad in period
                lastTimeAdClicked = ctx.currentTimeInSecond + ctx.adsConfigModel.adDisabledSecond
                adClickedNumber = 0

                // Save to sharedPref
                CommonUtil.saveAppSettingsModel(ctx, this)

                // Destroy ads because user has clicked so much
                destroyNativeAdHome()
                destroyNativeAdResult()
            }
        }

        // Assign this to true to avoid showing OpenAd after back to App from ads
        isJustClickedAd = true
    }
}