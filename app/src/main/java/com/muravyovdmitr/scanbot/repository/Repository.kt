package com.muravyovdmitr.scanbot.repository

/**
 * Created by: Alex Kucherenko
 * Date: 23.11.2017.
 */
interface Repository {
	fun create(picture: ScanbotPicture): Int

	fun read(id: Int): ScanbotPicture
}