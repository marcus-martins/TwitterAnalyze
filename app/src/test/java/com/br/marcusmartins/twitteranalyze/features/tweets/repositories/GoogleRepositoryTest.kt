package com.br.marcusmartins.twitteranalyze.features.tweets.repositories

import com.br.marcusmartins.twitteranalyze.UnitTest
import com.br.marcusmartins.twitteranalyze.core.exception.Failure.NetworkConnection
import com.br.marcusmartins.twitteranalyze.core.exception.Failure.ServerError
import com.br.marcusmartins.twitteranalyze.core.functional.Either
import com.br.marcusmartins.twitteranalyze.core.functional.Either.Right
import com.br.marcusmartins.twitteranalyze.core.platform.NetworkHandler
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.*
import com.br.marcusmartins.twitteranalyze.features.tweets.services.GoogleService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import retrofit2.Call
import retrofit2.Response

class GoogleRepositoryTest : UnitTest() {
    private val analyze = "teste"

    private lateinit var networkRepository: GoogleRepository.Network

    @Mock private lateinit var networkHandler: NetworkHandler
    @Mock private lateinit var service: GoogleService

    @Mock private lateinit var googleCall: Call<SentimentResponse>
    @Mock private lateinit var googleResponse: Response<SentimentResponse>

    @Before fun setUp() {
        networkRepository = GoogleRepository.Network(networkHandler, service)
    }

    @Test fun `should return neutral sentiment by default`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { googleResponse.body() }.willReturn(null)
        given { googleResponse.isSuccessful }.willReturn(true)
        given { googleCall.execute() }.willReturn(googleResponse)
        given { service.postAnalysis(any()) }.willReturn(googleCall)

        val sentiment = networkRepository.analyze(analyze)

        sentiment shouldEqual Right(SentimentType.NEUTRAL)
        verify(service).postAnalysis(any())
    }

    @Test fun `should get sentiment positive from service`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { googleResponse.body() }.willReturn(SentimentResponse(Sentiment(score = 0.1)))
        given { googleResponse.isSuccessful }.willReturn(true)
        given { googleCall.execute() }.willReturn(googleResponse)
        given { service.postAnalysis(any()) }.willReturn(googleCall)

        val sentiment = networkRepository.analyze(analyze)

        sentiment shouldEqual Right(SentimentType.POSITIVE)
        verify(service).postAnalysis(any())
    }

    @Test fun `should get sentiment negative from service`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { googleResponse.body() }.willReturn(SentimentResponse(Sentiment(score = -0.1)))
        given { googleResponse.isSuccessful }.willReturn(true)
        given { googleCall.execute() }.willReturn(googleResponse)
        given { service.postAnalysis(any()) }.willReturn(googleCall)

        val sentiment = networkRepository.analyze(analyze)

        sentiment shouldEqual Right(SentimentType.NEGATIVE)
        verify(service).postAnalysis(any())
    }

    @Test fun `sentiment service should return network failure when no connection`() {
        given { networkHandler.isConnected }.willReturn(false)

        val sentiment = networkRepository.analyze(analyze)

        sentiment shouldBeInstanceOf Either::class.java
        sentiment.isLeft shouldEqual true
        sentiment.either({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test fun `sentiment service should return network failure when undefined connection`() {
        given { networkHandler.isConnected }.willReturn(null)

        val sentiment = networkRepository.analyze(analyze)

        sentiment shouldBeInstanceOf Either::class.java
        sentiment.isLeft shouldEqual true
        sentiment.either({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test fun `sentiment service should return server error if no successful response`() {
        given { networkHandler.isConnected }.willReturn(true)

        val sentiment = networkRepository.analyze(analyze)

        sentiment shouldBeInstanceOf Either::class.java
        sentiment.isLeft shouldEqual true
        sentiment.either({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }

    @Test fun `sentiment request should catch exceptions`() {
        given { networkHandler.isConnected }.willReturn(true)

        val sentiment = networkRepository.analyze(analyze)

        sentiment shouldBeInstanceOf Either::class.java
        sentiment.isLeft shouldEqual true
        sentiment.either({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }
}