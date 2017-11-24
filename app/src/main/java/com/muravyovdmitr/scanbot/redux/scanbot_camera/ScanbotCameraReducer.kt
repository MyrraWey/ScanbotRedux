package com.muravyovdmitr.scanbot.redux.scanbot_camera

import android.util.Log
import com.develop.zuzik.redux.core.store.Action
import com.develop.zuzik.redux.core.store.Reducer

/**
 * User: Dima Muravyov
 * Date: 16.11.2017
 */
class ScanbotCameraReducer : Reducer<ScanbotCamera.State> {

	override fun reduce(oldState: ScanbotCamera.State, action: Action): ScanbotCamera.State =
			(action as? ScanbotCameraAction)?.let {
				reduce(oldState, it).apply {
					Log.d("asdasd", "ScanbotCameraReducer action - $action resultState - $this")
				}
			} ?: oldState


	private fun reduce(oldState: ScanbotCamera.State, action: ScanbotCameraAction): ScanbotCamera.State =
			when (action) {
				is ScanbotCameraAction.ToggleFlash -> oldState.copy(flashEnabled = !oldState.flashEnabled)
				is ScanbotCameraAction.ToggleAutomaticCapture ->
					oldState.copy(automaticCaptureEnabled = !oldState.automaticCaptureEnabled)
				is ScanbotCameraAction.TakePicture -> oldState.copy(processing = true)
				is ScanbotCameraAction.HandlePicture -> oldState.copy(processing = true)
				is ScanbotCameraAction.PictureHandled -> oldState.copy(processing = false, navigateToScanbotGallery = true)
			}
}