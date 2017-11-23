package com.muravyovdmitr.scanbot.redux.pictures_view

import com.develop.zuzik.redux.core.extension.asConsumer
import com.develop.zuzik.redux.core.model.ReduxPresenter
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * User: Dima Muravyov
 * Date: 22.11.2017
 */
class PicturesViewPresenter(private val model: PicturesView.Model) :
		ReduxPresenter<PicturesView.View>(),
		PicturesView.Presenter {

	override fun onStart(view: PicturesView.View) {
		intent(model
				.versionProperty { state -> state.pictures }
				.filter { pictures -> pictures.isNotEmpty() }
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(view.showPictures.asConsumer()))
		intent(model
				.state
				.filter { state -> state.pictures.data.isNotEmpty() }
				.filter { state -> state.currentPicture != null }
				.map<Int> { state -> state.currentPicture }
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(view.navigateToPicture.asConsumer()))
		intent(model
				.state
				.filter { state -> state.pictures.data.isNotEmpty() }
				.filter { state -> state.currentPicture != null }
				.map<PicturesView.CounterBundle> { state ->
					PicturesView.CounterBundle(state.currentPicture!! + 1, state.pictures.data.size)
				}
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(view.updateCounter.asConsumer()))
		intent(model
				.state
				.map<Boolean> { state -> state.currentPicture != null && state.pictures.data.isNotEmpty() }
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(view.setContentVisibility.asConsumer()))
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