package com.jm.tools.audio.audiocutter.util.extension

import android.animation.ObjectAnimator
import android.content.Context
import android.view.MenuItem
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import com.jm.tools.audio.audiocutter.R
import com.jm.tools.audio.audiocutter.util.SafeOnClickListener

inline val View.ctx: Context
    get() = context

var TextView.textColor: Int
    get() = currentTextColor
    set(value) = setTextColor(ContextCompat.getColor(ctx, value))

fun View.gone() {
    visibility = GONE
}

fun View.visible() {
    visibility = VISIBLE
}

fun View.invisible() {
    visibility = INVISIBLE
}

fun View.setOnSafeClickListener(safeTime: Long = 300, clickListener: (View?) -> Unit) {
    setOnClickListener(SafeOnClickListener.newInstance(safeTime) {
        clickListener(it)
    })
}

fun ViewGroup.setAnimation(visibility: Int, animation: Int) {
    val anim = AnimationUtils.loadAnimation(ctx, animation)
    val animController = LayoutAnimationController(anim)

    this.visibility = visibility
    layoutAnimation = animController
    startAnimation(anim)
}

fun View.calRatio(width: Int, heightRatio: Float) {
    layoutParams.width = width
    layoutParams.height = (width * heightRatio).toInt()
}

fun View.translateByX(byX: Float, translationTime: Long = 200) {
    ObjectAnimator.ofFloat(this, "translationX", byX).apply {
        duration = translationTime
        start()
    }
}

fun View.translateByY(byY: Float) {
    ObjectAnimator.ofFloat(this, "translationY", byY).apply {
        duration = 200
        start()
    }
}

fun View.showPopupMenu(menuRes: Int, listener: (MenuItem) -> Unit) {
    val themeWrapper = ContextThemeWrapper(ctx, R.style.PopupMenu)
    PopupMenu(themeWrapper, this).apply {
        menuInflater.inflate(menuRes, menu)

        setOnMenuItemClickListener { item ->
            listener(item)
            return@setOnMenuItemClickListener true
        }

        show()
    }
}