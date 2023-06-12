package com.jm.tools.audio.audiocutter.ui.audiocutter.list

import com.jm.tools.audio.audiocutter.data.model.AudioModel
import com.jm.tools.audio.audiocutter.ui.base.BaseView

interface CutterListView : BaseView {
    fun onAudiosLoaded(audios: List<AudioModel>)
}