package com.oacikel.gooddog.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.oacikel.gooddog.db.RoomConstants
import com.oacikel.gooddog.db.entity.DogEntity

@Dao
interface DogDao {
    @Insert
    fun insertDog(dog: DogEntity)

    @Query("SELECT * FROM ${RoomConstants.DOGS_TABLE_NAME}")
    fun getAllDogs(): List<DogEntity>

    @Query("DELETE FROM ${RoomConstants.DOGS_TABLE_NAME} WHERE imageUrl=:imageUrl")
    fun deleteDog(imageUrl: String)

    @Query("SELECT * FROM ${RoomConstants.DOGS_TABLE_NAME} WHERE imageUrl=:imageUrl")
    suspend fun getDogByUrl(imageUrl: String): DogEntity?

    @Query("SELECT * FROM ${RoomConstants.DOGS_TABLE_NAME} WHERE breedName = :breedName")
    fun filterDogsByBreed(breedName: String?): List<DogEntity>

    @Query("SELECT * FROM ${RoomConstants.DOGS_TABLE_NAME} WHERE subBreedName = :subBreedName")
    fun filterDogsBySubBreed(subBreedName: String?): List<DogEntity>

}