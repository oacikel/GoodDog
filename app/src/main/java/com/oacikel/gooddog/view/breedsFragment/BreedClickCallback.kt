package com.oacikel.gooddog.view.breedsFragment

interface BreedClickCallback {
    fun onBreedClicked(breedName: String)
    fun onSubBreedClicked(breedName: String, subBreedName: String)
}