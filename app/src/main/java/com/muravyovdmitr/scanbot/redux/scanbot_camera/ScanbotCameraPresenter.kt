package com.muravyovdmitr.scanbot.redux.scanbot_camera

import com.develop.zuzik.redux.core.extension.asConsumer
import com.develop.zuzik.redux.core.model.ReduxPresenter
import io.reactivex.android.schedulers.AndroidSchedulers

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
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(view.setAutomaticCaptureEnabled.asConsumer()))
		intent(model
				.property { state -> state.flashEnabled }
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(view.setFlashEnabled.asConsumer()))
		intent(model
				.property { state -> state.processing }
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(view.displayProgress.asConsumer()))
		intent(model
				.property { state -> state.navigateToScanbotGallery }
				.filter { navigateBack -> navigateBack }
				.map { Unit }
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(view.goToScanbotGallery.asConsumer()))

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