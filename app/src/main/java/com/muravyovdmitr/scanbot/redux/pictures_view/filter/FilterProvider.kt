package com.muravyovdmitr.scanbot.redux.pictures_view.filter

/**
 * User: Dima Muravyov
 * Date: 22.11.2017
 */

class FilterProvider {

	fun getAvailableFilters(): List<FilterType> =
			listOf(
					FilterType.SEPIA,
					FilterType.BLACK_WHITE,
					FilterType.DARK,
					FilterType.YELLOW)
}