package com.muravyovdmitr.scanbot.redux.pictures_view

import android.graphics.Bitmap
import com.develop.zuzik.redux.core.store.Action

/**
 * User: Dima Muravyov
 * Date: 22.11.2017
 */
sealed class PicturesViewAction : Action {
	class LoadingPictures : PicturesViewAction()
	class PicturesLoaded(val pictures: List<Bitmap>, val page: Int) : PicturesViewAction()
	class PageChanged(val page: Int) : PicturesViewAction()
}