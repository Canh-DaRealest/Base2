package com.jm.tools.audio.audiocutter.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.jm.tools.audio.audiocutter.util.CommonUtil

abstract class BaseFragment<V : BaseView, P : BasePresenterImp<V>, VB : ViewBinding> : Fragment(),
    BaseView {

    protected lateinit var parentActivity: AppCompatActivity
    protected val self: Fragment by lazy { this }
    protected lateinit var presenter: P

    private var _binding: VB? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = context as AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = initPresenter()
        presenter.attachView(initView())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWidgets(view)

        // Close keyboard when user touch outside
        CommonUtil.closeKeyboardWhileClickOutSide(parentActivity, view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
    * return viewBinding
    * */
    abstract fun getViewBinding(): VB

    /*
    * Set up widgets such as EditText, TextView, RecyclerView, etc
    * */
    abstract fun initWidgets(rootView: View)
}
