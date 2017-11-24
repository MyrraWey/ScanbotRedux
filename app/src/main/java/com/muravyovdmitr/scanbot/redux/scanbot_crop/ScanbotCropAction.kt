package com.muravyovdmitr.scanbot.redux.scanbot_crop

import android.graphics.PointF
import com.develop.zuzik.redux.core.store.Action
import com.muravyovdmitr.scanbot.repository.SelectedContour

/**
 * Created by: Alex Kucherenko
 * Date: 20.11.2017.
 */
sealed class ScanbotCropAction : Action {
	class ContourAutoDetecting : ScanbotCropAction()
	class ContourAutoDetected(val contour: SelectedContour) : ScanbotCropAction()
	class ResetContour(val contour: SelectedContour) : ScanbotCropAction()
	class Save(val polygon: List<PointF>) : ScanbotCropAction()
	class Verify(val contour: SelectedContour) : ScanbotCropAction()
}