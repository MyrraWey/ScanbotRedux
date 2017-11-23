package com.muravyovdmitr.scanbot.redux.pictures_view

import com.develop.zuzik.redux.core.extension.asConsumer
import com.develop.zuzik.redux.core.model.ReduxPresenter
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * User: Dima Muravyov
 * Date: 22.11.2017
 */
class PicturesViewPresenter(private val model: PicturesView.Model) : ReduxPresenter<PicturesView.View>(), PicturesView.Presenter {

	override fun onStart(view: PicturesView.View) {
		intent(model
				.state
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe { state ->
					if (state.currentPicture != null && !state.pictures.isEmpty()) {
						view.showPictures.onNext(state.pictures)
						view.navigateToPicture.onNext(state.currentPicture)
						view.updateCounter.onNext(
								PicturesView.CounterBundle(state.currentPicture + 1, state.pictures.size))
						view.setContentVisibility.onNext(true)
					} else {
						view.setContentVisibility.onNext(false)
					}
				})
		intent(model
				.property { state -> state.processing }
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(view.displayProgress.asConsumer()))

		intent(view
				.onCurrentPictureChanged
				.subscribe(model.changeCurrentPicture.asConsumer()))

		model.loadPictures.onNext(Unit)
	}
}