package com.br.marcusmartins.twitteranalyze.features.tweets.usecases

import com.br.marcusmartins.twitteranalyze.UnitTest
import com.br.marcusmartins.twitteranalyze.core.functional.Either
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.SentimentType
import com.br.marcusmartins.twitteranalyze.features.tweets.repositories.GoogleRepository
import com.br.marcusmartins.twitteranalyze.features.tweets.usecases.GetAnalyze
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class GetAnalyzeTest : UnitTest() {

    private val analyzeText = "text"

    private lateinit var getAnalyze: GetAnalyze

    @Mock
    private lateinit var googleRepository: GoogleRepository

    @Mock
    private lateinit var scope: CoroutineScope

    @Mock
    private lateinit var dispatcher: CoroutineDispatcher

    @Before
    fun setUp() {
        getAnalyze = GetAnalyze(
            googleRepository,
            scope,
            dispatcher
        )
        given { googleRepository.analyze(analyzeText) }.willReturn(Either.Right(SentimentType.NEUTRAL))
    }

    @Test
    fun `should get data from repository`() {
        runBlocking { getAnalyze.run(GetAnalyze.Params(analyzeText)) }

        verify(googleRepository).analyze(analyzeText)
        verifyNoMoreInteractions(googleRepository)
    }
}