package com.muravyovdmitr.scanbot.camera_activity

import android.graphics.Bitmap
import com.develop.zuzik.redux.core.model.Redux
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * User: Dima Muravyov
 * Date: 16.11.2017
 */
interface ScanbotCamera {

	data class State(val flashEnabled: Boolean,
					 val automaticCaptureEnabled: Boolean,
					 val processing: Boolean,
					 val navigateBack: Boolean)

	interface Model : Redux.Model<State> {
		val toggleFlash: Observer<Unit>
		val toggleAutomaticCapture: Observer<Unit>
		val pictureProcessing: Observer<Boolean>
		val picturePrepared: Observer<Bitmap>
	}

	interface View : Redux.View {
		val setFlashEnabled: Observer<Boolean>
		val setAutomaticCaptureEnabled: Observer<Boolean>
		val displayProgress: Observer<Boolean>
		val takePicture: Observer<Unit>
		val navigateBack: Observer<Unit>

		val onToggleFlash: Observable<Unit>
		val onToggleAutomaticCapture: Observable<Unit>
		val onTakePicture: Observable<Unit>
		val onPictureStateChanged: Observable<ScanbotCameraView.PictureState>
	}

	interface Presenter : Redux.Presenter<View>
}