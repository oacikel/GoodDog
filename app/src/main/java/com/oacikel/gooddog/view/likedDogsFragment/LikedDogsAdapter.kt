package com.oacikel.gooddog.view.likedDogsFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.oacikel.gooddog.R
import com.oacikel.gooddog.databinding.ItemLikedDogBinding
import com.oacikel.gooddog.db.entity.DogEntity

class LikedDogsAdapter constructor(val callback: DogLikeToggleCallback) :
    RecyclerView.Adapter<LikedDogsAdapter.LikedDogsViewHolder>() {
    private var dogs = arrayListOf<DogEntity>()


    fun submitDogs(dogs: ArrayList<DogEntity>?) {
        if (dogs != null) {
            this.dogs = dogs
        }
        notifyDataSetChanged()
    }

    fun removeDog(dog: DogEntity) {
        val index = dogs.indexOfFirst { it.imageUrl == dog.imageUrl }
        dogs.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LikedDogsViewHolder(
            DataBindingUtil.inflate
                (
                LayoutInflater.from(parent.context),
                R.layout.item_liked_dog,
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: LikedDogsViewHolder,
        position: Int
    ) {
        holder.itemBinding.dog = dogs[position]
        holder.itemBinding.callback = callback
    }

    override fun getItemCount(): Int {
        return dogs.size
    }


    inner class LikedDogsViewHolder(
        val itemBinding: ItemLikedDogBinding
    ) : RecyclerView.ViewHolder(itemBinding.root)
}