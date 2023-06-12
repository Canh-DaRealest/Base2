package com.jm.tools.audio.audiocutter.ui.audiocutter.list

import android.content.Context
import com.jm.tools.audio.audiocutter.data.interactor.AudioInteractor
import com.jm.tools.audio.audiocutter.ui.base.BasePresenterImp
import com.jm.tools.audio.audiocutter.util.extension.addToCompositeDisposable
import com.jm.tools.audio.audiocutter.util.extension.applyIOWithAndroidMainThread
import io.reactivex.Single

class CutterListPresenterImp(ctx: Context) : BasePresenterImp<CutterListView>(ctx) {

    private val audioInteractor by lazy { AudioInteractor(ctx) }

    fun getAllAudio() {
        view?.also { v ->
            Single.fromCallable {
                audioInteractor.loadAudios()
            }.applyIOWithAndroidMainThread()
                .subscribe(
                    {
                        v.onAudiosLoaded(it)
                    },
                    {
                        it.printStackTrace()
                    }
                ).addToCompositeDisposable(compositeDisposable)
        }
    }
}