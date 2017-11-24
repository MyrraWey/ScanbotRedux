package com.muravyovdmitr.scanbot.activity.scanbot_gallery

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.muravyovdmitr.scanbot.repository.BitmapRepository
import com.muravyovdmitr.scanbot.repository.ScanbotPicture


/**
 * User: Dima Muravyov
 * Date: 21.11.2017
 */

class ScanbotGalleryPagerAdapter(private val bitmapRepository: BitmapRepository) : PagerAdapter() {
	var pictures: MutableList<ScanbotPicture> = mutableListOf()
		set(pictures) {
			field.clear()
			field.addAll(pictures)
			notifyDataSetChanged()
		}

	override fun isViewFromObject(view: View?, `object`: Any?): Boolean = view == `object`

	override fun getCount(): Int = pictures.size

	override fun getItemPosition(`object`: Any?): Int {
		/*TODO add correct logic for position validation*/
		return POSITION_NONE
	}

	override fun instantiateItem(container: ViewGroup, position: Int): Any {
		val imageView = ImageView(container.context)
		//TODO smells - maybe it't better to extract image loading logic
		imageView.setImageBitmap(bitmapRepository.read(pictures[position].modifiedImageId))
		container.addView(imageView)
		return imageView
	}

	override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
		container.removeView(`object` as View)
	}
}
