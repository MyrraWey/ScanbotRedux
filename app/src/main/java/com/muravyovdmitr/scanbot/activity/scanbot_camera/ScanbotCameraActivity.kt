package com.muravyovdmitr.scanbot.activity.scanbot_camera

import android.app.Activity
import android.os.Bundle
import com.jakewharton.rxbinding2.view.clicks
import com.muravyovdmitr.scanbot.R
import com.muravyovdmitr.scanbot.redux.scanbot_camera.ScanbotCamera
import com.muravyovdmitr.scanbot.redux.scanbot_camera.ScanbotCameraModel
import com.muravyovdmitr.scanbot.redux.scanbot_camera.ScanbotCameraPresenter
import com.muravyovdmitr.scanbot.redux.scanbot_camera.bitmap_factory.ScanbotBitmapFactoryImpl
import com.muravyovdmitr.scanbot.view.LockProgressDialog
import com.muravyovdmitr.scanbot.view.ScanbotCameraView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_scanbot_camera.*

class ScanbotCameraActivity : Activity() {
	private val model: ScanbotCamera.Model = ScanbotCameraModel(
			ScanbotCamera.State(false, true, false, false),
			ScanbotBitmapFactoryImpl())
	private val presenter: ScanbotCamera.Presenter = ScanbotCameraPresenter(model)
	private val compositeDisposable = CompositeDisposable()
	private val cameraView: ScanbotCameraView by lazy { scvCamera }
	private val progressDialog = LockProgressDialog()
	private val pictureReceived = PublishSubject.create<ScanbotCamera.PictureBundle>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_scanbot_camera)

		model.init()
	}

	override fun onStart() {
		super.onStart()

		cameraView.onPictureReceived = { pictureBytes, pictureOrientation ->
			pictureReceived.onNext(ScanbotCamera.PictureBundle(pictureBytes, pictureOrientation))
		}
		val view = createViewAndInitDisposable()
		presenter.onStart(view)
	}

	override fun onResume() {
		super.onResume()
		cameraView.onResume()
	}

	override fun onPause() {
		cameraView.onPause()
		super.onPause()
	}

	override fun onStop() {
		cameraView.onPictureReceived = null
		compositeDisposable.clear()
		presenter.onStop()
		super.onStop()
	}

	override fun onDestroy() {
		model.release()
		super.onDestroy()
	}

	private fun createViewAndInitDisposable(): ScanbotCamera.View {
		val view = object : ScanbotCamera.View {
			override val setFlashEnabled = PublishSubject.create<Boolean>()
			override val setAutomaticCaptureEnabled = PublishSubject.create<Boolean>()
			override val displayProgress = PublishSubject.create<Boolean>()
			override val takePicture = PublishSubject.create<Unit>()
			override val navigateBack = PublishSubject.create<Unit>()
			override val onToggleFlash = tvFlashStatus.clicks()
			override val onToggleAutomaticCapture = tvAutomaticCaptureStatus.clicks()
			override val onTakePicture = tvTakePhoto.clicks()
			override val onPictureReceived = pictureReceived
		}

		compositeDisposable.addAll(
				view
						.setFlashEnabled
						.subscribe { flashEnabled ->
							tvFlashStatus.text = "Flash: ${if (flashEnabled) "Enabled" else "Dissabled"}"
							cameraView.flashEnabled = flashEnabled
						},
				view
						.setAutomaticCaptureEnabled
						.subscribe { automaticCaptureStatus ->
							tvAutomaticCaptureStatus.text = "AutomaticCaptureStatus: ${if (automaticCaptureStatus) "Enabled" else "Dissabled"}"
							cameraView.autoSnappingEnabled = automaticCaptureStatus
						},
				view
						.displayProgress
						.subscribe { displayProgress ->
							if (displayProgress) {
								progressDialog.start(this, "Processing")
							} else {
								progressDialog.stop()
							}
						},
				view
						.takePicture
						.subscribe { cameraView.takePicture() },
				view
						.navigateBack
						.subscribe {
							finish()
						})

		return view;
	}
}
