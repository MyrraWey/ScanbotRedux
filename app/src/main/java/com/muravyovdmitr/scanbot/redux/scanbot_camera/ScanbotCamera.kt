package com.muravyovdmitr.scanbot.redux.scanbot_camera

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
					 val navigateToScanbotGallery: Boolean)

	data class PictureBundle(val pictureBytes: ByteArray, val pictureOrientation: Int)

	interface Model : Redux.Model<State> {
		val toggleFlash: Observer<Unit>
		val toggleAutomaticCapture: Observer<Unit>
		val takePicture: Observer<Unit>
		val handlePicture: Observer<PictureBundle>
	}

	interface View : Redux.View {
		val setFlashEnabled: Observer<Boolean>
		val setAutomaticCaptureEnabled: Observer<Boolean>
		val displayProgress: Observer<Boolean>
		val takePicture: Observer<Unit>
		val goToScanbotGallery: Observer<Unit>

		val onToggleFlash: Observable<Unit>
		val onToggleAutomaticCapture: Observable<Unit>
		val onTakePicture: Observable<Unit>
		val onPictureReceived: Observable<PictureBundle>
	}

	interface Presenter : Redux.Presenter<View>
}