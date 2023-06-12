package com.jm.tools.audio.audiocutter.data.model

import com.google.gson.annotations.SerializedName

class AppSettingsModel : BaseModel() {
    @SerializedName("did_remove_ads")
    var didRemoveAds = false

    @SerializedName("did_check_vip")
    var didCheckVipStatus = false

    @SerializedName("default_lang")
    var defaultLanguage: String? = null

    @SerializedName("count_app_opened")
    var countAppOpened: Int = 0

    @SerializedName("count_saved_photo_success")
    var countSavedPhotoSuccess: Int = 0

    @SerializedName("count_click_rate_button")
    var countClickRateButton: Int = 0

    @SerializedName("count_click_not_now_button")
    var countClickNotNowButton: Int = 0

    @SerializedName("dont_show_rate_dialog_again")
    var dontShowRateDialogAgain = false

    @SerializedName("last_time_ad_clicked")
    var lastTimeAdClicked: Int = 0 // Second

    @SerializedName("ad_clicked_number")
    var adClickedNumber: Int = 0

    @SerializedName("inter_ad_session")
    var interAdSession: Int = 0 // Second

    @SerializedName("inter_ad_session_started_at")
    var interAdSessionStartedAt: Int = 0 // Second

    @SerializedName("max_inter_ad_shown_in_session")
    var maxInterAdShownInSession: Int = 4

    @SerializedName("number_of_inter_ad_shown_in_session")
    var numberOfInterAdShownInSession: Int = 0

    @SerializedName("is_tracking_saved_text")
    var isTrackingSavedText = false

    @SerializedName("is_showing_intro_home_icon")
    var isShowingIntroHomeIcon = false

    @SerializedName("is_showing_crop_feature_child_photo")
    var isShowingCropFeatureChildPhoto = true

    @SerializedName("count_view_result_fullscreen")
    var countViewResultFullscreen: Int = 0

    @SerializedName("count_click_ok_wallpaper")
    var countClickOkWallpaper: Int = 0
}