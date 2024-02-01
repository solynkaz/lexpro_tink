package com.example.lexpro_mobile.appmodule

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.lexpro_mobile.constants.DbConstants.BASE_URL
import com.example.lexpro_mobile.interceptors.CookieInterceptor
import com.example.lexpro_mobile.interceptors.GetCookieInterceptor
import com.example.lexpro_mobile.apiRepository.ApiRepos
import com.example.lexpro_mobile.apiRepository.api.*
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    @Named("logPostApi")
    fun performLoginPost(@ApplicationContext context: Context): PostLoginJSON {
        //val httpLoggingInterceptor = HttpLoggingInterceptor()
        //httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder().followRedirects(false)
                    //.addNetworkInterceptor(httpLoggingInterceptor)
                    //.addInterceptor(ChuckerInterceptor(context))
                    .addInterceptor(GetCookieInterceptor(context)).build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Singleton
    @Provides
    @Named("getStage")
    fun getStageList(@ApplicationContext context : Context): GetStageListJSON =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient().newBuilder().followRedirects(false)
                    .addInterceptor(CookieInterceptor(context))
                    //.addInterceptor(ChuckerInterceptor(context))
                .build()
            )
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build()
            .create(
                GetStageListJSON::class.java
            )

    @Singleton
    @Provides
    @Named("rkkFilter")
    fun rkkFilter(@ApplicationContext context: Context) : rkkFilter {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient().newBuilder().followRedirects(false)
                    //.addNetworkInterceptor(httpLoggingInterceptor)
                    .addInterceptor(ChuckerInterceptor(context))
                    .addInterceptor(CookieInterceptor(context)).build()

            )
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build()
            .create(
                rkkFilter::class.java
            )
    }
    @Singleton
    @Provides
    @Named("getAttachment")
    fun getAttachment(@ApplicationContext context: Context) : GetAttachment {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient().newBuilder().followRedirects(false)
                    //.addNetworkInterceptor(httpLoggingInterceptor)
                    .addInterceptor(ChuckerInterceptor(context))
                    .addInterceptor(CookieInterceptor(context)).build()

            )
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build()
            .create(
                GetAttachment::class.java
            )
    }

    @Singleton
    @Provides
    @Named("getMailingList")
    fun getMailingList(@ApplicationContext context: Context) : GetMailingList {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient().newBuilder().followRedirects(false)
                    //.addNetworkInterceptor(httpLoggingInterceptor)
                    .addInterceptor(ChuckerInterceptor(context))
                    .addInterceptor(CookieInterceptor(context)).build()

            )
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build()
            .create(
                GetMailingList::class.java
            )
    }

    @Singleton
    @Provides
    fun provideApiRepos(
        @Named("logPostApi") postPerformLogin: PostLoginJSON,
        @Named("getStage") getStageList: GetStageListJSON,
        @Named("rkkFilter") rkkFilter: rkkFilter,
        @Named("getAttachment") getAttachment: GetAttachment,
        @Named("getMailingList") getMailingList: GetMailingList
    ): ApiRepos = ApiRepos(postPerformLogin, getStageList, rkkFilter, getAttachment, getMailingList)
}