package com.muravyovdmitr.scanbot.redux.pictures_view

import com.develop.zuzik.redux.core.store.Action
import com.develop.zuzik.redux.core.store.Reducer

/**
 * User: Dima Muravyov
 * Date: 22.11.2017
 */
class PicturesViewReducer : Reducer<PicturesView.State> {

	override fun reduce(oldState: PicturesView.State, action: Action): PicturesView.State =
			(action as? PicturesViewAction)?.let {
				reduce(oldState, it)
			} ?: oldState


	private fun reduce(oldState: PicturesView.State, action: PicturesViewAction): PicturesView.State =
			when (action) {
				is PicturesViewAction.LoadingPictures -> oldState.copy(processing = true)
				is PicturesViewAction.PicturesLoaded -> oldState.copy(processing = false, pictures = action.pictures)
				is PicturesViewAction.ApplyingFilter -> oldState.copy(processing = true)
				is PicturesViewAction.FilterApplied -> oldState.copy(processing = false, pictures = action.pictures)
				is PicturesViewAction.RotatingPicture -> oldState.copy(processing = true)
				is PicturesViewAction.PictureRotated -> oldState.copy(processing = false, pictures = action.pictures)
				is PicturesViewAction.DeletingPicture -> oldState.copy(processing = true)
				is PicturesViewAction.PictureDeleted -> oldState.copy(processing = false, pictures = action.pictures)
			}
}