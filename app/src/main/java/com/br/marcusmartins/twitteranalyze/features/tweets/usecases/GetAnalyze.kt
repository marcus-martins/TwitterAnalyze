package com.br.marcusmartins.twitteranalyze.features.tweets.usecases

import com.br.marcusmartins.twitteranalyze.core.interactor.UseCase
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.SentimentType
import com.br.marcusmartins.twitteranalyze.features.tweets.usecases.GetAnalyze.Params
import com.br.marcusmartins.twitteranalyze.features.tweets.repositories.GoogleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Named

class GetAnalyze
@Inject constructor(private val googleRepository: GoogleRepository,
                    @Named("UseCaseScope") scope: CoroutineScope,
                    @Named("UseCaseDispatcher")dispatcher: CoroutineDispatcher
) : UseCase<SentimentType, Params>(scope, dispatcher) {

    override suspend fun run(params: Params) = googleRepository.analyze(params.text)

    data class Params(val text: String)
}