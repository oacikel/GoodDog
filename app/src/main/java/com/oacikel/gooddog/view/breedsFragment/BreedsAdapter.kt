package com.oacikel.gooddog.view.breedsFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.oacikel.gooddog.R
import com.oacikel.gooddog.databinding.ItemBreedBinding
import com.oacikel.gooddog.db.entity.BreedEntity

class BreedsAdapter constructor(val callback: BreedClickCallback) :
    RecyclerView.Adapter<BreedsAdapter.BreedsViewHolder>() {
    private var breeds = arrayListOf<BreedEntity>()


    fun submitBreeds(breedEntities: ArrayList<BreedEntity>) {
        this.breeds = breedEntities
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BreedsViewHolder(
            DataBindingUtil.inflate
                (
                LayoutInflater.from(parent.context),
                R.layout.item_breed,
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: BreedsViewHolder,
        position: Int
    ) {

        val subBreedsAdapter = SubBreedsAdapter(callback,breeds[position])
        holder.itemBinding.breed = breeds[position]
        holder.itemBinding.callback = callback
        holder.itemBinding.recyclerViewSubBreed.adapter = subBreedsAdapter
        if (breeds[position].subBreedEntityList.isNotEmpty()) {
            subBreedsAdapter.submitSubBreeds(breeds[position].subBreedEntityList)
        }
    }

    override fun getItemCount(): Int {
        return breeds.size
    }


    inner class BreedsViewHolder(
        val itemBinding: ItemBreedBinding
    ) : RecyclerView.ViewHolder(itemBinding.root)


}