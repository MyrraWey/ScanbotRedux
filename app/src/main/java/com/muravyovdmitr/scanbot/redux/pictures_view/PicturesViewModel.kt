package com.muravyovdmitr.scanbot.redux.pictures_view

import com.develop.zuzik.redux.core.model.ReduxModel
import com.develop.zuzik.redux.core.store.Action
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * User: Dima Muravyov
 * Date: 22.11.2017
 */
class PicturesViewModel(defaultState: PicturesView.State,
						private val picturesRepository: StubPicturesRepository,
						modelScheduler: Scheduler = Schedulers.computation()) :
		ReduxModel<PicturesView.State>(defaultState, modelScheduler),
		PicturesView.Model {

	override val loadPictures: PublishSubject<Unit> = PublishSubject.create<Unit>()
	override val changeCurrentPicture: PublishSubject<Int> = PublishSubject.create<Int>()

	init {
		addAction(loadPictures
				.flatMap { picturesRepository.getPictures() }
				.map<Action> { pictures -> PicturesViewAction.PicturesLoaded(pictures, 0) }
				.startWith(PicturesViewAction.LoadingPictures()))
		addAction(changeCurrentPicture.map { page -> PicturesViewAction.PageChanged(page) })

		addReducer(PicturesViewReducer())
	}
}