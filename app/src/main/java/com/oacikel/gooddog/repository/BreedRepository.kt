package com.oacikel.gooddog.repository

import androidx.lifecycle.MutableLiveData
import com.oacikel.gooddog.api.BreedService
import com.oacikel.gooddog.db.dao.DogDao
import com.oacikel.gooddog.db.entity.BreedImageListEntity
import com.oacikel.gooddog.db.entity.BreedListEntity
import com.oacikel.gooddog.db.entity.DogEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreedRepository @Inject constructor(
    private val service: BreedService,
    private val dogDao: DogDao
) {
    suspend fun fetchBreedsList(breedsLiveData: MutableLiveData<BreedListEntity>) {
        try {
            breedsLiveData.postValue(service.getBreeds())
        } catch (e: Exception) {
            breedsLiveData.postValue(BreedListEntity(status = "error",null))
        }
    }

    suspend fun fetchAllBreedPhotos(
        breedName: String,
        breedPhotosLiveData: MutableLiveData<BreedImageListEntity>
    ) {
        try {
            breedPhotosLiveData.postValue(service.getBreedImages(breedName))
        } catch (e: Exception) {
            breedPhotosLiveData.postValue(BreedImageListEntity(status = "error", null))
        }

    }

    suspend fun fetchAllSubBreedPhotos(
        breedName: String,
        subBreedName: String,
        subBreedPhotosLiveData: MutableLiveData<BreedImageListEntity>
    ) {
        try {
            subBreedPhotosLiveData.postValue(service.getSubBreedImages(breedName, subBreedName))
        }catch (e:Exception){
            subBreedPhotosLiveData.postValue(BreedImageListEntity(status = "error",null))
        }
    }

    fun likeDog(dog: DogEntity) {
        dogDao.insertDog(dog)
    }

    fun unlikeDog(imageUrl: String) {
        dogDao.deleteDog(imageUrl)
    }

    suspend fun getDogByUrl(imageUrl: String): DogEntity? {
        return dogDao.getDogByUrl(imageUrl)
    }

    fun filterDogs(breedName: String?, subBreedName: String?): List<DogEntity> {
        return when {
            breedName.isNullOrEmpty() -> {
                dogDao.getAllDogs()
            }
            subBreedName.isNullOrEmpty() -> {
                dogDao.filterDogsByBreed(breedName)
            }
            else -> {
                dogDao.filterDogsBySubBreed(subBreedName)
            }
        }
    }
}