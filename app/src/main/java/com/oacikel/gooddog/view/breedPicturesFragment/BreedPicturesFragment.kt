package com.oacikel.gooddog.view.breedPicturesFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.oacikel.gooddog.R
import com.oacikel.gooddog.databinding.FragmentBreedImagesListBinding
import com.oacikel.gooddog.db.entity.BreedImageListEntity
import com.oacikel.gooddog.viewModel.BreedPicturesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val LOG_TAG = "OCUL - BreedPicturesFragment"

@AndroidEntryPoint
class BreedPicturesFragment : Fragment(), ImageLikeToggleCallback {
    private val viewModel: BreedPicturesViewModel by viewModels()
    private lateinit var binding: FragmentBreedImagesListBinding
    val args: BreedPicturesFragmentArgs by navArgs()

    lateinit var imagesAdapter: BreedImagesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentBreedImagesListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_breed_images_list, container, false
        )
        this.binding = binding
        init()

        return binding.root
    }


    private fun init() {
        observeBreedPictures()
        fetchImages()
    }

    private fun fetchImages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchBreedImages(args.breedName, args.subBreedName)
        }
    }

    private fun observeBreedPictures() {

        viewModel.breedImages.observe(viewLifecycleOwner) {
            if (it.status == "success") {
                setAdapter(it)
            }

        }

    }

    private fun setAdapter(breedListEntity: BreedImageListEntity) {
        viewLifecycleOwner.lifecycleScope.launch {
            imagesAdapter = BreedImagesAdapter(this@BreedPicturesFragment)
            binding.viewPagerImages.adapter = imagesAdapter
            imagesAdapter.submitDogs(
                viewModel.getDogListForAdapter(
                    breedListEntity,
                    args.breedName,
                    args.subBreedName
                )
            )
        }
    }

    override fun imageLiked(imageUrl: String) {
        Log.d(
            LOG_TAG,
            "Liked an image of a dog from breed ${args.breedName}, sub breed:${args.subBreedName}"
        )
        viewModel.likeBreed(args.breedName, args.subBreedName, imageUrl)
    }

    override fun imageUnliked(imageUrl: String) {
        Log.d(
            LOG_TAG,
            "Unliked an image of a dog from breed ${args.breedName}, sub breed:${args.subBreedName}"
        )
        viewModel.unlikeBreed(imageUrl)
    }
}