package com.muravyovdmitr.scanbot.redux.scanbot_gallery

import android.util.Log
import com.develop.zuzik.redux.core.store.Action
import com.develop.zuzik.redux.core.store.Reducer

/**
 * User: Dima Muravyov
 * Date: 22.11.2017
 */
class ScanbotGalleryReducer : Reducer<ScanbotGallery.State> {

	override fun reduce(oldState: ScanbotGallery.State, action: Action): ScanbotGallery.State =
			(action as? ScanbotGalleryAction)?.let {
				reduce(oldState, it).apply {
					Log.d("asdasd", "ScanbotGalleryReducer action - $action resultState - $this")
				}
			} ?: oldState


	private fun reduce(oldState: ScanbotGallery.State, action: ScanbotGalleryAction): ScanbotGallery.State =
			when (action) {
				is ScanbotGalleryAction.LoadingPictures -> oldState.copy(processing = true)
				is ScanbotGalleryAction.PicturesLoaded -> oldState.copy(processing = false, pictures = action.pictures)
				is ScanbotGalleryAction.ApplyingFilter -> oldState.copy(processing = true)
				is ScanbotGalleryAction.FilterApplied -> oldState.copy(processing = false, pictures = action.pictures)
				is ScanbotGalleryAction.RotatingPicture -> oldState.copy(processing = true)
				is ScanbotGalleryAction.PictureRotated -> oldState.copy(processing = false, pictures = action.pictures)
				is ScanbotGalleryAction.DeletingPicture -> oldState.copy(processing = true)
				is ScanbotGalleryAction.PictureDeleted -> oldState.copy(processing = false, pictures = action.pictures)
			}
}