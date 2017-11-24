package com.muravyovdmitr.scanbot.repository

import android.graphics.Bitmap

/**
 * Created by: Alex Kucherenko
 * Date: 23.11.2017.
 */
interface BitmapRepository {
	fun create(bitmap: Bitmap): Int

	fun read(id: Int): Bitmap
}