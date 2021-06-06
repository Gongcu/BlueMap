package com.bluemap.overcom_blue.di

import com.bluemap.overcom_blue.network.BluemapAPI
import com.bluemap.overcom_blue.user.UserManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://bluemap.site/"

@Module
@InstallIn(SingletonComponent::class) // => ApplicationComponent is deprecated
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(BluemapAPI::class.java)

    @Provides
    @Singleton
    fun provideUserManager() = UserManager
}