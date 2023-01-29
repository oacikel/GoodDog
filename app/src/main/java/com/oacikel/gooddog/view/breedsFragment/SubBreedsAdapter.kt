package com.oacikel.gooddog.view.breedsFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.oacikel.gooddog.R
import com.oacikel.gooddog.databinding.ItemSubBreedBinding
import com.oacikel.gooddog.db.entity.BreedEntity
import com.oacikel.gooddog.db.entity.SubBreedEntity

class SubBreedsAdapter constructor(
    private val callback: BreedClickCallback,
    private val parentBreedEntity: BreedEntity
) :
    RecyclerView.Adapter<SubBreedsAdapter.SubBreedsViewHolder>() {
    private var subBreeds = arrayListOf<SubBreedEntity>()

    fun submitSubBreeds(subBreedEntities: ArrayList<SubBreedEntity>) {
        this.subBreeds = subBreedEntities
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SubBreedsViewHolder(
            DataBindingUtil.inflate
                (
                LayoutInflater.from(parent.context),
                R.layout.item_sub_breed,
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: SubBreedsViewHolder,
        position: Int
    ) {
        holder.itemBinding.subBreed = subBreeds[position]
        holder.itemBinding.breed = parentBreedEntity
        holder.itemBinding.callBack = callback
    }

    override fun getItemCount(): Int {
        return subBreeds.size
    }

    inner class SubBreedsViewHolder(
        val itemBinding: ItemSubBreedBinding
    ) : RecyclerView.ViewHolder(itemBinding.root)


}