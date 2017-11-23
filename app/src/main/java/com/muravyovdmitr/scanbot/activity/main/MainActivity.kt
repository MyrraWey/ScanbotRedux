package com.muravyovdmitr.scanbot.activity.main

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.muravyovdmitr.scanbot.R
import com.muravyovdmitr.scanbot.activity.pictures_view.PicturesViewActivity
import com.muravyovdmitr.scanbot.activity.scan_bot.ScanBotActivity
import com.muravyovdmitr.scanbot.activity.scanbot_camera.ScanbotCameraActivity
import com.muravyovdmitr.scanbot.activity.scanbot_crop.ScanbotCropActivity
import com.muravyovdmitr.scanbot.startActivity
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

class MainActivity : Activity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		bScanBotActivity.setOnClickListener { startActivity(ScanBotActivity::class) }
		bScanbotReduxActivity.setOnClickListener { startActivity(ScanbotCameraActivity::class) }
		bScanbotCropActivity.setOnClickListener { startActivity(ScanbotCropActivity::class) }
		bPicturesViewActivity.setOnClickListener { startActivity(PicturesViewActivity::class) }
	}
}
