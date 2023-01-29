package com.oacikel.gooddog.view.likedDogsFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.oacikel.gooddog.R
import com.oacikel.gooddog.databinding.FragmentLikedDogsBinding
import com.oacikel.gooddog.db.entity.DogEntity
import com.oacikel.gooddog.viewModel.LikedDogsListViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val LOG_TAG = "OCUL - LikedDogsFragment"

@AndroidEntryPoint
class LikedDogsFragment : Fragment(), DogLikeToggleCallback, FilterSelectionCompleteCallback {
    private val viewModel: LikedDogsListViewModel by viewModels()
    private lateinit var binding: FragmentLikedDogsBinding

    lateinit var imagesAdapter: LikedDogsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentLikedDogsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_liked_dogs, container, false
        )
        this.binding = binding
        binding.fragment = this
        binding.viewModel = viewModel
        init()

        return binding.root
    }


    private fun init() {
        viewModel.filterLikedDogs(null, null)
        observeFilteredDogs()
    }

    private fun observeFilteredDogs() {
        viewModel.filteredDogs.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                setImagesAdapter(it)
            } else {
                // TODO: Show empty page with description
            }
        }
    }

    private fun setImagesAdapter(dogs: List<DogEntity>?) {
        imagesAdapter = LikedDogsAdapter(this)
        binding.viewPagerImages.adapter = imagesAdapter
        if (!dogs.isNullOrEmpty()) {
            imagesAdapter.submitDogs(dogs as ArrayList<DogEntity>)
        }
    }

    override fun dogLiked(dog: DogEntity) {
        viewModel.likeDog(dog)
    }

    override fun dogUnliked(dog: DogEntity) {
        viewModel.unlikeDog(dog)
        imagesAdapter.removeDog(dog)
    }

    fun onFilterOptionClicked() {
        BreedFilterDialogFragment.selectionCompleteCallback = this
        val direction =
            LikedDogsFragmentDirections.actionLikedDogsFragmentToBreedFilterDialogFragment()
        findNavController()
            .navigate(direction)
    }

    override fun filterSelected(breedName: String?, subBreedName: String?) {
        Log.d(LOG_TAG, "Filter selection made for $breedName - $subBreedName")
        viewModel.filterLikedDogs(breedName, subBreedName)
    }
}