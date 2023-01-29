package com.oacikel.gooddog

import android.app.Activity
import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BreedApp : Application() {


    override fun onCreate() {
        super.onCreate()
        instance = this
        sContext = this
        sInstance = this
    }

    companion object {
        lateinit var sContext: Context
        lateinit var instance: BreedApp
        lateinit var activity: Activity
        private var sInstance: BreedApp? = null
        var isBackCount: Int = 0

        fun get(): BreedApp? {
            return sInstance
        }
    }
}