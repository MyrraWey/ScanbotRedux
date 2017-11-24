package com.muravyovdmitr.scanbot.redux.pictures_view.filter

/**
 * User: Dima Muravyov
 * Date: 22.11.2017
 */

class FilterToNameMapper {

	fun map(filterType: FilterType): String =
			when (filterType) {
				FilterType.SEPIA -> "Sepia"
				FilterType.BLACK_WHITE -> "Black_White"
				FilterType.DARK -> "Dark"
				FilterType.YELLOW -> "Yellow"
			}
}