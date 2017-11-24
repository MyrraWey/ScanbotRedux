package com.muravyovdmitr.scanbot.repository

import com.muravyovdmitr.scanbot.repository.ColorFilterType

/**
 * User: Dima Muravyov
 * Date: 22.11.2017
 */

class ColorFilterProvider {

	fun getAvailableFilters(): List<ColorFilterType> =
			listOf(
					ColorFilterType.SEPIA,
					ColorFilterType.BLACK_WHITE,
					ColorFilterType.DARK,
					ColorFilterType.YELLOW)
}