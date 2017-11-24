package com.muravyovdmitr.scanbot.redux.scanbot_gallery

import com.develop.zuzik.redux.core.model.Redux
import com.muravyovdmitr.scanbot.repository.ScanbotPicture
import com.muravyovdmitr.scanbot.repository.ColorFilterType
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * User: Dima Muravyov
 * Date: 21.11.2017
 */
interface ScanbotGallery {

	class FilterAction(val scanbotPictureId: Int, val colorFilterType: ColorFilterType)

	data class State(val pictures: List<ScanbotPicture>, val processing: Boolean)

	interface Model : Redux.Model<State> {
		val loadPictures: Observer<Unit>
		val applyFilter: Observer<FilterAction>
		val rotatePicture: Observer<Int>
		val deletePicture: Observer<Int>
	}

	interface View : Redux.View {
		val showPictures: Observer<List<ScanbotPicture>>
		val displayProgress: Observer<Boolean>

		val onApplyFilter: Observable<FilterAction>
		val onRotatePicture: Observable<Int>
		val onDeletePicture: Observable<Int>
	}

	interface Presenter : Redux.Presenter<View>
}