package com.muravyovdmitr.scanbot.redux.scanbot_camera

import com.develop.zuzik.redux.core.model.ReduxModel
import com.develop.zuzik.redux.core.store.Action
import com.muravyovdmitr.scanbot.redux.scanbot_camera.bitmap_factory.ScanbotBitmapFactory
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * User: Dima Muravyov
 * Date: 16.11.2017
 */
class ScanbotCameraModel(defaultState: ScanbotCamera.State,
						 private val bitmapFactory: ScanbotBitmapFactory,
						 modelScheduler: Scheduler = Schedulers.computation()) :
		ReduxModel<ScanbotCamera.State>(defaultState, modelScheduler),
		ScanbotCamera.Model {

	override val toggleFlash: PublishSubject<Unit> = PublishSubject.create()
	override val toggleAutomaticCapture: PublishSubject<Unit> = PublishSubject.create()
	override val takePicture: PublishSubject<Unit> = PublishSubject.create()
	override val handlePicture: PublishSubject<ScanbotCamera.PictureBundle> = PublishSubject.create()

	init {
		addAction(toggleFlash.map { ScanbotCameraAction.ToggleFlash() })
		addAction(toggleAutomaticCapture.map { ScanbotCameraAction.ToggleAutomaticCapture() })
		addAction(takePicture.map { ScanbotCameraAction.TakePicture() })
		addAction(
				handlePicture
						.flatMap { pictureBundle ->
							Observable
									.just(pictureBundle)
									.map { (bytes, orientation) -> bitmapFactory.create(bytes, orientation) }
									.map { /*TODO save image*/ }
									.map<Action> { ScanbotCameraAction.PictureHandled() }
									.startWith(ScanbotCameraAction.HandlePicture())
						})

		addReducer(ScanbotCameraReducer())
	}
}