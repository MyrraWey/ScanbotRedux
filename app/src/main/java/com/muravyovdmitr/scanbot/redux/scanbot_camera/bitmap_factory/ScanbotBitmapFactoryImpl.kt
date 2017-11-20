package com.muravyovdmitr.scanbot.redux.scanbot_camera.bitmap_factory

import android.graphics.Bitmap
import android.graphics.Matrix
import net.doo.snap.lib.detector.ContourDetector

/**
 * User: Dima Muravyov
 * Date: 20.11.2017
 */
class ScanbotBitmapFactoryImpl : ScanbotBitmapFactory {

	override fun create(bytes: ByteArray, orientation: Int): Bitmap {
		val options = android.graphics.BitmapFactory.Options()
		options.inSampleSize = 8
		var originalBitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)

		if (orientation > 0) {
			val matrix = Matrix()
			matrix.setRotate(orientation.toFloat(), originalBitmap.width / 2f, originalBitmap.height / 2f)
			originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, false)
		}

		val detector = ContourDetector()
		detector.detect(originalBitmap)

		return detector.processImageAndRelease(originalBitmap, detector.polygonF, ContourDetector.IMAGE_FILTER_NONE)
	}
}