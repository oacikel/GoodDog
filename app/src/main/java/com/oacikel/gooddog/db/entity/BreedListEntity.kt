package com.oacikel.gooddog.db.entity

data class BreedListEntity (
    val status: String,
    val message: Map<String, ArrayList<String>>
)
