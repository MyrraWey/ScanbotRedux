package com.muravyovdmitr.scanbot.redux.scanbot_crop

import com.develop.zuzik.redux.core.store.Action
import com.develop.zuzik.redux.core.store.Reducer

/**
 * Created by: Alex Kucherenko
 * Date: 20.11.2017.
 */
class ScanbotCropReducer : Reducer<ScanbotCrop.State> {

	override fun reduce(oldState: ScanbotCrop.State, action: Action): ScanbotCrop.State =
			(action as? ScanbotCropAction)?.let {
				reduce(oldState, it)
			} ?: oldState


	private fun reduce(oldState: ScanbotCrop.State, action: ScanbotCropAction): ScanbotCrop.State {
		return when (action) {
			is ScanbotCropAction.ContourAutoDetecting -> oldState.copy(processing = true, type = ScanbotCrop.Type.AUTODETECT)
			is ScanbotCropAction.ContourAutoDetected -> oldState.copy(processing = false, contour = action.contour)
			is ScanbotCropAction.ResetContour -> oldState.copy(processing = false, type = ScanbotCrop.Type.RESET, contour = action.contour)
			is ScanbotCropAction.Save -> oldState.copy(contour = oldState.contour?.copy(polygon = action.polygon))
			is ScanbotCropAction.Verify -> oldState.copy(contour = if (oldState.contour != null) oldState.contour else action.contour)
		}
	}

}