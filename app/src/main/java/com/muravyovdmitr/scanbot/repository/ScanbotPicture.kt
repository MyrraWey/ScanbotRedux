package com.muravyovdmitr.scanbot.repository

/**
 * Created by: Alex Kucherenko
 * Date: 23.11.2017.
 */
data class ScanbotPicture(val id: Int,
						  val originalImageId: Int,
						  var modifiedImageId: Int,
						  val filters: MutableList<ScanbotFilter>) {
}