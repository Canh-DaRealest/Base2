package com.jm.tools.audio.audiocutter.ui.base

import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.AppBarLayout
import com.jm.tools.audio.audiocutter.R
import com.jm.tools.audio.audiocutter.util.CommonUtil
import com.jm.tools.audio.audiocutter.util.PermissionUtil
import com.jm.tools.audio.audiocutter.util.extension.contentView
import com.jm.tools.audio.audiocutter.util.extension.textColor

abstract class BaseActivity<V : BaseView, P : BasePresenterImp<V>, VB : ViewBinding> :
    AppCompatActivity(), BaseView {

    protected val self by lazy { this }
    protected lateinit var presenter: P

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Init presenter & view
        presenter = initPresenter()
        presenter.attachView(initView())

        // Init viewBinding
        binding = getViewBinding()
        setContentView(binding.root)

        // Close keyboard when user touches outside
        CommonUtil.closeKeyboardWhileClickOutSide(self, contentView)

        /* Base methods */
        initWidgets()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    /*
    * return view
    * */
    abstract fun initView(): V

    /*
    * Return presenter
    * */
    abstract fun initPresenter(): P

    /*
    * Return activity's viewBinding
    * */
    abstract fun getViewBinding(): VB

    /*
    * Set up widgets such as EditText, TextView, RecyclerView, etc
    * */
    abstract fun initWidgets()

    protected fun translucentStatusBar() {
//        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    protected fun hideNavigationBar() {
        if (PermissionUtil.isApi30orHigher()) {
            window.decorView.windowInsetsController?.apply {
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                hide(WindowInsets.Type.navigationBars())
            }
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }

    protected fun showNavigationBar() {
        if (PermissionUtil.isApi30orHigher()) {
            window.decorView.windowInsetsController?.show(WindowInsets.Type.navigationBars())
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    protected fun hideSystemBar() {
        if (PermissionUtil.isApi30orHigher()) {
            window.decorView.windowInsetsController?.apply {
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                hide(WindowInsets.Type.systemBars())
            }
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }

    protected fun showSystemBar() {
        if (PermissionUtil.isApi30orHigher()) {
            window.decorView.windowInsetsController?.show(WindowInsets.Type.systemBars())
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
    }

    protected fun applyToolbar(
        toolbar: Toolbar,
        title: Any? = null,
        titleColor: Int = R.color.textColorPrimary,
        background: Int? = null,
        height: Int? = null,
        backIcon: Int = R.drawable.ic_back_white,
        onBackListener: (() -> Unit)? = null
    ) {
        toolbar.run {
            // Init toolbar
            setSupportActionBar(this)
            supportActionBar?.setDisplayShowTitleEnabled(false)

            // Custom Back icon
            onBackListener?.run {
                setNavigationIcon(backIcon)
                setNavigationOnClickListener { onBackListener() }
            }

            // Custom title
            title?.run {
                val strTitle = when (title) {
                    is String -> title
                    is CharSequence -> title.toString()
                    is Int -> getString(title)
                    else -> title.toString()
                }
                toolbar.title = strTitle
                for (childView in toolbar.children) {
                    if (childView is TextView) {
                        childView.textColor = titleColor
                    }
                }
            }

            // Custom background
            background?.also { bg ->
                setBackgroundResource(bg)
            }

            // Custom height
            height?.also { h ->
                val params = toolbar.layoutParams
                params.height = h
                layoutParams = params
            }
        }
    }

    protected fun removeElevation(appBarLayout: AppBarLayout) {
        appBarLayout.outlineProvider = null
    }
}
