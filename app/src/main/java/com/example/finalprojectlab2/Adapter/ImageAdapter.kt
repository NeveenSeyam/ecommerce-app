package com.example.finalprojectlab2.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.example.finalprojectlab2.R


class ImageAdapter internal constructor(context: Context) : PagerAdapter() {
    private val mContext: Context
    private val mImageIds = intArrayOf(
        R.drawable.model,
        R.drawable.model,
        R.drawable.model,
        R.drawable.model

    )

    override fun getCount(): Int {
        return mImageIds.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(mContext)
        imageView.setScaleType(ImageView.ScaleType.MATRIX)
        imageView.setImageResource(mImageIds[position])
        container.addView(imageView, 0)
        return imageView
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as ImageView)
    }

    init {
        mContext = context
    }
}