package com.muravyovdmitr.scanbot.repository

import com.muravyovdmitr.scanbot.repository.ColorFilterType

/**
 * User: Dima Muravyov
 * Date: 22.11.2017
 */

class ColorFilterToNameMapper {

	fun map(colorFilterType: ColorFilterType): String =
			when (colorFilterType) {
				ColorFilterType.SEPIA -> "Sepia"
				ColorFilterType.BLACK_WHITE -> "Black_White"
				ColorFilterType.DARK -> "Dark"
				ColorFilterType.YELLOW -> "Yellow"
			}
}