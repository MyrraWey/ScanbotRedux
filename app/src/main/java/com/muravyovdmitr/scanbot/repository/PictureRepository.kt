package com.muravyovdmitr.scanbot.repository

/**
 * Created by: Alex Kucherenko
 * Date: 24.11.2017.
 */
interface PictureRepository {
	fun create(bitmapRepoId: Int): Int
	fun read(id: Int): ScanbotPicture
	fun update(id: Int, picture: ScanbotPicture?)
	fun delete(id: Int)
}