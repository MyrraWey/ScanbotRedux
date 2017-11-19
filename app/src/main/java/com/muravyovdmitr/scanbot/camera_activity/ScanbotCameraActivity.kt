package com.muravyovdmitr.scanbot.camera_activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.jakewharton.rxbinding2.view.clicks
import com.muravyovdmitr.scanbot.LockProgressDialog
import com.muravyovdmitr.scanbot.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_scanbot_camera.*

class ScanbotCameraActivity : Activity() {
	private val model: ScanbotCamera.Model = ScanbotCameraModel(ScanbotCamera.State(false, true, false, false))
	private val presenter: ScanbotCamera.Presenter = ScanbotCameraPresenter(model)
	private val compositeDisposable = CompositeDisposable()
	private val cameraView: ScanbotCameraView by lazy { scvCamera }
	private val progressDialog = LockProgressDialog()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_scanbot_camera)

		model.init()

		compositeDisposable.addAll(
				model
						.state
						.subscribe(
								{ state -> Log.d("asdasd", "ScanbotCameraModel::state onNext - $state") },
								{ error -> Log.d("asdasd", "ScanbotCameraModel::state onError - $error") }),
				model
						.error
						.subscribe(
								{ error -> Log.d("asdasd", "ScanbotCameraModel::error onNext - $error") },
								{ error -> Log.d("asdasd", "ScanbotCameraModel::error onError - $error") }),
				cameraView
						.pictureState
						.subscribe({ Log.d("asdasd", "ScanbotCameraView::state - $it") }))
	}

	override fun onStart() {
		super.onStart()

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
			override val onPictureStateChanged = cameraView.pictureState
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
