package com.muravyovdmitr.scanbot.repository

import android.graphics.Bitmap

/**
 * Created by: Alex Kucherenko
 * Date: 23.11.2017.
 */
class BitmapFileRepository : BitmapRepository {
	private var maxId = 0

	override fun create(bitmap: Bitmap): Int {
		maxId++
		saveBitmapInFileSystem(bitmap)
		return maxId
	}

	override fun read(id: Int): Bitmap {
		TODO("read picture from file system")
	}

	private fun saveBitmapInFileSystem(picture: Bitmap) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}