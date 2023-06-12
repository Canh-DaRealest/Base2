package com.jm.tools.audio.audiocutter.ui.base

import android.content.Context
import com.jm.tools.audio.audiocutter.util.extension.addToCompositeDisposable
import com.jm.tools.audio.audiocutter.widget.MyProgressDialog
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

open class BasePresenterImp<T : BaseView>(private val ctx: Context) : BasePresenter<T>() {

    private val progressDialog: MyProgressDialog by lazy { MyProgressDialog(ctx) }

    protected var view: T? = null
    protected val compositeDisposable by lazy { CompositeDisposable() }

    override fun attachView(view: T) {
        this.view = view
    }

    override fun detachView() {
        view?.onDestroyAds()
        compositeDisposable.clear()
        view = null
    }

    protected fun showProgressDialog(cancelable: Boolean = false, msg: String? = null) {
        try {
            if (!progressDialog.isShowing) {
                progressDialog.setCancelable(cancelable)
                progressDialog.show()
                msg?.run {
                    progressDialog.setMessage(this)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun dismissProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    protected fun delayBeforeDoSomething(
        delayTime: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, onSuccess: () -> Unit
    ) {
        view?.also {
            Single.timer(
                delayTime,
                timeUnit,
                AndroidSchedulers.mainThread()
            ).subscribe(object : SingleObserver<Any> {
                override fun onSubscribe(d: Disposable) {
                    d.addToCompositeDisposable(compositeDisposable)
                }

                override fun onSuccess(t: Any) {
                    onSuccess()
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
        }
    }
}