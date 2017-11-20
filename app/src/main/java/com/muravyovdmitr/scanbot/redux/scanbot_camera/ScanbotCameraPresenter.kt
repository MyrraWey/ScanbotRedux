package com.muravyovdmitr.scanbot.redux.scanbot_camera

import com.develop.zuzik.redux.core.extension.asConsumer
import com.develop.zuzik.redux.core.model.ReduxPresenter

/**
 * User: Dima Muravyov
 * Date: 16.11.2017
 */
class ScanbotCameraPresenter(private val model: ScanbotCamera.Model) :
		ReduxPresenter<ScanbotCamera.View>(),
		ScanbotCamera.Presenter {

	override fun onStart(view: ScanbotCamera.View) {
		intent(model
				.property { state -> state.automaticCaptureEnabled }
				.subscribe(view.setAutomaticCaptureEnabled.asConsumer()))
		intent(model
				.property { state -> state.flashEnabled }
				.subscribe(view.setFlashEnabled.asConsumer()))
		intent(model
				.property { state -> state.processing }
				.subscribe(view.displayProgress.asConsumer()))
		intent(model
				.property { state -> state.navigateBack }
				.filter { navigateBack -> navigateBack }
				.map { Unit }
				.subscribe(view.navigateBack.asConsumer()))

		intent(view
				.onToggleAutomaticCapture
				.subscribe(model.toggleAutomaticCapture.asConsumer()))
		intent(view
				.onToggleFlash
				.subscribe(model.toggleFlash.asConsumer()))
		intent(view
				.onTakePicture
				.subscribe {
					model.takePicture.onNext(Unit)
					view.takePicture.onNext(Unit)
				})
		intent(view
				.onPictureReceived
				.subscribe(model.handlePicture.asConsumer()))
	}
}