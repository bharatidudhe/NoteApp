package com.ai.noteapp.di

import com.ai.noteapp.api.AuthInterceptor
import com.ai.noteapp.api.NoteApi
import com.ai.noteapp.api.userApi
import com.ai.noteapp.utils.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit.Builder{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)

    }

    @Singleton
    @Provides
    fun providesOkhttpClient(authInterceptor: AuthInterceptor):OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun provdeUserApi(retrofitBuilder: Retrofit.Builder): userApi {
        return retrofitBuilder.build().create(userApi::class.java)
    }

    @Singleton
    @Provides
    fun provdeNoteApi(retrofitBuilder: Retrofit.Builder,okHttpClient: OkHttpClient): NoteApi {
        return retrofitBuilder.client(okHttpClient).build().create(NoteApi::class.java)
    }
}