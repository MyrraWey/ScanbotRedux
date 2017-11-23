package com.muravyovdmitr.scanbot.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.muravyovdmitr.scanbot.R
import kotlinx.android.synthetic.main.view_scanbot_camera.view.*
import net.doo.snap.camera.AutoSnappingController
import net.doo.snap.camera.ContourDetectorFrameHandler
import net.doo.snap.lib.detector.DetectionResult
import net.doo.snap.ui.PolygonView

@SuppressLint("ShowToast")
/**
 * User: Dima Muravyov
 * Date: 17.11.2017
 */
class ScanbotCameraView : FrameLayout {
	private val cameraView: net.doo.snap.camera.ScanbotCameraView by lazy { scvCameraa }
	private val polygonView: PolygonView by lazy { pvPolygon }

	private val contourDetectorFrameHandler: ContourDetectorFrameHandler by lazy { ContourDetectorFrameHandler.attach(cameraView) }
	private val autoSnappingController: AutoSnappingController by lazy { AutoSnappingController.attach(cameraView, contourDetectorFrameHandler) }
	private val userGuidanceToast: Toast by lazy { Toast.makeText(context, "", Toast.LENGTH_SHORT) }

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

	var onPictureReceived: ((ByteArray, Int) -> Unit)? = null

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
			cameraView.post {
				cameraView.setAutoFocusSound(false)
				cameraView.setShutterSound(false)
				cameraView.continuousFocus()
			}
		}
		cameraView.addPictureCallback { imageBytes, imageOrientation ->
			userGuidanceToast.cancel()
			onPictureReceived?.invoke(imageBytes, imageOrientation)

			cameraView.continuousFocus()
			cameraView.startPreview()
		}
	}

	private fun initContourDetectorFrameHandler() {
		contourDetectorFrameHandler.setAcceptedAngleScore(60.0)
		contourDetectorFrameHandler.setAcceptedSizeScore(70.0)
		contourDetectorFrameHandler.addResultHandler(polygonView)
		contourDetectorFrameHandler.addResultHandler { detectedFrame ->
			cameraView.post { showUserGuidance(detectedFrame.detectionResult) }
			return@addResultHandler false
		}
	}

	private fun showUserGuidance(result: DetectionResult) {
		if (autoSnappingEnabled) {
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
}