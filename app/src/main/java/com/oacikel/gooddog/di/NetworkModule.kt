package com.oacikel.gooddog.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.oacikel.gooddog.BreedApp
import com.oacikel.gooddog.api.BreedService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.CookieManager
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private const val CACHE_SIZE = 10L
private const val CACHE_MEMORY_SIZE = 1024L

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()

    @Provides
    @Singleton
    fun provideBreedApi(retrofit: Retrofit): BreedService =
        retrofit.create(BreedService::class.java)

    @Singleton
    @Provides
    fun provideDispatcher(): Dispatcher {
        return Dispatcher()
    }

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext ctx: Context): Cache {
        // 10 MB Cache for Network Unavailable
        return Cache(
            File(ctx.cacheDir, "https"),
            CACHE_SIZE * CACHE_MEMORY_SIZE * CACHE_MEMORY_SIZE
        )
    }

    @Singleton
    @Provides
    @Named("OKHTTP_CLIENT")
    fun provideOKHttpClient(
        dispatcher: Dispatcher,
        cache: Cache,
    ): OkHttpClient {
        dispatcher.maxRequests = 4
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val cookieManager = CookieManager()
        cookieManager.cookieStore.removeAll()

        val chuckerCollector = ChuckerCollector(
            context = BreedApp.sContext,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )

        val chuckerInterceptor = ChuckerInterceptor.Builder(BreedApp.sContext)
            .collector(chuckerCollector)
            .maxContentLength(250_000L)
            .build()

        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(interceptor)
            .addNetworkInterceptor(chuckerInterceptor)
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .dispatcher(dispatcher)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(@Named("OKHTTP_CLIENT") client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BreedService.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}