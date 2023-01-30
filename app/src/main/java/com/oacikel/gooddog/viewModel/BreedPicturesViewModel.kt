package com.oacikel.gooddog.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oacikel.gooddog.db.entity.BreedImageListEntity
import com.oacikel.gooddog.db.entity.DogEntity
import com.oacikel.gooddog.repository.BreedRepository
import com.oacikel.gooddog.util.createDogEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BreedPicturesViewModel @Inject constructor(
    private val repository: BreedRepository
) : BaseViewModel() {
    private val breedImagesLiveData = MutableLiveData<BreedImageListEntity>()
    val breedImages: LiveData<BreedImageListEntity> = breedImagesLiveData

    init {

    }

    suspend fun fetchBreedImages(breedName: String, subBreedName: String?) {
        if (subBreedName == null) {
            repository.fetchAllBreedPhotos(breedName, breedImagesLiveData)
        } else {
            repository.fetchAllSubBreedPhotos(breedName, subBreedName, breedImagesLiveData)
        }
    }

    fun likeBreed(breed: String, subBreed: String?, imageUrl: String) {
        repository.likeDog(
            createDogEntity(breed, subBreed, imageUrl)
        )
    }

    fun unlikeBreed(imageUrl: String) {
        repository.unlikeDog(imageUrl)
    }

    private suspend fun updateDogLikeStatus(dog: DogEntity) {
        if (repository.getDogByUrl(dog.imageUrl) != null) {
            dog.isLiked = true
        }
    }

    suspend fun getDogListForAdapter(
        breedImages: BreedImageListEntity,
        breedName: String,
        subBreedName: String?
    ): ArrayList<DogEntity> {
        val dogList = arrayListOf<DogEntity>()
        breedImages.message?.forEach { url ->
            createDogEntity(breedName, subBreedName, url).apply {
                updateDogLikeStatus(this)
                dogList.add(this)
            }
        }
        return dogList
    }
}