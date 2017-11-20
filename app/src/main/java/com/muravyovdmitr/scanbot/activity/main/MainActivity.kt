package com.muravyovdmitr.scanbot.activity.main

import android.app.Activity
import android.os.Bundle
import com.muravyovdmitr.scanbot.R
import com.muravyovdmitr.scanbot.activity.scan_bot.ScanBotActivity
import com.muravyovdmitr.scanbot.activity.scanbot_camera.ScanbotCameraActivity
import com.muravyovdmitr.scanbot.startActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		bScanBotActivity.setOnClickListener { startActivity(ScanBotActivity::class) }
		bScanbotReduxActivity.setOnClickListener { startActivity(ScanbotCameraActivity::class) }
	}
}
