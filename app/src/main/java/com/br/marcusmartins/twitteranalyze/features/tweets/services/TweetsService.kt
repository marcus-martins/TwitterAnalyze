package com.br.marcusmartins.twitteranalyze.features.tweets.services

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TweetsService
@Inject constructor(@Named("TwitterRetrofit") retrofit: Retrofit) :
    TweetsApi {
    private val tweetsApi by lazy { retrofit.create(TweetsApi::class.java) }

    override fun getTweets(accountName: String) =
        tweetsApi.getTweets(accountName)
}