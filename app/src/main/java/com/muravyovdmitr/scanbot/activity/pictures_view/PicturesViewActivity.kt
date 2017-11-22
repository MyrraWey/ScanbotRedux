package com.muravyovdmitr.scanbot.activity.pictures_view

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import com.muravyovdmitr.scanbot.R
import com.muravyovdmitr.scanbot.redux.pictures_view.StubPicturesRepository
import com.muravyovdmitr.scanbot.redux.pictures_view.PicturesView
import com.muravyovdmitr.scanbot.redux.pictures_view.PicturesViewModel
import com.muravyovdmitr.scanbot.redux.pictures_view.PicturesViewPresenter
import com.muravyovdmitr.scanbot.view.LockProgressDialog
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_pictures_view.*

class PicturesViewActivity : Activity() {
	private val model: PicturesView.Model = PicturesViewModel(PicturesView.State(listOf(), null, false), StubPicturesRepository())
	private val presenter: PicturesView.Presenter = PicturesViewPresenter(model)
	private val compositeDisposable = CompositeDisposable()
	private val currentPictureChanged = PublishSubject.create<Int>()
	private val picturesViewPagerAdapter = PicturesViewPagerAdapter()
	private val progressDialog = LockProgressDialog()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_pictures_view)

		model.init()
		configureViewPager()

		compositeDisposable.addAll(
				model
						.state
						.subscribe(
								{ state -> Log.d("asdasd", "PicturesViewActivity::state onNext - $state") },
								{ error -> Log.d("asdasd", "PicturesViewActivity::state onError - $error") }),
				model
						.error
						.subscribe(
								{ error -> Log.d("asdasd", "PicturesViewActivity::error onNext - $error") },
								{ error -> Log.d("asdasd", "PicturesViewActivity::error onError - $error") }))
	}

	override fun onStart() {
		super.onStart()
		presenter.onStart(createViewAndInitDisposables())
	}

	override fun onStop() {
		presenter.onStop()
		super.onStop()
	}

	override fun onDestroy() {
		model.release()
		super.onDestroy()
	}

	private fun configureViewPager() {
		vpPager.adapter = picturesViewPagerAdapter
		vpPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

			override fun onPageSelected(position: Int) = currentPictureChanged.onNext(position)
		})
	}

	private fun createViewAndInitDisposables(): PicturesView.View {
		val view = object : PicturesView.View {
			override val showPictures = PublishSubject.create<List<Bitmap>>()
			override val navigateToPicture = PublishSubject.create<Int>()
			override val setContentVisibility = PublishSubject.create<Boolean>()
			override val displayProgress = PublishSubject.create<Boolean>()
			override val updateCounter = PublishSubject.create<PicturesView.CounterBundle>()
			override val onCurrentPictureChanged: Observable<Int> = currentPictureChanged
		}

		compositeDisposable.addAll(
				view
						.showPictures
						.subscribe { pictures ->
							picturesViewPagerAdapter.pictures = pictures.toMutableList()
						},
				view
						.navigateToPicture
						.subscribe { itemIndex ->
							if (vpPager.currentItem != itemIndex) {
								vpPager.currentItem = itemIndex
							}
						},
				view
						.setContentVisibility
						.subscribe { visible ->
							vpPager.visibility = if (visible) View.VISIBLE else View.INVISIBLE
							tvCounter.visibility = if (visible) View.VISIBLE else View.INVISIBLE
						},
				view
						.updateCounter
						.subscribe { counterBundle ->
							tvCounter.text = "${counterBundle.currentPage} of ${counterBundle.totalPages}"
						},
				view
						.displayProgress
						.subscribe { displayProgress ->
							if (displayProgress) {
								progressDialog.start(this, "Processing")
							} else {
								progressDialog.stop()
							}
						})

		return view
	}
}
