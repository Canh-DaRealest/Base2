package com.jm.tools.audio.audiocutter.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

object RxViewEventObservable {

    fun onSearchingFrom(searchView: TextView, timeOut: Long = 300): Observable<String> {
        val subject = PublishSubject.create<String>()

        searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                subject.onNext(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        return subject.debounce(timeOut, TimeUnit.MILLISECONDS)
            .filter {
                return@filter true
            }
            .distinctUntilChanged()
            .switchMap {
                return@switchMap Observable.just(it)
            }
    }

    fun onSeekbarChangedFrom(seekBar: AppCompatSeekBar): Observable<Int> {
        val subject = PublishSubject.create<Int>()
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                subject.onNext(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        return subject.debounce(10, TimeUnit.MILLISECONDS)
            .filter {
                return@filter true
            }
            .distinctUntilChanged()
            .switchMap {
                return@switchMap Observable.just(it)
            }
    }
}