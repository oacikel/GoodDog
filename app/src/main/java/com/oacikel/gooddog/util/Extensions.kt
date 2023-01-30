package com.oacikel.gooddog.util

import androidx.lifecycle.MutableLiveData
import com.oacikel.gooddog.db.entity.BreedEntity
import com.oacikel.gooddog.db.entity.BreedListEntity
import com.oacikel.gooddog.db.entity.DogEntity
import com.oacikel.gooddog.db.entity.SubBreedEntity

fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }
fun BreedListEntity.getAsBreedList(): ArrayList<BreedEntity> {
    val breedEntityList: ArrayList<BreedEntity> = arrayListOf()
    val breedMap = this.message
    val subBreedEntityList: ArrayList<SubBreedEntity> = arrayListOf()
    breedMap?.forEach { (breed, subBreeds) ->
        subBreeds.forEach { name ->
            subBreedEntityList.add(
                SubBreedEntity(
                    subBreedName = name
                )
            )
        }
        breedEntityList.add(
            BreedEntity(
                breedName = breed,
                subBreedEntityList = ArrayList(subBreedEntityList)
            )
        )
        subBreedEntityList.clear()
    }
    return breedEntityList
}

fun createDogEntity(breed: String, subBreed: String?, imageUrl: String): DogEntity {
    return DogEntity(
        id = 0,
        breedEntity = BreedEntity(breed, arrayListOf(SubBreedEntity(subBreed))),
        imageUrl = imageUrl,
        isLiked = null,
        breedName = breed,
        subBreedName = subBreed
    )
}