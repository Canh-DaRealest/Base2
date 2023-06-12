package com.jm.tools.audio.audiocutter.util

import android.os.Environment
import com.jm.tools.audio.audiocutter.BuildConfig

object Constant {

    val ROOT_FOLDER = Environment.getExternalStorageDirectory().absolutePath
    val SAVED_PHOTOS_FOLDER =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/ToP"

    const val LINK_APP =
        "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
    const val LINK_POLICY = "https://sites.google.com/"
    const val PROVIDER = "com.jm.tools.audio.audiocutter.fileprovider"

    object Key {
        const val PHOTO_MODEL = "photo_model"
        const val TAG = "tag"
        const val TEXT = "text"
        const val FONT_ID = "font_id"
        const val STICKER_ID = "sticker_id"
        const val EMOJI_ID = "emoji_id"
        const val LINE_ID = "line_id"
        const val TYPO_ID = "typo_id"
        const val BORDER_ID = "border_id"
        const val OUTPUT_PATH = "output_path"
        const val QUOTE = "quote"
        const val CROPPED_FILE_PATH = "cropped_file_path"
        const val FLAG_WATCHED_AD = "flag_watched_ad"
    }

    object Event {
        const val REMOVE_AD = "remove_ad"
    }
}