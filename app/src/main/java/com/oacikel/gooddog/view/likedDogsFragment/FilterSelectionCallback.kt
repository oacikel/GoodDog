package com.oacikel.gooddog.view.likedDogsFragment

import com.oacikel.gooddog.db.entity.BreedEntity
import com.oacikel.gooddog.db.entity.SubBreedEntity

interface FilterSelectionCallback {
    fun breedFilterSelected(breed: BreedEntity?)
    fun subBreedFilterSelected(breed:BreedEntity?, subBreedEntity: SubBreedEntity?)
}