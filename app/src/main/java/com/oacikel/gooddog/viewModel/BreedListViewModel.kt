package com.oacikel.gooddog.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oacikel.gooddog.db.entity.BreedListEntity
import com.oacikel.gooddog.repository.BreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedListViewModel @Inject constructor(
    private val repository: BreedRepository
) : BaseViewModel() {
    private val breedsLiveData = MutableLiveData<BreedListEntity>()
    val breeds: LiveData<BreedListEntity> = breedsLiveData

    init {
        viewModelScope.launch {
            repository.fetchBreedsList(breedsLiveData)
        }
    }
}