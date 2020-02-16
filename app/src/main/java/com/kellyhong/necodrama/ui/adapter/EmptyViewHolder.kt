package com.kellyhong.necodrama.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kellyhong.necodrama.R

class EmptyViewHolder(itemView: View, msg: Int): RecyclerView.ViewHolder(itemView) {
    companion object {
        @JvmStatic
        fun newInstance(parent: ViewGroup, msg: Int) =
            EmptyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_empty_result, parent,false), msg)
    }

    init {
        (itemView as TextView).setText(msg)
    }
}