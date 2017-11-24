package com.muravyovdmitr.scanbot.redux.scanbot_gallery

import com.develop.zuzik.redux.core.store.Action
import com.muravyovdmitr.scanbot.repository.ScanbotPicture

/**
 * User: Dima Muravyov
 * Date: 22.11.2017
 */
sealed class ScanbotGalleryAction : Action {
	class LoadingPictures : ScanbotGalleryAction()
	class PicturesLoaded(val pictures: List<ScanbotPicture>) : ScanbotGalleryAction()
	class ApplyingFilter : ScanbotGalleryAction()
	class FilterApplied(val pictures: List<ScanbotPicture>) : ScanbotGalleryAction()
	class RotatingPicture : ScanbotGalleryAction()
	class PictureRotated(val pictures: List<ScanbotPicture>) : ScanbotGalleryAction()
	class DeletingPicture : ScanbotGalleryAction()
	class PictureDeleted(val pictures: List<ScanbotPicture>) : ScanbotGalleryAction()
}