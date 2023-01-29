package com.oacikel.gooddog.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oacikel.gooddog.db.entity.BreedEntity

object BreedEntityConverter {

    @TypeConverter
    @JvmStatic
    fun fromBreedEntity(breedEntityEntity: BreedEntity?): String? {
        if (breedEntityEntity == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<BreedEntity>() {

        }.type
        return gson.toJson(breedEntityEntity, type)
    }

    @TypeConverter
    @JvmStatic
    fun toVehicleEntityList(breedEntity: String?): BreedEntity? {
        if (breedEntity == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<BreedEntity>() {

        }.type
        return gson.fromJson<BreedEntity>(breedEntity, type)
    }
}