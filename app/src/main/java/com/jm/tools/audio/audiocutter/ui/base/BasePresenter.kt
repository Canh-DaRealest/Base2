package com.jm.tools.audio.audiocutter.ui.base

abstract class BasePresenter<T : BaseView>() {

    abstract fun attachView(view: T)

    abstract fun detachView()
}