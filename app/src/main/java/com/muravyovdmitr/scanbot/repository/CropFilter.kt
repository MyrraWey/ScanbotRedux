package com.muravyovdmitr.scanbot.repository

import android.graphics.Bitmap
import net.doo.snap.lib.detector.ContourDetector

/**
 * Created by: Alex Kucherenko
 * Date: 23.11.2017.
 */
data class CropFilter(val contour: SelectedContour) : ScanbotFilter {
	override fun apply(bitmap: Bitmap) = ContourDetector().processImageF(bitmap, contour.polygon, ContourDetector.IMAGE_FILTER_NONE)
}