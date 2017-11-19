package com.muravyovdmitr.scanbot.camera_activity

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
				.onPictureStateChanged
				.subscribe { pictureState ->
					when (pictureState) {
						is ScanbotCameraView.PictureState.PreparingPicture -> {
							model.pictureProcessing.onNext(true)
						}
						is ScanbotCameraView.PictureState.Idle -> {
							model.pictureProcessing.onNext(false)
						}
						is ScanbotCameraView.PictureState.PicturePrepared -> {
							model.pictureProcessing.onNext(false)
							model.picturePrepared.onNext(pictureState.picture)
						}
					}

				})
		intent(view
				.onTakePicture
				.subscribe(view.takePicture.asConsumer()))
	}
}