package com.example.icfealabanza.di

import com.example.icfealabanza.common.constants.BASE_URL
import com.example.icfealabanza.data.network.api_client.DeezerApiClient
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun getApiClient(retrofit: Retrofit): DeezerApiClient =
        retrofit.create(DeezerApiClient::class.java)

    @Singleton
    @Provides
    fun getFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

}