package com.oacikel.gooddog.view.breedsFragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.oacikel.gooddog.R
import com.oacikel.gooddog.databinding.FragmentBreedsListBinding
import com.oacikel.gooddog.db.entity.BreedEntity
import com.oacikel.gooddog.util.Status
import com.oacikel.gooddog.util.getAsBreedList
import com.oacikel.gooddog.viewModel.BreedListViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val LOG_TAG = "OCUL - BreedsFragment"

@AndroidEntryPoint
class BreedsFragment : Fragment(), BreedClickCallback {
    private val viewModel: BreedListViewModel by viewModels()
    private lateinit var binding: FragmentBreedsListBinding

    lateinit var breedsAdapter: BreedsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentBreedsListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_breeds_list, container, false
        )
        this.binding = binding
        init()
        return binding.root
    }

    private fun init() {
        viewModel.status.postValue(Status.LOADING)
        binding.fragment = this
        binding.viewModel = viewModel
        handleMenuClicks()
        observeBreeds()
    }

    private fun handleMenuClicks() {
        binding.toolbarBreedsList.inflateMenu(R.menu.menu_breeds_list)
        binding.toolbarBreedsList.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_liked_dogs -> {
                    onLikedImagesButtonClicked()
                    true
                }
                else -> false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                onLikedImagesButtonClicked()
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_breeds_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun observeBreeds() {

        viewModel.breeds.observe(viewLifecycleOwner) {
            if(it!=null){
                if (it.status == "success") {
                    viewModel.status.postValue(Status.INVISIBLE_SUCCESS)
                    setAdapter(it.getAsBreedList())
                }else if(it.status=="error"){
                    viewModel.status.postValue(Status.ERROR)
                }
            }
        }
    }

    fun onLikedImagesButtonClicked() {
        val direction =
            BreedsFragmentDirections.actionBreedsFragmentToLikedDogsFragment()
        findNavController()
            .navigate(direction)
    }

    private fun setAdapter(breedEntities: ArrayList<BreedEntity>) {
        breedsAdapter = BreedsAdapter(this)
        binding.recyclerViewBreedsList.adapter = breedsAdapter
        breedsAdapter.submitBreeds(breedEntities)
    }

    override fun onBreedClicked(breedName: String) {
        Log.d(LOG_TAG, "Clicked on $breedName.")
        val direction =
            BreedsFragmentDirections.actionBreedsFragmentToBreedPicturesFragment(
                breedName = breedName,
                subBreedName = null,
                isSubBreed = false
            )
        findNavController()
            .navigate(direction)
    }

    override fun onSubBreedClicked(breedName: String, subBreedName: String) {
        Log.d(LOG_TAG, "Clicked on $subBreedName, which is a sub breed of $breedName.")
        val direction =
            BreedsFragmentDirections.actionBreedsFragmentToBreedPicturesFragment(
                breedName = breedName,
                subBreedName = subBreedName,
                isSubBreed = true
            )
        findNavController()
            .navigate(direction)
    }
}