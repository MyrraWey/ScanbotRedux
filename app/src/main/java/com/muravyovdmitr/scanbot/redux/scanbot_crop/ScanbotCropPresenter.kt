package com.muravyovdmitr.scanbot.redux.scanbot_crop

import android.graphics.Bitmap
import com.develop.zuzik.redux.core.extension.asConsumer
import com.develop.zuzik.redux.core.model.ReduxPresenter
import com.muravyovdmitr.scanbot.repository.SelectedContour

/**
 * Created by: Alex Kucherenko
 * Date: 20.11.2017.
 */
class ScanbotCropPresenter(private val model: ScanbotCrop.Model) :
		ReduxPresenter<ScanbotCrop.View>(),
		ScanbotCrop.Presenter {

	override fun onStart(view: ScanbotCrop.View) {
		intent(model
				.property { state -> state.processing }
				.subscribe(view.displayProgress.asConsumer()))
		intent(model
				.state
				.filter { it.contour != null }
				.map<SelectedContour> { state -> state.contour }
				.subscribe(view.displayContour.asConsumer()))
		intent(model
				.state
				.filter { it.resource != null }
				.map<Bitmap> { state -> state.resource }
				.subscribe(view.setupResource.asConsumer()))
		intent(model
				.property { state -> state.type }
				.subscribe(view.configureControls.asConsumer()))

		intent(view
				.onAutoDetectContourClicked
				.subscribe(model.contourAutoDetect.asConsumer()))
		intent(view
				.onResetContourClicked
				.subscribe(model.resetContour.asConsumer()))
		intent(view
				.onSaveClicked
				.subscribe(model.save.asConsumer()))
	}
}