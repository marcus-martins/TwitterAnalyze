package com.br.marcusmartins.twitteranalyze.features.tweets.services

import com.br.marcusmartins.twitteranalyze.features.tweets.entities.Analyze
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.SentimentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

internal interface GoogleApi {
        companion object {
            private const val ANALYSIS = "v1/documents:analyzeSentiment"
        }

        @POST(ANALYSIS)
        fun postAnalysis(@Body request: Analyze): Call<SentimentResponse>
}