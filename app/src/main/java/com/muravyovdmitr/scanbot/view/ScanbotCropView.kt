package com.muravyovdmitr.scanbot.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.muravyovdmitr.scanbot.R
import kotlinx.android.synthetic.main.view_scanbot_crop.view.*
import net.doo.snap.lib.detector.ContourDetector
import net.doo.snap.lib.detector.Line2D
import net.doo.snap.ui.EditPolygonImageView
import net.doo.snap.ui.MagnifierView

/**
 * Created by: Alex Kucherenko
 * Date: 20.11.2017.
 */
class ScanbotCropView : RelativeLayout {

	private val editPolygonView: EditPolygonImageView by lazy { polygonView }
	private val magnifierView: MagnifierView by lazy { magnifier }
	private var originalBitmap: Bitmap? = null

	constructor(context: Context) : super(context)

	constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
		val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		layoutInflater.inflate(R.layout.view_scanbot_crop, this, true)

		editPolygonView.setImageResource(R.drawable.test_receipt)
		originalBitmap = (editPolygonView.getDrawable() as BitmapDrawable).bitmap
		magnifierView.setupMagnifier(editPolygonView)
	}

	fun getPolygon() = editPolygonView.polygon

	fun displayContour(linesPair: Pair<List<Line2D>, List<Line2D>>?,
					   polygon: List<PointF>?) {
		if (polygon != null && linesPair != null) {
			editPolygonView.setPolygon(polygon)
			editPolygonView.setLines(linesPair.first, linesPair.second)
		}
	}
}