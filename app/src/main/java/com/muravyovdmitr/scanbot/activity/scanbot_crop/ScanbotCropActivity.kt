package com.muravyovdmitr.scanbot.activity.scanbot_crop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PointF
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import com.muravyovdmitr.scanbot.R
import com.muravyovdmitr.scanbot.redux.scanbot_crop.ScanbotCrop
import com.muravyovdmitr.scanbot.redux.scanbot_crop.ScanbotCropModel
import com.muravyovdmitr.scanbot.redux.scanbot_crop.ScanbotCropPresenter
import com.muravyovdmitr.scanbot.repository.SelectedContour
import com.muravyovdmitr.scanbot.view.LockProgressDialog
import com.muravyovdmitr.scanbot.view.ScanbotCropView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_scanbot_crop.*

/**
 * Created by: Alex Kucherenko
 * Date: 20.11.2017.
 */
class ScanbotCropActivity : Activity() {
	private val model: ScanbotCrop.Model = ScanbotCropModel(
			ScanbotCrop.State(
					false,
					null,
					ScanbotCrop.Type.RESET,
					null))
	private val presenter: ScanbotCrop.Presenter = ScanbotCropPresenter(model)
	private val compositeDisposable = CompositeDisposable()
	private val scanbotCropView: ScanbotCropView by lazy { cropView }
	private val progressDialog = LockProgressDialog()
	private val saveClicks = PublishSubject.create<List<PointF>>()

	companion object {
		private val IMAGE_ID = "IMAGE_ID"
		fun createIntent(context: Context, imageId: Int) = Intent(context, ScanbotCropActivity::class.java)
				.putExtra(IMAGE_ID, imageId)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_scanbot_crop)

		model.init()
		model.load.onNext(intent.getIntExtra(IMAGE_ID, 0))
		btnSave.setOnClickListener({ saveClicks.onNext(scanbotCropView.getPolygon()) })
	}

	override fun onStart() {
		super.onStart()
		presenter.onStart(createViewAndInitDisposable())
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

	private fun createViewAndInitDisposable(): ScanbotCrop.View {
		val view = object : ScanbotCrop.View {
			override val displayProgress = PublishSubject.create<Boolean>()
			override val displayContour = PublishSubject.create<SelectedContour>()
			override val setupResource = PublishSubject.create<Bitmap>()
			override val configureControls = PublishSubject.create<ScanbotCrop.Type>()
			override val onAutoDetectContourClicked = btnAutoDetectContour.clicks()
			override val onSaveClicked = saveClicks
			override val onResetContourClicked = btnResetContour.clicks()
		}

		compositeDisposable.addAll(
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
						.setupResource
						.subscribe { resource ->
							scanbotCropView.setResource(resource)
						},
				view
						.displayContour
						.subscribe { contour ->
							scanbotCropView.displayContour(
									contour.linesPair,
									contour.polygon)
						},
				view
						.configureControls
						.subscribe {
							when (it) {
								ScanbotCrop.Type.RESET -> {
									btnAutoDetectContour.visibility = View.VISIBLE
									btnResetContour.visibility = View.GONE
								}
								ScanbotCrop.Type.AUTODETECT -> {
									btnAutoDetectContour.visibility = View.GONE
									btnResetContour.visibility = View.VISIBLE
								}
							}
						})
		return view;
	}
}