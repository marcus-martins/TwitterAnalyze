package com.br.marcusmartins.twitteranalyze.features.tweets.services

import com.br.marcusmartins.twitteranalyze.features.tweets.entities.Analyze
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class GoogleService
@Inject constructor(@Named("GoogleRetrofit") retrofit: Retrofit) :
    GoogleApi {
    private val googleApi by lazy { retrofit.create(GoogleApi::class.java) }

    override fun postAnalysis(request: Analyze) =
        googleApi.postAnalysis(request)
}