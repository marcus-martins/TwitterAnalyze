package com.br.marcusmartins.twitteranalyze.core.di

import android.content.Context
import com.br.marcusmartins.twitteranalyze.AndroidApplication
import com.br.marcusmartins.twitteranalyze.BuildConfig
import com.br.marcusmartins.twitteranalyze.R
import com.br.marcusmartins.twitteranalyze.core.platform.DataStore
import com.br.marcusmartins.twitteranalyze.core.platform.TwitterAuthService
import com.br.marcusmartins.twitteranalyze.core.platform.TwitterTokenInterceptor
import com.br.marcusmartins.twitteranalyze.features.tweets.repositories.GoogleRepository
import com.br.marcusmartins.twitteranalyze.features.tweets.repositories.TweetsRepository
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: AndroidApplication) {
    private val twitterUrl = application.getString(R.string.twitter_api_url)
    private val googleUrl = application.getString(R.string.google_api_url)
    private val googleApiKey = application.getString(R.string.google_api_key)

    @Provides @Singleton fun provideApplicationContext(): Context = application

    @Provides @Singleton fun provideDataStore(context: Context): DataStore = DataStore(context)

    @Named("AuthRetrofit")
    @Provides
    @Singleton
    fun providetwitter(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(twitterUrl)
            .client(createClient(null))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Named("TwitterRetrofit")
    @Provides
    @Singleton
    fun provideTwitterRetrofit(twitterTokenInterceptor: TwitterTokenInterceptor): Retrofit {
        return Retrofit.Builder()
                .baseUrl(twitterUrl)
                .client(createClient(twitterTokenInterceptor))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Named("GoogleRetrofit")
    @Provides
    @Singleton
    fun provideGoogleRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(googleUrl)
            .client(createClient(Interceptor { chain ->
                val url = chain.
                    request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("key", googleApiKey)
                    .build()
                chain.proceed(chain.request().newBuilder().url(url).build())
            }))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createClient(interceptor: Interceptor?): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        interceptor?.let { okHttpClientBuilder.addInterceptor(it) }
        return okHttpClientBuilder
            .protocols(Arrays.asList(Protocol.HTTP_1_1))
            .build()
    }

    @Provides @Singleton fun provideTokenInterceptor(context: Context, dataStore: DataStore, twitterAuthService: TwitterAuthService) =
        TwitterTokenInterceptor(context, dataStore, twitterAuthService)

    @Provides @Singleton fun provideTweetsRepository(dataSource: TweetsRepository.Network): TweetsRepository = dataSource

    @Provides @Singleton fun provideGoogleRepository(dataSource: GoogleRepository.Network): GoogleRepository = dataSource

    @Provides @Singleton fun provideAuthService(@Named("AuthRetrofit") retrofit: Retrofit): TwitterAuthService = retrofit.create(TwitterAuthService::class.java)
}
