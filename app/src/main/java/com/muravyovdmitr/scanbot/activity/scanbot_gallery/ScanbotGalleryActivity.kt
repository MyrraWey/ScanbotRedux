package com.muravyovdmitr.scanbot.activity.scanbot_gallery

import android.app.Activity
import android.database.DataSetObserver
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.PopupWindow
import com.jakewharton.rxbinding2.view.clicks
import com.muravyovdmitr.scanbot.R
import com.muravyovdmitr.scanbot.activity.scanbot_crop.ScanbotCropActivity
import com.muravyovdmitr.scanbot.redux.scanbot_gallery.ScanbotGallery
import com.muravyovdmitr.scanbot.redux.scanbot_gallery.ScanbotGalleryModel
import com.muravyovdmitr.scanbot.redux.scanbot_gallery.ScanbotGalleryPresenter
import com.muravyovdmitr.scanbot.redux.scanbot_gallery.StubScanbotBitmapRepository
import com.muravyovdmitr.scanbot.repository.BitmapRepository
import com.muravyovdmitr.scanbot.repository.ScanbotPicture
import com.muravyovdmitr.scanbot.repository.ColorFilterProvider
import com.muravyovdmitr.scanbot.repository.ColorFilterToNameMapper
import com.muravyovdmitr.scanbot.repository.ColorFilterType
import com.muravyovdmitr.scanbot.view.LockProgressDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_pictures_view.*

class ScanbotGalleryActivity : Activity() {
	private val model: ScanbotGallery.Model = ScanbotGalleryModel(ScanbotGallery.State(listOf(), false))
	private val presenter: ScanbotGallery.Presenter = ScanbotGalleryPresenter(model)
	private val compositeDisposable = CompositeDisposable()
	private val currentPictureChanged = PublishSubject.create<Int>()
	private val filterSelected = PublishSubject.create<ColorFilterType>()
	private val bitmapRepository: BitmapRepository = StubScanbotBitmapRepository()/*TODO ScanbotRepositoryKeeper.bitmapRepository*/
	private val picturesViewPagerAdapter = ScanbotGalleryPagerAdapter(bitmapRepository)
	private val filterProvider = ColorFilterProvider()
	private val filterToNameMapper = ColorFilterToNameMapper()
	private val progressDialog = LockProgressDialog()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_pictures_view)

		model.init()
		configureViews()
		picturesViewPagerAdapter.registerDataSetObserver(object : DataSetObserver() {

			override fun onChanged() {
				if (picturesViewPagerAdapter.pictures.isNotEmpty()) {
					currentPictureChanged.onNext(0)
					tvCounter.visibility = View.VISIBLE
				} else {
					tvCounter.visibility = View.INVISIBLE
				}
			}
		})
		compositeDisposable.addAll(
				currentPictureChanged
						.subscribe { currentPage ->
							tvCounter.text = "${currentPage + 1} of ${picturesViewPagerAdapter.pictures.size}"
						})


		//TODO: fix code of CropActivity launching; pass correct image scanbotPictureId
		ivCrop.setOnClickListener({
			startActivity(ScanbotCropActivity.createIntent(this, 0))
		})
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

	private fun createViewAndInitDisposables(): ScanbotGallery.View {
		val view = object : ScanbotGallery.View {
			override val showPictures = PublishSubject.create<List<ScanbotPicture>>()
			override val displayProgress = PublishSubject.create<Boolean>()
			override val onApplyFilter =
					filterSelected
							.map { filterType -> ScanbotGallery.FilterAction(getCurrentPictureId(), filterType) }
			override val onRotatePicture =
					ivRotate
							.clicks()
							.map { getCurrentPictureId() }
			override val onDeletePicture =
					ivDelete
							.clicks()
							.map { getCurrentPictureId() }
		}

		compositeDisposable.addAll(
				view
						.showPictures
						.subscribe { pictures ->
							picturesViewPagerAdapter.pictures = pictures.toMutableList()
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

	private fun getCurrentPictureId() = picturesViewPagerAdapter.pictures[vpPager.currentItem].id
}
