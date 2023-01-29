package com.oacikel.gooddog.binding

import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oacikel.gooddog.R
import com.oacikel.gooddog.db.entity.BreedEntity
import com.oacikel.gooddog.db.entity.DogEntity
import com.oacikel.gooddog.db.entity.SubBreedEntity
import com.oacikel.gooddog.util.Status
import com.oacikel.gooddog.view.breedPicturesFragment.ImageLikeToggleCallback
import com.oacikel.gooddog.view.breedsFragment.BreedClickCallback
import com.oacikel.gooddog.view.common.DialogOkButtonCallback
import com.oacikel.gooddog.view.common.ErrorDialogFragment
import com.oacikel.gooddog.view.common.LoadingDialogFragment
import com.oacikel.gooddog.view.likedDogsFragment.DogLikeToggleCallback
import com.oacikel.gooddog.view.likedDogsFragment.FilterSelectionCallback
import com.oacikel.gooddog.view.likedDogsFragment.FilterSelectionCompleteCallback
import com.oacikel.gooddog.viewModel.BaseViewModel


object BindingAdapter {

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter(
        value = ["loadingManager", "loadingStatus", "errorOkClick"],
        requireAll = false
    )
    fun View.onLoading(
        fragmentManager: Fragment?,
        baseViewModel: BaseViewModel?,
        okClick: DialogOkButtonCallback?
    ) {

        this.post {
            try {
                fragmentManager?.lifecycleScope?.launchWhenResumed {//IllegalStateException: Can not perform this action after onSaveInstanceState
                    if (fragmentManager.isAdded && !fragmentManager.isHidden) {
                        var loadingDialogFragment: LoadingDialogFragment

                        baseViewModel?.status?.observeForever {
                            if (fragmentManager.fragmentManager != null) {//NullPointerExeption not associated with a fragment manager
                                val showingLoading = fragmentManager.childFragmentManager
                                    .findFragmentByTag("LoadingDialogFragment")
                                if (baseViewModel.status.value == Status.LOADING) {
                                    if (showingLoading == null) {
                                        loadingDialogFragment = LoadingDialogFragment()
                                        loadingDialogFragment.isCancelable = false

                                        loadingDialogFragment.show(
                                            fragmentManager.childFragmentManager,
                                            "LoadingDialogFragment"
                                        )
                                        fragmentManager.childFragmentManager
                                            .executePendingTransactions()
                                    }
                                } else {
                                    val showingLoading =
                                        fragmentManager.childFragmentManager
                                            .findFragmentByTag("LoadingDialogFragment")
                                    if (showingLoading != null) {
                                        (showingLoading as LoadingDialogFragment).dismissAllowingStateLoss()
                                        fragmentManager.childFragmentManager
                                            .executePendingTransactions()
                                    }


                                    if (baseViewModel.errorMessage.value != 401.toString()) {
                                        if (baseViewModel.status.value == Status.ERROR) {
                                            onError(fragmentManager, baseViewModel, okClick)
                                        } else if (baseViewModel.status.value == Status.ONLY_MESSAGE_ERROR) {
                                            onError(fragmentManager, baseViewModel, okClick, true)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                //fragment assocation error
            }
        }
    }

    private fun onError(
        fragmentManager: Fragment,
        baseViewModel: BaseViewModel,
        dialogOkButtonCallback: DialogOkButtonCallback? = null,
        onlyMessage: Boolean? = false
    ) {
        baseViewModel.errorMessage.observeForever {
            ErrorDialogFragment.errorMessage = it
            ErrorDialogFragment.dialogOkButtonCallback = dialogOkButtonCallback
            if (onlyMessage != null && onlyMessage)
                ErrorDialogFragment.onlyMessage = onlyMessage
            val errorDialogFragment = ErrorDialogFragment()
            errorDialogFragment.isCancelable = true
            val showingError = fragmentManager.childFragmentManager
                .findFragmentByTag("ErrorDialogFragment")
            if (showingError == null) {
                errorDialogFragment.show(
                    fragmentManager.childFragmentManager,
                    "ErrorDialogFragment"
                )
                fragmentManager.childFragmentManager.executePendingTransactions()
            } else {
                (showingError as ErrorDialogFragment).binding.errorMessage = it
            }
        }

    }


    @JvmStatic
    @BindingAdapter(
        value = ["breedToCheckSubBreeds", "recyclerViewToShowHide"], requireAll = false
    )
    fun ImageView.showSubBreeds(
        breedEntity: BreedEntity?,
        subBreedList: RecyclerView,
    ) {
        if (breedEntity?.subBreedEntityList?.isNotEmpty() == true) {
            this.visibility = View.VISIBLE
            this.setOnClickListener {
                if (subBreedList.visibility == View.VISIBLE) {
                    subBreedList.visibility = View.GONE
                } else {
                    subBreedList.visibility = View.VISIBLE
                }
            }
        } else {
            this.visibility = View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter(
        value = ["breedToFetch", "breedClickCallback"]
    )
    fun TextView.handleBreedClick(
        breedEntity: BreedEntity?,
        callback: BreedClickCallback,
    ) {
        this.setOnClickListener {
            breedEntity?.breedName?.let { name -> callback.onBreedClicked(name) }
        }
    }

    @JvmStatic
    @BindingAdapter(
        value = ["breedToFetch", "subBreedToFetch", "breedClickCallback"]
    )
    fun TextView.handleSubBreedClick(
        breedEntity: BreedEntity?,
        subBreedEntity: SubBreedEntity?,
        callback: BreedClickCallback,
    ) {
        this.setOnClickListener {
            breedEntity?.breedName?.let { breed ->
                subBreedEntity?.subBreedName?.let { subBreed ->
                    callback.onSubBreedClicked(
                        breed,
                        subBreed
                    )
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter(
        value = ["imageUrl"]
    )
    fun ImageView.setImage(
        url: String,
    ) {
        Glide.with(this.context)
            .load(url)
            .into(this)
    }

    @JvmStatic
    @BindingAdapter(
        value = ["isLiked", "imageLikeCallback", "imageLikeUrl"]
    )
    fun ImageView.toggleLikeUnlike(
        isLiked: Boolean?,
        callback: ImageLikeToggleCallback,
        imageUrl: String
    ) {
        this.tag = isLiked == true

        if (this.tag == true) {
            this.setImageResource(R.drawable.ic_like_filled)
        } else {
            this.setImageResource(R.drawable.ic_like_empty)
        }

        this.setOnClickListener {
            if (this.tag == true) {
                this.setImageResource(R.drawable.ic_like_empty)
                callback.imageUnliked(imageUrl)
            } else {
                this.setImageResource(R.drawable.ic_like_filled)
                callback.imageLiked(imageUrl)
            }
            this.tag = !(this.tag as Boolean)
        }
    }

    @JvmStatic
    @BindingAdapter(
        value = ["dogLikeCallback", "dogToLikeUnlike"]
    )
    fun ImageView.toggleLikeUnlikeForDogEntity(
        callback: DogLikeToggleCallback,
        dog: DogEntity
    ) {
        this.setImageResource(R.drawable.ic_like_filled)
        this.tag = true
        this.setOnClickListener {
            if (this.tag == true) {
                callback.dogUnliked(dog)
            } else {
                callback.dogLiked(dog)
            }
            this.tag = !(this.tag as Boolean)
        }
    }

    @JvmStatic
    @BindingAdapter(
        value = ["filterCallback"]
    )
    fun Spinner.handleBreedFilterSelection(
        callback: FilterSelectionCallback?
    ) {
        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position)
                callback?.breedFilterSelected(selectedItem as BreedEntity)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    @JvmStatic
    @BindingAdapter(
        value = ["parentBreed", "filterCallback", "includeSubBreedCheckBox"]
    )
    fun Spinner.handleSubBreedFilterSelection(
        parentBreed: BreedEntity?,
        callback: FilterSelectionCallback?,
        checkBox: CheckBox
    ) {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            this.isEnabled = isChecked
            if (isChecked) {
                callback?.subBreedFilterSelected(parentBreed, selectedItem as SubBreedEntity)
            } else {
                callback?.subBreedFilterSelected(parentBreed, null)
            }
        }
        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                this@handleSubBreedFilterSelection.isEnabled = checkBox.isChecked
                val selectedItem = parent.getItemAtPosition(position)
                if (checkBox.isChecked) {
                    callback?.subBreedFilterSelected(parentBreed, selectedItem as SubBreedEntity)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    @JvmStatic
    @BindingAdapter(
        value = ["selectedBreed", "selectedSubBreed", "selectionCompleteCallback", "dialog"]
    )
    fun Button.onFilterSelectionComplete(
        selectedBreed: BreedEntity?,
        selectedSubBreed: SubBreedEntity?,
        callback: FilterSelectionCompleteCallback,
        dialog: DialogFragment
    ) {
        this.setOnClickListener {
            val breedName = selectedBreed?.breedName
            val subBreedName = selectedSubBreed?.subBreedName
            callback.filterSelected(breedName, subBreedName)
            dialog.dismiss()
        }
    }
}