package com.muravyovdmitr.scanbot.repository

/**
 * Created by: Alex Kucherenko
 * Date: 23.11.2017.
 */
class ScanbotRepository : Repository {
	override fun create(picture: ScanbotPicture): Int {
		TODO("save contour in field and image to file system")
	}

	override fun read(id: Int): ScanbotPicture {
		TODO("read picture from file system")
	}
}