package com.oacikel.gooddog.view.likedDogsFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.oacikel.gooddog.R
import com.oacikel.gooddog.databinding.SpinnerItemSubBreedBinding
import com.oacikel.gooddog.db.entity.SubBreedEntity

class SubBreedFilterAdapter constructor(
    private val context: Context,
    private val list: Array<SubBreedEntity>
) :
    BaseAdapter() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun getCount() = list.size

    override fun getItem(position: Int) = list[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: SpinnerItemSubBreedBinding
        if (convertView == null) {
            binding =
                DataBindingUtil.inflate(
                    layoutInflater,
                    R.layout.spinner_item_sub_breed,
                    parent,
                    false
                )
        } else {
            binding = DataBindingUtil.getBinding(convertView)!!
        }

        binding.subBreed = getItem(position)
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: SpinnerItemSubBreedBinding
        if (convertView == null) {
            binding =
                DataBindingUtil.inflate(
                    layoutInflater,
                    R.layout.spinner_item_sub_breed,
                    parent,
                    false
                )
        } else {
            binding = DataBindingUtil.getBinding(convertView)!!
        }
        binding.subBreed = getItem(position)
        return binding.root
    }
}