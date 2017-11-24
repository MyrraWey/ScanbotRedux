package com.muravyovdmitr.scanbot.redux.scanbot_crop

import android.graphics.Bitmap
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
	class Load(
			val id: Int,
			val resource: Bitmap,
			val contour: SelectedContour) : ScanbotCropAction()
}