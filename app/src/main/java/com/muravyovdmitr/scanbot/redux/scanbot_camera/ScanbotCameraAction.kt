package com.muravyovdmitr.scanbot.redux.scanbot_camera

import com.develop.zuzik.redux.core.store.Action

/**
 * User: Dima Muravyov
 * Date: 16.11.2017
 */
sealed class ScanbotCameraAction : Action {
	class ToggleFlash : ScanbotCameraAction()
	class ToggleAutomaticCapture : ScanbotCameraAction()
	class TakePicture : ScanbotCameraAction()
	class HandlePicture : ScanbotCameraAction()
	class PictureHandled : ScanbotCameraAction()
}