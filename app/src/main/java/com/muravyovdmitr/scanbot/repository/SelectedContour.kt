package com.muravyovdmitr.scanbot.repository

import android.graphics.PointF
import android.util.Pair
import net.doo.snap.lib.detector.Line2D

/**
 * Created by: Alex Kucherenko
 * Date: 23.11.2017.
 */
data class SelectedContour(val linesPair: Pair<List<Line2D>, List<Line2D>>?,
						   val polygon: List<PointF>?)