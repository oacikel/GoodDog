package com.oacikel.gooddog.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.oacikel.gooddog.db.RoomConstants
import com.oacikel.gooddog.db.converter.BreedEntityConverter

@Entity(tableName = RoomConstants.DOGS_TABLE_NAME)
data class DogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @TypeConverters(BreedEntityConverter::class) val breedEntity: BreedEntity,
    val imageUrl: String,
    @ColumnInfo(name = RoomConstants.DOG_ENTITY_BREED_NAME)val breedName: String,
    @ColumnInfo(name = RoomConstants.DOG_ENTITY_SUB_BREED_NAME)val subBreedName: String?,
    var isLiked: Boolean?,
)