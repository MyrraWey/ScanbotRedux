package com.muravyovdmitr.scanbot.redux.scanbot_crop

import android.graphics.PointF
import com.develop.zuzik.redux.core.model.Redux
import com.muravyovdmitr.scanbot.repository.SelectedContour
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * Created by: Alex Kucherenko
 * Date: 20.11.2017.
 */
interface ScanbotCrop {
	data class State(val processing: Boolean,
					 val resource: Int,
					 val type: Type,
					 val contour: SelectedContour?)

	enum class Type {
		RESET,
		AUTODETECT,
		USER_MANUAL
	}

	interface Model : Redux.Model<State> {
		val contourAutoDetect: Observer<Unit>
		val resetContour: Observer<Unit>
		val save: Observer<List<PointF>>
		val verifyState: Observer<Unit>
	}

	interface View : Redux.View {
		val displayProgress: Observer<Boolean>
		val displayContour: Observer<SelectedContour>
		val configureControls: Observer<Type>

		val onAutoDetectContourClicked: Observable<Unit>
		val onResetContourClicked: Observable<Unit>
		val onSaveClicked: Observable<List<PointF>>
	}

	interface Presenter : Redux.Presenter<View>
}