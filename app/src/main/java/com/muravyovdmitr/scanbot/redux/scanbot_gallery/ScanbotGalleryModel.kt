package com.muravyovdmitr.scanbot.redux.scanbot_gallery

import com.develop.zuzik.redux.core.model.ReduxModel
import com.develop.zuzik.redux.core.store.Action
import com.muravyovdmitr.scanbot.repository.*
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * User: Dima Muravyov
 * Date: 22.11.2017
 */
class ScanbotGalleryModel(defaultState: ScanbotGallery.State,
						  modelScheduler: Scheduler = Schedulers.computation()) :
		ReduxModel<ScanbotGallery.State>(defaultState, modelScheduler),
		ScanbotGallery.Model {

	override val loadPictures: PublishSubject<Unit> = PublishSubject.create<Unit>()
	override val applyFilter: PublishSubject<ScanbotGallery.FilterAction> = PublishSubject.create<ScanbotGallery.FilterAction>()
	override val rotatePicture: PublishSubject<Int> = PublishSubject.create<Int>()
	override val deletePicture: PublishSubject<Int> = PublishSubject.create<Int>()

	init {
		addAction(loadPictures
				.flatMap {
					/*TODO picturesRepository.getPictures()*/
					Observable
							.just(listOf(
									ScanbotPicture(17, 0, 0, CropFilter(SelectedContour(null, null)), ColorFilter(ColorFilterType.BLACK_WHITE)),
									ScanbotPicture(18, 1, 1, CropFilter(SelectedContour(null, null)), ColorFilter(ColorFilterType.BLACK_WHITE)),
									ScanbotPicture(19, 2, 2, CropFilter(SelectedContour(null, null)), ColorFilter(ColorFilterType.BLACK_WHITE))))
				}
				.map<Action> { pictures -> ScanbotGalleryAction.PicturesLoaded(pictures) }
				.startWith(ScanbotGalleryAction.LoadingPictures()))
		addAction(applyFilter
				.flatMap { filterAction ->
					state
							.take(1)
							.map { state -> state.pictures }
							.delay(500, TimeUnit.MILLISECONDS)
							.map<Action> { pictures -> ScanbotGalleryAction.FilterApplied(pictures) }
							.startWith(ScanbotGalleryAction.ApplyingFilter())
				})
		addAction(rotatePicture
				.flatMap { pictureId ->
					state
							.take(1)
							.map { state -> state.pictures }
							.delay(500, TimeUnit.MILLISECONDS)
							.map<Action> { pictures -> ScanbotGalleryAction.PictureRotated(pictures) }
							.startWith(ScanbotGalleryAction.RotatingPicture())
				})
		addAction(deletePicture
				.flatMap { scanbotPictureId ->
					state
							.take(1)
							.map { state ->
								state
										.pictures
										.filter { picture -> picture.id != scanbotPictureId }
							}
							.delay(500, TimeUnit.MILLISECONDS)
							.map<Action> { pictures -> ScanbotGalleryAction.PictureDeleted(pictures) }
							.startWith(ScanbotGalleryAction.DeletingPicture())
				})

		addReducer(ScanbotGalleryReducer())
	}
}