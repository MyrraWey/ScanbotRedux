package com.muravyovdmitr.scanbot.camera_activity

import com.develop.zuzik.redux.core.store.Action

/**
 * User: Dima Muravyov
 * Date: 16.11.2017
 */
sealed class ScanbotCameraAction : Action {
	class ToggleFlash : ScanbotCameraAction()
	class ToggleAutomaticCapture : ScanbotCameraAction()
	class PictureProcessingChanged(val inProcessing: Boolean) : ScanbotCameraAction()
	class SavingPicture : ScanbotCameraAction()
	class PictureSaved : ScanbotCameraAction()
}