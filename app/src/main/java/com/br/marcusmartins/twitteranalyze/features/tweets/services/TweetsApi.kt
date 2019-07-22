package com.br.marcusmartins.twitteranalyze.features.tweets.services

import com.br.marcusmartins.twitteranalyze.features.tweets.entities.Tweets

import retrofit2.Call
import retrofit2.http.*

internal interface TweetsApi {
    companion object {
        private const val TWEETS = "1.1/statuses/user_timeline.json"
        private const val TWITTER_NAME = "screen_name"
    }

    @GET(TWEETS) fun getTweets(@Query(
        TWITTER_NAME
    ) accountName: String): Call<List<Tweets>>
}