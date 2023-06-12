package com.jm.tools.audio.audiocutter.util

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

// Use object so we have a singleton instance
object RxBus {

    private val publisher = PublishSubject.create<Any?>()

    private fun publish(event: Any?) {
        event?.let {
            publisher.onNext(it)
        }
    }

    // Listen should return an Observable and not the publisher
    // Using ofType we filter only events that match that class type
    private fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)

//
//    fun publishAdClosedEvent(model: EventAdModel) {
//        publish(model)
//    }
//
//    fun listenAdClosed(): Observable<EventAdModel> {
//        return listen(EventAdModel::class.java)
//    }
}