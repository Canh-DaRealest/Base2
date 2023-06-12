package com.jm.tools.audio.audiocutter.ui.audiocutter.list

import androidx.recyclerview.widget.GridLayoutManager
import com.jm.tools.audio.audiocutter.data.model.AudioModel
import com.jm.tools.audio.audiocutter.databinding.ActivityCutterListBinding
import com.jm.tools.audio.audiocutter.ui.base.BaseActivity
import com.jm.tools.audio.audiocutter.util.ExternalStoragePermissionUtil
import com.jm.tools.audio.audiocutter.util.extension.ctx
import com.jm.tools.audio.audiocutter.util.extension.gone
import com.jm.tools.audio.audiocutter.util.extension.visible

class CutterListActivity :
    BaseActivity<CutterListView, CutterListPresenterImp, ActivityCutterListBinding>(),
    CutterListView {

    private val externalStoragePermissionUtil = ExternalStoragePermissionUtil(self)
    private val arrAllAudio by lazy { arrayListOf<AudioModel>() }
    private val arrFilteredPhotos by lazy { arrayListOf<AudioModel>() }
    private var photoAdapter: AudioCutterAdapter? = null

    override fun initView(): CutterListView {
        return this
    }

    override fun initPresenter(): CutterListPresenterImp {
        return CutterListPresenterImp(ctx)
    }

    override fun getViewBinding(): ActivityCutterListBinding {
        return ActivityCutterListBinding.inflate(layoutInflater)
    }

    override fun initWidgets() {
        // Init toolbar
        applyToolbar(binding.toolbar) {
            finish()
        }

        // Init recyclerView
        binding.rclAudio.apply {
            layoutManager = GridLayoutManager(ctx, 3, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            photoAdapter =
                AudioCutterAdapter(self, arrFilteredPhotos, itemClickListener = { model ->

                })
            adapter = photoAdapter
        }

        // Get audios
        getAudios()
    }

    override fun onAudiosLoaded(audios: List<AudioModel>) {
        // Add all audios
        arrAllAudio.addAll(0, audios)

        arrFilteredPhotos.addAll(arrAllAudio)
        if (arrFilteredPhotos.isNotEmpty()) {
            binding.rclAudio.visible()

            // Notify data changed
            photoAdapter?.notifyItemRangeInserted(0, audios.size)
            binding.rclAudio.smoothScrollToPosition(0)
        } else {
            binding.rclAudio.gone()
        }
    }

    private fun getAudios() {
        externalStoragePermissionUtil.requestPermission(object :
            ExternalStoragePermissionUtil.Listener {
            override fun onGranted() {
                presenter.getAllAudio()
            }

            override fun onDenied() {
            }
        })
    }
}
