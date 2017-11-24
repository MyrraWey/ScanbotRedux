package com.muravyovdmitr.scanbot.repository

import android.graphics.Bitmap

/**
 * Created by: Alex Kucherenko
 * Date: 23.11.2017.
 */
interface ScanbotFilter {
	fun apply(bitmap: Bitmap): Bitmap
}