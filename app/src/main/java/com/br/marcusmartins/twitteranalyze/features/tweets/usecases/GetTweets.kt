package com.br.marcusmartins.twitteranalyze.features.tweets.usecases

import com.br.marcusmartins.twitteranalyze.core.interactor.UseCase
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.Tweets
import com.br.marcusmartins.twitteranalyze.features.tweets.usecases.GetTweets.Params
import com.br.marcusmartins.twitteranalyze.features.tweets.repositories.TweetsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Named

class GetTweets
@Inject constructor(private val tweetsRepository: TweetsRepository,
                    @Named("UseCaseScope") scope: CoroutineScope,
                    @Named("UseCaseDispatcher")dispatcher: CoroutineDispatcher
) : UseCase<List<Tweets>, Params>(scope, dispatcher) {

    override suspend fun run(params: Params) = tweetsRepository.tweets(params.accountName)

    data class Params(val accountName: String)
}