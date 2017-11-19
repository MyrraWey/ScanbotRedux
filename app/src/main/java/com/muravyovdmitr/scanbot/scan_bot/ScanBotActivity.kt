package com.muravyovdmitr.scanbot.scan_bot

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.muravyovdmitr.scanbot.R
import kotlinx.android.synthetic.main.activity_scan_bot.*
import net.doo.snap.camera.AutoSnappingController
import net.doo.snap.camera.ContourDetectorFrameHandler
import net.doo.snap.camera.PictureCallback
import net.doo.snap.camera.ScanbotCameraView
import net.doo.snap.lib.detector.ContourDetector
import net.doo.snap.lib.detector.DetectionResult


class ScanBotActivity : Activity(), PictureCallback, ContourDetectorFrameHandler.ResultHandler {

	private val cameraView: ScanbotCameraView by lazy { camera }
	private val resultView: ImageView by lazy { ivResult }
	private val contourDetectorFrameHandler: ContourDetectorFrameHandler by lazy { ContourDetectorFrameHandler.attach(cameraView) }
	private val autoSnappingController: AutoSnappingController by lazy { AutoSnappingController.attach(cameraView, contourDetectorFrameHandler) }
	private val userGuidanceToast: Toast by lazy { Toast.makeText(this, "", Toast.LENGTH_SHORT) }

	private var flashEnabled = false
	private var autoSnappingEnabled = true

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(R.layout.activity_scan_bot)

		cameraView.setCameraOpenCallback {
			cameraView.postDelayed({
				cameraView.setAutoFocusSound(false)
				cameraView.setShutterSound(false)
				cameraView.continuousFocus()
				cameraView.useFlash(flashEnabled)
			}, 700)
		}

		// Please note: https://github.com/doo/Scanbot-SDK-Examples/wiki/Detecting-and-drawing-contours#contour-detection-parameters
		contourDetectorFrameHandler.setAcceptedAngleScore(60.0)
		contourDetectorFrameHandler.setAcceptedSizeScore(70.0)
		contourDetectorFrameHandler.addResultHandler(polygonView)
		contourDetectorFrameHandler.addResultHandler(this)

		cameraView.addPictureCallback(this)

		userGuidanceToast.setGravity(Gravity.CENTER, 0, 0)

		this.snap.setOnClickListener { cameraView.takePicture(false) }

		this.flash.setOnClickListener {
			flashEnabled = !flashEnabled
			cameraView.useFlash(flashEnabled)
		}

		findViewById<View>(R.id.autoSnappingToggle).setOnClickListener {
			autoSnappingEnabled = !autoSnappingEnabled
			setAutoSnapEnabled(autoSnappingEnabled)
		}

		setAutoSnapEnabled(autoSnappingEnabled)
	}

	override fun onResume() {
		super.onResume()
		cameraView.onResume()
	}

	override fun onPause() {
		super.onPause()
		cameraView.onPause()
	}

	override fun handleResult(detectedFrame: ContourDetectorFrameHandler.DetectedFrame): Boolean {
		// Here you are continuously notified about contour detection results.
		// For example, you can show a user guidance text depending on the current detection status.
		this.runOnUiThread { showUserGuidance(detectedFrame.detectionResult) }

		return false // typically you need to return false
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

	override fun onPictureTaken(image: ByteArray, imageOrientation: Int) {
		Log.d("asdasd", "startTakingPicture")
		// Here we get the full image from the camera.
		// Implement a suitable async(!) detection and image handling here.
		// This is just a demo showing detected image as downscaled preview image.

		// Decode Bitmap from bytes of original image:
		val options = BitmapFactory.Options()
		options.inSampleSize = 8 // use 1 for original size (if you want no downscale)!
		// in this demo we downscale the image to 1/8 for the preview.
		var originalBitmap = BitmapFactory.decodeByteArray(image, 0, image.size, options)

		// rotate original image if required:
		if (imageOrientation > 0) {
			val matrix = Matrix()
			matrix.setRotate(imageOrientation.toFloat(), originalBitmap.width / 2f, originalBitmap.height / 2f)
			originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, false)
		}

		// Run document detection on original image:
		val detector = ContourDetector()
		detector.detect(originalBitmap)
		val documentImage = detector.processImageAndRelease(originalBitmap, detector.polygonF, ContourDetector.IMAGE_FILTER_NONE)
		Log.d("asdasd", "PictureReady")
		resultView.post {
			resultView.setImageBitmap(documentImage)
			cameraView.continuousFocus()
			cameraView.startPreview()
		}
	}

	private fun setAutoSnapEnabled(enabled: Boolean) {
		autoSnappingController.isEnabled = enabled
		contourDetectorFrameHandler.isEnabled = enabled
		polygonView.visibility = if (enabled) View.VISIBLE else View.GONE
	}
}
