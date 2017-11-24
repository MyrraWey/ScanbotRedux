package com.muravyovdmitr.scanbot.redux.scanbot_gallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.muravyovdmitr.scanbot.App
import com.muravyovdmitr.scanbot.R
import com.muravyovdmitr.scanbot.repository.BitmapRepository

/**
 * User: Dima Muravyov
 * Date: 24.11.2017
 */
class StubScanbotBitmapRepository : BitmapRepository {
	private val bitmaps: Map<Int, Int> = mapOf(
			Pair(0, R.drawable.image_1_c),
			Pair(1, R.drawable.image_2_c),
			Pair(2, R.drawable.image_3_c))

	override fun create(bitmap: Bitmap): Int {
		TODO("not implemented")
	}

	override fun read(id: Int): Bitmap {
		return if (bitmaps.containsKey(id)) {
			loadBitmap(id)
		} else {
			throw Exception("There is no image with such ID")
		}
	}

	private fun loadBitmap(drawableResourceId: Int): Bitmap {
		return BitmapFactory.decodeResource(App.INSTANCE.resources, bitmaps[drawableResourceId]!!)
	}
}