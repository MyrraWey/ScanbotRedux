package com.muravyovdmitr.scanbot.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.muravyovdmitr.scanbot.App
import com.muravyovdmitr.scanbot.R

/**
 * Created by: Alex Kucherenko
 * Date: 24.11.2017.
 */
class ScanbotRepositoryFacade {
	private val bitmapRepository = ScanbotRepositoryKeeper.bitmapRepository
	private val pictureRepository = ScanbotRepositoryKeeper.pictureRepository

	fun create(bitmap: Bitmap): Int {
		val id = bitmapRepository.create(bitmap)
		return pictureRepository.create(id)
	}

	fun readOriginalBitmap(id: Int) = bitmapRepository.read(pictureRepository.read(id).originalImageId)
	fun readModifiedBitmap(id: Int) = bitmapRepository.read(pictureRepository.read(id).modifiedImageId)
	fun updateContour(id: Int, contour: SelectedContour?) {
		contour?.let {
			val picture = pictureRepository
					.read(id)
					.copy(cropFilter = CropFilter(it))
			pictureRepository.update(id, picture)
		}
	}

	fun applyFilters(id: Int) {
		TODO("Implement apply filters")
	}
}