package com.oacikel.gooddog.db.entity

import java.io.Serializable

data class BreedEntity(
    val breedName: String,
    val subBreedEntityList: ArrayList<SubBreedEntity>
) : Serializable