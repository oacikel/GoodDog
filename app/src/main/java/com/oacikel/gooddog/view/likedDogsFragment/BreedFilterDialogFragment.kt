package com.oacikel.gooddog.view.likedDogsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.oacikel.gooddog.R
import com.oacikel.gooddog.databinding.DialogBreedFilterBinding
import com.oacikel.gooddog.db.entity.BreedEntity
import com.oacikel.gooddog.db.entity.SubBreedEntity
import com.oacikel.gooddog.viewModel.LikedDogsListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreedFilterDialogFragment() :
    DialogFragment(), FilterSelectionCallback {

    companion object {
        var selectionCompleteCallback: FilterSelectionCompleteCallback? = null
    }

    lateinit var binding: DialogBreedFilterBinding
    private val viewModel: LikedDogsListViewModel by viewModels()
    lateinit var breedFilterAdapter: BreedFilterAdapter
    lateinit var subBreedFilterAdapter: SubBreedFilterAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_breed_filter, container, false
        )
        binding.filterCallback = this
        binding.filterSelectionCompleteCallback = selectionCompleteCallback
        binding.dialog = this
        observeBreeds()
        observeLikedDogs()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.setCanceledOnTouchOutside(true)
        }
    }


    private fun observeBreeds() {
        viewModel.likedBreeds.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                setBreedAdapter(it)
            }
        }
    }

    private fun observeLikedDogs() {
        viewModel.filteredDogs.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                viewModel.updateBreedsList(it)
                viewModel.filteredDogs.removeObservers(viewLifecycleOwner)
            }
        }
    }

    private fun setBreedAdapter(breeds: List<BreedEntity>) {
        breedFilterAdapter = BreedFilterAdapter(
            requireContext(), breeds.toTypedArray()
        )
        binding.spinnerBreeds.adapter = breedFilterAdapter
    }

    private fun setSubBreedAdapter(subBreedEntities: List<SubBreedEntity>) {
        subBreedFilterAdapter = SubBreedFilterAdapter(
            requireContext(), subBreedEntities.toTypedArray()
        )
        binding.spinnerSubBreeds.adapter = subBreedFilterAdapter
    }

    override fun breedFilterSelected(breed: BreedEntity?) {
        binding.selectedBreed = breed
        breed?.subBreedEntityList?.let { setSubBreedAdapter(it) }
    }

    override fun subBreedFilterSelected(breed: BreedEntity?, subBreedEntity: SubBreedEntity?) {
        binding.selectedBreed = breed
        binding.selectedSubBreed = subBreedEntity
    }
}