package com.kellyhong.necodrama.uikit

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class ItemDecoration(private val leftRight: Int, private val topBottom: Int, private val padding: Int) : RecyclerView.ItemDecoration() {

	override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val viewPosition = parent.getChildAdapterPosition(view)

        when {
            parent.layoutManager is StaggeredGridLayoutManager -> getItemOffsetsOfSG(outRect,
                    viewPosition,
                    parent.layoutManager as StaggeredGridLayoutManager)
            parent.layoutManager is LinearLayoutManager -> getItemOffsetsOfLL(outRect,
                    viewPosition,
                    parent.layoutManager as LinearLayoutManager)
        }
    }

    private fun getItemOffsetsOfSG(outRect: Rect, viewPosition: Int, layoutManager: StaggeredGridLayoutManager) {
        val spanCount = layoutManager.spanCount

        if ((viewPosition + 1) % spanCount == 0) {
            outRect.left = leftRight / 2
            outRect.right = leftRight / 2
        } else {
            outRect.left = leftRight / 2
            outRect.right = leftRight / 2
        }
        if(viewPosition == layoutManager.itemCount - 1)
            outRect.bottom = topBottom

        outRect.top = topBottom
    }

    private fun getItemOffsetsOfLL(outRect: Rect, viewPosition: Int, layoutManager: LinearLayoutManager) {

        if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
            if (viewPosition == layoutManager.itemCount - 1) {
                outRect.bottom = topBottom + padding
            }
            outRect.top = topBottom

        } else {
            if (viewPosition == layoutManager.itemCount - 1) {
                outRect.right = leftRight + padding
            }
            if (viewPosition == 0){
                outRect.left = leftRight + padding
            } else
                outRect.left = leftRight
        }
    }
}
