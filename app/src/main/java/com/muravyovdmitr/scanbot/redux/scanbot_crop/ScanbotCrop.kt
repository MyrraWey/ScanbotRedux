package com.muravyovdmitr.scanbot.redux.scanbot_crop

import android.graphics.Bitmap
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
	data class State(val id: Int,
					 val processing: Boolean,
					 val resource: Bitmap?,
					 val type: Type,
					 val contour: SelectedContour?)

	enum class Type {
		RESET,
		AUTODETECT
	}

	interface Model : Redux.Model<State> {
		val contourAutoDetect: Observer<Unit>
		val resetContour: Observer<Unit>
		val save: Observer<List<PointF>>
		val load: Observer<Int>
	}

	interface View : Redux.View {
		val displayProgress: Observer<Boolean>
		val setupResource: Observer<Bitmap>
		val displayContour: Observer<SelectedContour>
		val configureControls: Observer<Type>

		val onAutoDetectContourClicked: Observable<Unit>
		val onResetContourClicked: Observable<Unit>
		val onSaveClicked: Observable<List<PointF>>
	}

	interface Presenter : Redux.Presenter<View>
}