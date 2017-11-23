package com.muravyovdmitr.scanbot.activity.pictures_view

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.muravyovdmitr.scanbot.redux.pictures_view.picture.Picture


/**
 * User: Dima Muravyov
 * Date: 21.11.2017
 */

class PicturesViewPagerAdapter : PagerAdapter() {
	var pictures: MutableList<Picture> = mutableListOf()
		set(pictures) {
			field.clear()
			field.addAll(pictures)
			notifyDataSetChanged()
		}

	override fun isViewFromObject(view: View?, `object`: Any?): Boolean = view == `object`

	override fun getCount(): Int = pictures.size

	override fun instantiateItem(container: ViewGroup, position: Int): Any {
		val imageView = ImageView(container.context)
		imageView.setImageBitmap(pictures[position].picture)
		container.addView(imageView)
		return imageView
	}

	override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
		container.removeView(`object` as View)
	}
}
