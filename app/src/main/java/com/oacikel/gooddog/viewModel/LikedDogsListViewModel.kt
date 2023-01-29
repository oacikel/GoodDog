package com.oacikel.gooddog.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.oacikel.gooddog.db.entity.BreedEntity
import com.oacikel.gooddog.db.entity.DogEntity
import com.oacikel.gooddog.repository.BreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikedDogsListViewModel @Inject constructor(
    private val repository: BreedRepository
) : BaseViewModel() {
    private var filteredDogsMutableLiveData = MutableLiveData<List<DogEntity>>()
    var filteredDogs: LiveData<List<DogEntity>> = filteredDogsMutableLiveData

    private val likedBreedsLiveData = MutableLiveData<List<BreedEntity>>()
    val likedBreeds: LiveData<List<BreedEntity>> = likedBreedsLiveData


    init {
        viewModelScope.launch {
            filteredDogsMutableLiveData.postValue(repository.filterDogs(null, null))
        }
    }

    fun likeDog(dogEntity: DogEntity) {
        repository.likeDog(dogEntity)
    }

    fun unlikeDog(dogEntity: DogEntity) {
        repository.unlikeDog(dogEntity.imageUrl)
    }

    private fun fetchBreedListFromDogList(dogs: List<DogEntity>?): ArrayList<BreedEntity> {
        val breeds = arrayListOf<BreedEntity>()
        dogs?.forEach { dog ->
            if (!breeds.any { it.breedName == dog.breedEntity.breedName }) {
                breeds.add(dog.breedEntity)
            } else {
                if (breeds.firstOrNull
                    { it.breedName == dog.breedEntity.breedName }?.subBreedEntityList?.any
                    { it.subBreedName == dog.breedEntity.subBreedEntityList[0].subBreedName }
                    == false
                ) {
                    breeds.firstOrNull { it.breedName == dog.breedEntity.breedName }?.subBreedEntityList!!.add(
                        dog.breedEntity.subBreedEntityList[0]
                    )
                }
            }
        }
        return breeds
    }

    fun updateBreedsList(dogs: List<DogEntity>?) {
        likedBreedsLiveData.value = fetchBreedListFromDogList(dogs)
    }

    fun filterLikedDogs(breedName: String?, subBreedName: String?) {
        filteredDogsMutableLiveData.postValue(repository.filterDogs(breedName, subBreedName))
    }

}