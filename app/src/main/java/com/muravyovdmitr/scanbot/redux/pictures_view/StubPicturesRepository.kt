package com.muravyovdmitr.scanbot.redux.pictures_view

import android.graphics.BitmapFactory
import com.muravyovdmitr.scanbot.App
import com.muravyovdmitr.scanbot.R
import com.muravyovdmitr.scanbot.redux.pictures_view.picture.Id
import com.muravyovdmitr.scanbot.redux.pictures_view.picture.Picture
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * User: Dima Muravyov
 * Date: 22.11.2017
 */
class StubPicturesRepository {

	fun getPictures(): Observable<List<Picture>> {
		return Observable
				.defer { Observable.just(listOf(R.drawable.image_1_c, R.drawable.image_2_c, R.drawable.image_3_c)) }
				.subscribeOn(Schedulers.computation())
				.map { imageResources ->
					val resources = App.INSTANCE.resources
					imageResources
							.map { imageResource -> BitmapFactory.decodeResource(resources, imageResource) }
							.mapIndexed { index, bitmap -> Picture(Id(index), bitmap) }
				}
				.delay(200, TimeUnit.MILLISECONDS)
	}
}