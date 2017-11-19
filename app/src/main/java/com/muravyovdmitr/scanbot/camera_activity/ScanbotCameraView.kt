package com.muravyovdmitr.scanbot.camera_activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.muravyovdmitr.scanbot.R
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.view_scanbot_camera.view.*
import net.doo.snap.camera.AutoSnappingController
import net.doo.snap.camera.ContourDetectorFrameHandler
import net.doo.snap.lib.detector.ContourDetector
import net.doo.snap.lib.detector.DetectionResult
import net.doo.snap.ui.PolygonView

/**
 * User: Dima Muravyov
 * Date: 17.11.2017
 */
class ScanbotCameraView : FrameLayout {

	sealed class PictureState {
		class Idle : PictureState()
		class PreparingPicture : PictureState()
		class PicturePrepared(val picture: Bitmap) : PictureState()
	}

	private val cameraView: net.doo.snap.camera.ScanbotCameraView by lazy { scvCameraa }
	private val polygonView: PolygonView by lazy { pvPolygon }

	private val contourDetectorFrameHandler: ContourDetectorFrameHandler by lazy { ContourDetectorFrameHandler.attach(cameraView) }
	private val autoSnappingController: AutoSnappingController by lazy { AutoSnappingController.attach(cameraView, contourDetectorFrameHandler) }
	private val userGuidanceToast: Toast by lazy {
		Toast.makeText(context, "", Toast.LENGTH_SHORT)
	}
	private val pictureStateSubject = BehaviorSubject.createDefault<PictureState>(PictureState.Idle())

	var autoSnappingEnabled: Boolean = false
		set(enabled) {
			field = enabled
			autoSnappingController.isEnabled = enabled
			contourDetectorFrameHandler.isEnabled = enabled
			polygonView.visibility = if (enabled) View.VISIBLE else View.GONE
			if (!enabled) {
				userGuidanceToast.cancel()
			}
		}
	var flashEnabled: Boolean = false
		set(enabled) {
			field = enabled
			cameraView.useFlash(enabled)
		}
	var pictureState: Observable<PictureState> = pictureStateSubject

	constructor(context: Context) : super(context)

	constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
		val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		layoutInflater.inflate(R.layout.view_scanbot_camera, this, true)

		initCameraView()
		initContourDetectorFrameHandler()
		userGuidanceToast.setGravity(Gravity.CENTER, 0, 0)
	}

	fun onResume() = cameraView.onResume()

	fun onPause() = cameraView.onPause()

	fun takePicture() = cameraView.takePicture(false)

	private fun initCameraView() {
		cameraView.setCameraOpenCallback {
			cameraView.postDelayed({
				cameraView.setAutoFocusSound(false)
				cameraView.setShutterSound(false)
				cameraView.continuousFocus()
			}, 700)
		}
		cameraView.addPictureCallback { image, imageOrientation ->
			Log.d("asdasd", "startPreparingImage")
			pictureStateSubject.onNext(PictureState.PreparingPicture())
			val options = BitmapFactory.Options()
			options.inSampleSize = 8
			var originalBitmap = BitmapFactory.decodeByteArray(image, 0, image.size, options)

			if (imageOrientation > 0) {
				val matrix = Matrix()
				matrix.setRotate(imageOrientation.toFloat(), originalBitmap.width / 2f, originalBitmap.height / 2f)
				originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, false)
			}

			val detector = ContourDetector()
			detector.detect(originalBitmap)
			val documentImage = detector.processImageAndRelease(originalBitmap, detector.polygonF, ContourDetector.IMAGE_FILTER_NONE)

			Log.d("asdasd", "ImagePrepared")
			pictureStateSubject.onNext(PictureState.PicturePrepared(documentImage))
			cameraView.continuousFocus()
			cameraView.startPreview()
		}
	}

	private fun initContourDetectorFrameHandler() {
		contourDetectorFrameHandler.setAcceptedAngleScore(60.0)
		contourDetectorFrameHandler.setAcceptedSizeScore(70.0)
		contourDetectorFrameHandler.addResultHandler(polygonView)
		contourDetectorFrameHandler.addResultHandler { detectedFrame ->
			polygonView.post { showUserGuidance(detectedFrame.detectionResult) }
			return@addResultHandler false
		}
	}

	private fun showUserGuidance(result: DetectionResult) {
		if (!autoSnappingEnabled) {
			return
		}

		when (result) {
			DetectionResult.OK -> {
				userGuidanceToast.setText("Don't move")
				userGuidanceToast.show()
			}
			DetectionResult.OK_BUT_TOO_SMALL -> {
				userGuidanceToast.setText("Move closer")
				userGuidanceToast.show()
			}
			DetectionResult.OK_BUT_BAD_ANGLES -> {
				userGuidanceToast.setText("Perspective")
				userGuidanceToast.show()
			}
			DetectionResult.ERROR_NOTHING_DETECTED -> {
				userGuidanceToast.setText("No Document")
				userGuidanceToast.show()
			}
			DetectionResult.ERROR_TOO_NOISY -> {
				userGuidanceToast.setText("Background too noisy")
				userGuidanceToast.show()
			}
			DetectionResult.ERROR_TOO_DARK -> {
				userGuidanceToast.setText("Poor light")
				userGuidanceToast.show()
			}
			else -> userGuidanceToast.cancel()
		}
	}
}