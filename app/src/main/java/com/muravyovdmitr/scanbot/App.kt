package com.muravyovdmitr.scanbot

import android.app.Application
import net.doo.snap.ScanbotSDKInitializer


/**
 * Created by Dima Muravyov on 18.11.2017.
 */
class App : Application() {

	companion object {
		@JvmStatic
		lateinit var INSTANCE: App
			private set
	}

	override fun onCreate() {
		super.onCreate()
		INSTANCE = this
		ScanbotSDKInitializer().initialize(this)
	}
}