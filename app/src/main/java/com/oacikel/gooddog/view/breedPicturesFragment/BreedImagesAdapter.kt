package com.oacikel.gooddog.view.breedPicturesFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.oacikel.gooddog.R
import com.oacikel.gooddog.databinding.ItemBreedImageBinding
import com.oacikel.gooddog.db.entity.DogEntity

class BreedImagesAdapter constructor(val callback: ImageLikeToggleCallback) :
    RecyclerView.Adapter<BreedImagesAdapter.BreedImagesViewHolder>() {
    private var dogs = arrayListOf<DogEntity>()


    fun submitDogs(dogs: ArrayList<DogEntity>) {
        this.dogs = dogs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BreedImagesViewHolder(
            DataBindingUtil.inflate
                (
                LayoutInflater.from(parent.context),
                R.layout.item_breed_image,
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: BreedImagesViewHolder,
        position: Int
    ) {
        holder.itemBinding.dog = dogs[position]
        holder.itemBinding.callback= callback
    }

    override fun getItemCount(): Int {
        return dogs.size
    }


    inner class BreedImagesViewHolder(
        val itemBinding: ItemBreedImageBinding
    ) : RecyclerView.ViewHolder(itemBinding.root)
}