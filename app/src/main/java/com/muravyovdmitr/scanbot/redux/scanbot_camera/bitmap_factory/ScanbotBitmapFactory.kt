package com.muravyovdmitr.scanbot.redux.scanbot_camera.bitmap_factory

import android.graphics.Bitmap

/**
 * User: Dima Muravyov
 * Date: 20.11.2017
 */
interface ScanbotBitmapFactory {

	fun create(bytes: ByteArray, orientation: Int): Bitmap
}