package com.br.marcusmartins.twitteranalyze.features.tweets.repositories

import com.br.marcusmartins.twitteranalyze.core.exception.Failure
import com.br.marcusmartins.twitteranalyze.core.functional.Either
import com.br.marcusmartins.twitteranalyze.core.platform.NetworkHandler
import com.br.marcusmartins.twitteranalyze.core.exception.Failure.NetworkConnection
import com.br.marcusmartins.twitteranalyze.core.exception.Failure.ServerError
import com.br.marcusmartins.twitteranalyze.core.functional.Either.Left
import com.br.marcusmartins.twitteranalyze.core.functional.Either.Right
import com.br.marcusmartins.twitteranalyze.features.tweets.services.GoogleService
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.Analyze
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.Document
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.SentimentResponse
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.SentimentType
import retrofit2.Call
import javax.inject.Inject

interface GoogleRepository {

    fun analyze(text: String): Either<Failure, SentimentType>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val service: GoogleService
    ) : GoogleRepository {
        private val DOCUMENT_TYPE = "PLAIN_TEXT"
        private val ENCODING_TYPE = "UTF8"

        override fun analyze(text: String): Either<Failure, SentimentType> {
            val analyze = Analyze(
                Document(
                    DOCUMENT_TYPE,
                    text
                ),
                ENCODING_TYPE
            )

            return when (networkHandler.isConnected) {
                true -> request(service.postAnalysis(analyze), { calculateScore(it.documentSentiment.score) }, SentimentResponse())
                false, null -> Left(NetworkConnection)
            }
        }

        private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> Right(transform((response.body() ?: default)))
                    false -> Left(ServerError)
                }
            } catch (exception: Throwable) {
                Left(ServerError)
            }
        }

        private fun calculateScore(score: Number): SentimentType {
            return when {
                score.toFloat() == 0.0f -> SentimentType.NEUTRAL
                score.toFloat() >= 0.1f -> SentimentType.POSITIVE
                score.toFloat() <= -0.1f -> SentimentType.NEGATIVE
                else -> SentimentType.NEUTRAL
            }
        }
    }
}