package com.kellyhong.necodrama.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kellyhong.necodrama.R
import com.kellyhong.necodrama.db.drama.Drama
import com.kellyhong.necodrama.util.ImageLoader
import com.kellyhong.necodrama.util.setOnSlowClickListener
import kotlinx.android.synthetic.main.detail_fragment.*

class DramaDetailFragment: Fragment() {

    companion object {
        private const val CONTENT = "content"
        fun newInstance(drama: Drama) = DramaDetailFragment().apply {
            val bundle = Bundle()
            bundle.putParcelable(CONTENT, drama)
            this.arguments = bundle
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments?.containsKey(CONTENT) == true)
            initView(arguments!!.getParcelable(CONTENT))
    }

    private fun initView(drama: Drama?) {
        if(drama == null) return
        action_bar.findViewById<TextView>(R.id.tv_title).text = drama.name
        action_bar.findViewById<ImageView>(R.id.iv_back).apply {
            visibility = View.VISIBLE
            setOnSlowClickListener(View.OnClickListener {
                activity?.onBackPressed()
            })
        }
        tv_create.text = drama.getDisplayTime()
        tv_rating.text = drama.getRate().toPlainString()
        tv_views.text = drama.totalViews.toString()
        rating_bar.rating = drama.rating.toFloat()
        ImageLoader.updatePhoto(iv_cover_photo, drama.thumb)
        iv_see_more.setOnSlowClickListener(View.OnClickListener {
            launchUri("https://www.google.com/search?q=${drama.name}")
        })
    }

    private fun launchUri(uriString: String) {
        if (uriString.isNotEmpty()) {
            val uri = Uri.parse(uriString)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}
