package com.muravyovdmitr.scanbot.redux.scanbot_crop

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.util.Pair
import com.develop.zuzik.redux.core.model.ReduxModel
import com.develop.zuzik.redux.core.store.Action
import com.muravyovdmitr.scanbot.App
import com.muravyovdmitr.scanbot.repository.SelectedContour
import io.reactivex.Observable
import io.reactivex.Observable.just
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import net.doo.snap.lib.detector.ContourDetector
import net.doo.snap.lib.detector.DetectionResult
import net.doo.snap.lib.detector.Line2D
import net.doo.snap.ui.EditPolygonImageView
import java.util.*

/**
 * Created by: Alex Kucherenko
 * Date: 20.11.2017.
 */
class ScanbotCropModel(defaultState: ScanbotCrop.State,
					   modelScheduler: Scheduler = AndroidSchedulers.mainThread()) :
		ReduxModel<ScanbotCrop.State>(defaultState, modelScheduler),
		ScanbotCrop.Model {

	override val contourAutoDetect: PublishSubject<Unit> = PublishSubject.create()
	override val resetContour: PublishSubject<Unit> = PublishSubject.create()
	override val save: PublishSubject<List<PointF>> = PublishSubject.create()
	override val verifyState: PublishSubject<Unit> = PublishSubject.create()

	init {
		addAction(contourAutoDetect
				.flatMap<Action> {
					state
							.take(1)
							.map { s -> s.resource }
							.observeOn(Schedulers.computation())
							.flatMap { resource -> transformToBitmap(resource) }
							.flatMap { bitmap -> autoDetectContourObservable(bitmap) }
							.map<Action> { contour -> ScanbotCropAction.ContourAutoDetected(contour) }
							.startWith(ScanbotCropAction.ContourAutoDetecting())
				}
		)
		addAction(resetContour.map { ScanbotCropAction.ResetContour(getDefaultContour()) })
		addAction(save
				//.flatMap { //create and apply crop filter, save to repository }
				.map { polygon -> ScanbotCropAction.Save(polygon) })
		addAction(verifyState.map { ScanbotCropAction.Verify(getDefaultContour()) })
		addReducer(ScanbotCropReducer())
	}

	private fun getDefaultContour(): SelectedContour {
		val detector = ContourDetector()
		return SelectedContour(
				Pair(detector.horizontalLines, detector.verticalLines),
				ArrayList(EditPolygonImageView.DEFAULT_POLYGON))
	}

	private fun transformToBitmap(resource: Int): Observable<Bitmap> {
		return Observable.defer { just(BitmapFactory.decodeResource(App.INSTANCE.getResources(), resource)) }
	}

	fun autoDetectContourObservable(image: Bitmap): io.reactivex.Observable<SelectedContour> {
		return Observable.defer { just(autoDetectContour(image)) }
	}

	fun autoDetectContour(image: Bitmap): SelectedContour {
		val detector = ContourDetector()
		val detectionResult = detector.detect(image)
		var linesPair: Pair<List<Line2D>, List<Line2D>>? = null
		var polygon: List<PointF> = ArrayList(EditPolygonImageView.DEFAULT_POLYGON)
		when (detectionResult) {
			DetectionResult.OK,
			DetectionResult.OK_BUT_BAD_ANGLES,
			DetectionResult.OK_BUT_TOO_SMALL,
			DetectionResult.OK_BUT_BAD_ASPECT_RATIO -> {
				linesPair = Pair(detector.horizontalLines, detector.verticalLines)
				polygon = detector.polygonF
			}
		}
		return SelectedContour(linesPair, polygon)
	}
}