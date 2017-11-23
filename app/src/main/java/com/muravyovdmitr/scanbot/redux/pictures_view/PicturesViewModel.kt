package com.muravyovdmitr.scanbot.redux.pictures_view

import com.develop.zuzik.redux.core.model.ReduxModel
import com.develop.zuzik.redux.core.store.Action
import com.muravyovdmitr.scanbot.redux.pictures_view.picture.Id
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

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
	override val applyFilter: PublishSubject<PicturesView.FilterAction> = PublishSubject.create<PicturesView.FilterAction>()
	override val rotatePicture: PublishSubject<Id> = PublishSubject.create<Id>()
	override val deletePicture: PublishSubject<Id> = PublishSubject.create<Id>()

	init {
		addAction(loadPictures
				.flatMap { picturesRepository.getPictures() }
				.map<Action> { pictures -> PicturesViewAction.PicturesLoaded(pictures) }
				.startWith(PicturesViewAction.LoadingPictures()))
		addAction(applyFilter
				.flatMap { filterAction ->
					state
							.take(1)
							.map { state -> state.pictures }
							.delay(500, TimeUnit.MILLISECONDS)
							.map<Action> { pictures -> PicturesViewAction.FilterApplied(pictures) }
							.startWith(PicturesViewAction.ApplyingFilter())
				})
		addAction(rotatePicture
				.flatMap { pictureId ->
					state
							.take(1)
							.map { state -> state.pictures }
							.delay(500, TimeUnit.MILLISECONDS)
							.map<Action> { pictures -> PicturesViewAction.PictureRotated(pictures) }
							.startWith(PicturesViewAction.RotatingPicture())
				})
		addAction(deletePicture
				.flatMap { pictureId ->
					state
							.take(1)
							.map { state ->
								state
										.pictures
										.filter { picture -> picture.id.id != pictureId.id }
							}
							.delay(500, TimeUnit.MILLISECONDS)
							.map<Action> { pictures -> PicturesViewAction.PictureDeleted(pictures) }
							.startWith(PicturesViewAction.DeletingPicture())
				})

		addReducer(PicturesViewReducer())
	}
}