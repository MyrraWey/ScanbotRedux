package com.muravyovdmitr.scanbot.activity.pictures_view

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.PopupWindow
import com.develop.zuzik.redux.core.model.Version
import com.muravyovdmitr.scanbot.R
import com.muravyovdmitr.scanbot.redux.pictures_view.PicturesView
import com.muravyovdmitr.scanbot.redux.pictures_view.PicturesViewModel
import com.muravyovdmitr.scanbot.redux.pictures_view.PicturesViewPresenter
import com.muravyovdmitr.scanbot.redux.pictures_view.StubPicturesRepository
import com.muravyovdmitr.scanbot.redux.pictures_view.filter.FilterProvider
import com.muravyovdmitr.scanbot.redux.pictures_view.filter.FilterToNameMapper
import com.muravyovdmitr.scanbot.redux.pictures_view.filter.FilterType
import com.muravyovdmitr.scanbot.view.LockProgressDialog
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_pictures_view.*
import java.util.concurrent.TimeUnit

class PicturesViewActivity : Activity() {
	private val model: PicturesView.Model = PicturesViewModel(PicturesView.State(Version(data = listOf()), null, false), StubPicturesRepository())
	private val presenter: PicturesView.Presenter = PicturesViewPresenter(model)
	private val compositeDisposable = CompositeDisposable()
	private val currentPictureChanged = PublishSubject.create<Int>()
	private val filterSelected = PublishSubject.create<FilterType>()
	private val picturesViewPagerAdapter = PicturesViewPagerAdapter()
	private val filterProvider = FilterProvider()
	private val filterToNameMapper = FilterToNameMapper()
	private val progressDialog = LockProgressDialog()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_pictures_view)

		model.init()
		configureViews()

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

	private fun configureViews() {
		configureViewPager()
		ivFilter.setOnClickListener { showFiltersPopup() }
	}

	private fun showFiltersPopup() {
		val filters = filterProvider.getAvailableFilters()
		val filterNames = filters.map { filterType -> filterToNameMapper.map(filterType) }
		val popupWindow = createFilterPopup(
				filterNames,
				{ selectedItemIndex -> filterSelected.onNext(filters[selectedItemIndex]) })
		popupWindow.showAtLocation(rlContent, Gravity.CENTER, 0, 0)
	}

	private fun createFilterPopup(items: List<String>, itemSelected: (Int) -> Unit): PopupWindow {
		val popupWindow = PopupWindow(this)
		val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items)
		val listView = ListView(this)
		listView.adapter = adapter
		listView.onItemClickListener = AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
			itemSelected.invoke(p2)
			popupWindow.dismiss()
		}
		popupWindow.isFocusable = true
		popupWindow.width = WindowManager.LayoutParams.WRAP_CONTENT
		popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
		popupWindow.contentView = listView
		return popupWindow
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
			override val onCurrentPictureChanged: Observable<Int> = currentPictureChanged.sample(500, TimeUnit.MILLISECONDS)
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
