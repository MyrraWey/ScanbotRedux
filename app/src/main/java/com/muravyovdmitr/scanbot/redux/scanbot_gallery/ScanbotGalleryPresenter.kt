package com.muravyovdmitr.scanbot.redux.scanbot_gallery

import com.develop.zuzik.redux.core.extension.asConsumer
import com.develop.zuzik.redux.core.model.ReduxPresenter
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * User: Dima Muravyov
 * Date: 22.11.2017
 */
class ScanbotGalleryPresenter(private val model: ScanbotGallery.Model) :
		ReduxPresenter<ScanbotGallery.View>(),
		ScanbotGallery.Presenter {

	override fun onStart(view: ScanbotGallery.View) {
		intent(model
				.property { state -> state.pictures }
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(view.showPictures.asConsumer()))
		intent(model
				.property { state -> state.processing }
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(view.displayProgress.asConsumer()))

		intent(view
				.onApplyFilter
				.subscribe(model.applyFilter.asConsumer()))
		intent(view
				.onRotatePicture
				.subscribe(model.rotatePicture.asConsumer()))
		intent(view
				.onDeletePicture
				.subscribe(model.deletePicture.asConsumer()))

		model.loadPictures.onNext(Unit)
	}
}