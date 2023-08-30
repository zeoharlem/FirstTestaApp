package com.zeoharlem.testaapp.repository

import androidx.lifecycle.asLiveData
import com.google.gson.GsonBuilder
import com.zeoharlem.testaapp.common.Logg
import com.zeoharlem.testaapp.data.local.AppDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val USER_AGENT = "first-testa-AbcDefGh"

    @Singleton
    @Provides
    fun provideOkHttpClient(cacheData: AppDataStore): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(getInterceptor(cacheData))
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        val gson = GsonBuilder().setLenient().create()
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideScalarConverterFactory(): ScalarsConverterFactory {
        return ScalarsConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        scalarsConverterFactory: ScalarsConverterFactory,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .addConverterFactory(scalarsConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): FirstTestaApiServices {
        return retrofit.create(FirstTestaApiServices::class.java)
    }

    @Singleton
    @Provides
    fun provideCacheData() = AppDataStore

    private fun getInterceptor(cacheData: AppDataStore) = Interceptor {
        val request = it.request().newBuilder()
            .addHeader("Authorization", "Bearer ${cacheData.getToken()}")

        // TODO: Conditional Debugger logg disabled on production

        request.addHeader("User-Agent", USER_AGENT)
        it.proceed(request.build())
    }

    private fun getLoggingInterceptor() = HttpLoggingInterceptor { message ->
        Logg.debug(message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}