package com.muravyovdmitr.scanbot.redux.pictures_view

import android.graphics.Bitmap
import com.develop.zuzik.redux.core.model.Redux
import com.develop.zuzik.redux.core.model.Version
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * User: Dima Muravyov
 * Date: 21.11.2017
 */
interface PicturesView {

	class CounterBundle(val currentPage: Int, val totalPages: Int)

	data class State(val pictures: Version<List<Bitmap>>, val currentPicture: Int?, val processing: Boolean)

	interface Model : Redux.Model<State> {
		val loadPictures: Observer<Unit>
		val changeCurrentPicture: Observer<Int>
	}

	interface View : Redux.View {
		val showPictures: Observer<List<Bitmap>>
		val navigateToPicture: Observer<Int>
		val setContentVisibility: Observer<Boolean>
		val displayProgress: Observer<Boolean>
		val updateCounter: Observer<CounterBundle>

		val onCurrentPictureChanged: Observable<Int>

		/*fun onAddPicture(): Observable<Unit>
		fun onCropPicture(): Observable<Unit>
		val onApplyFilter: Observable<FilterType>
		fun onRotatePicture(): Observable<Unit>
		fun onDeletePicture(): Observable<Unit>*/
	}

	interface Presenter : Redux.Presenter<View>
}