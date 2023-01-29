package com.oacikel.gooddog.view.likedDogsFragment

import com.oacikel.gooddog.db.entity.DogEntity

interface DogLikeToggleCallback {
    fun dogLiked(dog:DogEntity)
    fun dogUnliked(dog:DogEntity)
}