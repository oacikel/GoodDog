package com.oacikel.gooddog.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.oacikel.gooddog.db.converter.BreedEntityConverter
import com.oacikel.gooddog.db.dao.DogDao
import com.oacikel.gooddog.db.entity.DogEntity

@Database(
    entities = [DogEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(
    BreedEntityConverter::class
)

abstract class AppDb : RoomDatabase() {
    abstract fun dogDao(): DogDao
}