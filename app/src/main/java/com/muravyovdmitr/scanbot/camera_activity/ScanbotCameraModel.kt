package com.muravyovdmitr.scanbot.camera_activity

import android.graphics.Bitmap
import com.develop.zuzik.redux.core.model.ReduxModel
import com.develop.zuzik.redux.core.store.Action
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

/**
 * User: Dima Muravyov
 * Date: 16.11.2017
 */
class ScanbotCameraModel(defaultState: ScanbotCamera.State, modelScheduler: Scheduler = AndroidSchedulers.mainThread()) :
		ReduxModel<ScanbotCamera.State>(defaultState, modelScheduler),
		ScanbotCamera.Model {

	override val toggleFlash: PublishSubject<Unit> = PublishSubject.create()
	override val toggleAutomaticCapture: PublishSubject<Unit> = PublishSubject.create()
	override val pictureProcessing: PublishSubject<Boolean> = PublishSubject.create()
	override val picturePrepared: PublishSubject<Bitmap> = PublishSubject.create()

	init {
		addAction(toggleFlash.map { ScanbotCameraAction.ToggleFlash() })
		addAction(toggleAutomaticCapture.map { ScanbotCameraAction.ToggleAutomaticCapture() })
		addAction(pictureProcessing.map { processing -> ScanbotCameraAction.PictureProcessingChanged(processing) })
		addAction(
				picturePrepared
						.map<Action> { picture ->
							//TODO save picture here
							ScanbotCameraAction.PictureSaved()
						}
						.startWith(ScanbotCameraAction.SavingPicture()))

		addReducer(ScanbotCameraReducer())
	}
}