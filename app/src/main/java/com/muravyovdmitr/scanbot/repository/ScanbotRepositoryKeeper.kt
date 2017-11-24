package com.muravyovdmitr.scanbot.repository

/**
 * Created by: Alex Kucherenko
 * Date: 23.11.2017.
 */
object ScanbotRepositoryKeeper {
	val bitmapRepository: BitmapRepository = BitmapFileRepository()
	val pictureRepository: PictureRepository = ScanbotPictureRepository()
}