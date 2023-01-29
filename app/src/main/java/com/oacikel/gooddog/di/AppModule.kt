package com.oacikel.gooddog.di

import android.app.Application
import androidx.room.Room
import com.oacikel.gooddog.db.AppDb
import com.oacikel.gooddog.db.dao.DogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDogDao(db: AppDb): DogDao {
        return db.dogDao()
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDb {
        return Room.databaseBuilder(app, AppDb::class.java, "dog-db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration().build()
    }
}