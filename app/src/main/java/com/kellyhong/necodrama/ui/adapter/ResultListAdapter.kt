package com.kellyhong.necodrama.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kellyhong.necodrama.R
import com.kellyhong.necodrama.arch.OnVHItemClicker
import com.kellyhong.necodrama.db.drama.Drama
import com.kellyhong.necodrama.util.CardPressedAnim
import com.kellyhong.necodrama.util.ImageLoader
import com.kellyhong.necodrama.util.setOnSlowClickListener

class ResultListAdapter(private var dataset: List<Drama>,
                        private val onClick:OnItemClicker): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val normal = 0
    private val empty = 1
    private val onItemClicker = object : OnVHItemClicker {
        override fun onClick(adapterPosition: Int) {
            onClick.onClick(getItem(adapterPosition))
        }
    }

    fun updateData(dataset: List<Drama>) {
        this.dataset = dataset
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == empty)
            EmptyViewHolder.newInstance(parent, R.string.search_empty)
        else
            TextViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_drama, parent,false), onItemClicker)
    }

    override fun getItemViewType(position: Int): Int {
        return if(dataset.isEmpty()) empty else normal
    }

    override fun getItemCount(): Int = if(dataset.isEmpty()) empty else dataset.size

    private fun getItem(position: Int): Drama? {
        return if(position >= 0 && position < dataset.size)
            dataset[position]
        else
            null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is TextViewHolder)
            holder.putData(getItem(position))
    }

    class TextViewHolder(itemView: View, private val onClick: OnVHItemClicker): RecyclerView.ViewHolder(itemView) {
        private val ivCoverPhoto = itemView.findViewById<ImageView>(R.id.iv_cover_photo)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private val tvSubTitle = itemView.findViewById<TextView>(R.id.tv_create)
        private val ratingBar = itemView.findViewById<RatingBar>(R.id.rating_bar)

        init {
            val e = itemView.context.resources.getDimension(R.dimen.elevation_card)
            CardPressedAnim.elevationPressed(e, itemView)
            itemView.setOnSlowClickListener(View.OnClickListener {
                onClick.onClick(adapterPosition)
            })
        }

        fun putData(info: Drama?) {
            if(info != null) {
                tvTitle.text = info.name
                tvSubTitle.text = info.getDisplayDate()
                ratingBar.rating = info.rating.toFloat()
                ImageLoader.updatePhoto(ivCoverPhoto, info.thumb)
            }
        }
    }

    interface OnItemClicker {
        fun onClick(result: Drama?)
    }
}