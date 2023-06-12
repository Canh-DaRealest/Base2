package com.jm.tools.audio.audiocutter.ui.audiocutter.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.jm.tools.audio.audiocutter.R
import com.jm.tools.audio.audiocutter.data.model.AudioModel

open class AudioCutterAdapter(
    private val ctx: AppCompatActivity,
    private val arrPhotos: List<AudioModel>,
    private val itemClickListener: (AudioModel) -> Unit
) : RecyclerView.Adapter<AudioCutterAdapter.ItemViewHolder>() {

    private val layoutInflater by lazy { LayoutInflater.from(ctx) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = layoutInflater.inflate(R.layout.item_audio_cutter, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrPhotos.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (itemCount > 0) {
            val model = arrPhotos[position]
            holder.bind(model)
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val lblTitle: TextView = itemView.findViewById(R.id.lbl_title)
        private val lblInfo: TextView = itemView.findViewById(R.id.lbl_info)
        private val imgPlay: ImageView = itemView.findViewById(R.id.img_play)

        fun bind(model: AudioModel) {
            with(model) {
                lblTitle.text = name

                itemView.setOnClickListener {
                    itemClickListener(model)
                }
            }
        }
    }
}