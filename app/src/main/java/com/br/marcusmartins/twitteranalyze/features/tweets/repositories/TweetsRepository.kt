package com.br.marcusmartins.twitteranalyze.features.tweets.repositories

import com.br.marcusmartins.twitteranalyze.core.exception.Failure
import com.br.marcusmartins.twitteranalyze.core.functional.Either
import com.br.marcusmartins.twitteranalyze.core.exception.Failure.NetworkConnection
import com.br.marcusmartins.twitteranalyze.core.exception.Failure.ServerError
import com.br.marcusmartins.twitteranalyze.core.functional.Either.Left
import com.br.marcusmartins.twitteranalyze.core.functional.Either.Right
import com.br.marcusmartins.twitteranalyze.core.platform.*
import com.br.marcusmartins.twitteranalyze.features.tweets.services.TweetsService
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.Tweets
import retrofit2.Call
import javax.inject.Inject

interface TweetsRepository {
    fun tweets(accountName: String): Either<Failure, List<Tweets>>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val service: TweetsService
    ) : TweetsRepository {

        override fun tweets(accountName: String): Either<Failure, List<Tweets>> {
            return when (networkHandler.isConnected) {
                true -> request(service.getTweets(accountName), { it }, emptyList())
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
    }
}