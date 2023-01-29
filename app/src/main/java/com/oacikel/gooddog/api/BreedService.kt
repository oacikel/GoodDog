package com.oacikel.gooddog.api

import com.oacikel.gooddog.db.entity.BreedImageListEntity
import com.oacikel.gooddog.db.entity.BreedListEntity
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BreedService {
    companion object{
        const val BASE_URL ="https://dog.ceo/api/"
    }

    @GET("breeds/list/all")
    suspend fun getBreeds():BreedListEntity

    @GET("breed/{breed}/images")
    suspend fun getBreedImages(@Path("breed") breed: String): BreedImageListEntity

    @GET("breed/{breed}/{breed2}/images")
    suspend fun getSubBreedImages(@Path("breed") breed: String, @Path("breed2") subBreed: String): BreedImageListEntity
}