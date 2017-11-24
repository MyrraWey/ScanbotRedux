package com.muravyovdmitr.scanbot.redux.pictures_view

import com.develop.zuzik.redux.core.model.Redux
import com.muravyovdmitr.scanbot.redux.pictures_view.filter.FilterType
import com.muravyovdmitr.scanbot.redux.pictures_view.picture.Id
import com.muravyovdmitr.scanbot.redux.pictures_view.picture.Picture
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * User: Dima Muravyov
 * Date: 21.11.2017
 */
interface PicturesView {

	class FilterAction(val id: Id, val filterType: FilterType)

	data class State(val pictures: List<Picture>, val processing: Boolean)

	interface Model : Redux.Model<State> {
		val loadPictures: Observer<Unit>
		val applyFilter: Observer<FilterAction>
		val rotatePicture: Observer<Id>
		val deletePicture: Observer<Id>
	}

	interface View : Redux.View {
		val showPictures: Observer<List<Picture>>
		val displayProgress: Observer<Boolean>

		val onApplyFilter: Observable<FilterAction>
		val onRotatePicture: Observable<Id>
		val onDeletePicture: Observable<Id>
	}

	interface Presenter : Redux.Presenter<View>
}