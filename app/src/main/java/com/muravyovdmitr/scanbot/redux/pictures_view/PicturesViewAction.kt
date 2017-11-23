package com.muravyovdmitr.scanbot.redux.pictures_view

import com.develop.zuzik.redux.core.store.Action
import com.muravyovdmitr.scanbot.redux.pictures_view.picture.Picture

/**
 * User: Dima Muravyov
 * Date: 22.11.2017
 */
sealed class PicturesViewAction : Action {
	class LoadingPictures : PicturesViewAction()
	class PicturesLoaded(val pictures: List<Picture>) : PicturesViewAction()
	class ApplyingFilter : PicturesViewAction()
	class FilterApplied(val pictures: List<Picture>) : PicturesViewAction()
	class RotatingPicture : PicturesViewAction()
	class PictureRotated(val pictures: List<Picture>) : PicturesViewAction()
	class DeletingPicture : PicturesViewAction()
	class PictureDeleted(val pictures: List<Picture>) : PicturesViewAction()
}