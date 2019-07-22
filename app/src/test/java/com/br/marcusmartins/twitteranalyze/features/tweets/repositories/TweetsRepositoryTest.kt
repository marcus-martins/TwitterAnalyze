package com.br.marcusmartins.twitteranalyze.features.tweets.repositories

import com.br.marcusmartins.twitteranalyze.UnitTest
import com.br.marcusmartins.twitteranalyze.core.exception.Failure.NetworkConnection
import com.br.marcusmartins.twitteranalyze.core.exception.Failure.ServerError
import com.br.marcusmartins.twitteranalyze.core.functional.Either
import com.br.marcusmartins.twitteranalyze.core.functional.Either.Right
import com.br.marcusmartins.twitteranalyze.core.platform.NetworkHandler
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.Tweets
import com.br.marcusmartins.twitteranalyze.features.tweets.services.TweetsService
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

class TweetsRepositoryTest : UnitTest() {
    private val accountName = "account"

    private lateinit var networkRepository: TweetsRepository.Network

    @Mock private lateinit var networkHandler: NetworkHandler
    @Mock private lateinit var service: TweetsService

    @Mock private lateinit var tweetsCall: Call<List<Tweets>>
    @Mock private lateinit var tweetsResponse: Response<List<Tweets>>

    @Before fun setUp() {
        networkRepository = TweetsRepository.Network(networkHandler, service)
    }

    @Test fun `should return empty list by default`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { tweetsResponse.body() }.willReturn(null)
        given { tweetsResponse.isSuccessful }.willReturn(true)
        given { tweetsCall.execute() }.willReturn(tweetsResponse)
        given { service.getTweets(accountName) }.willReturn(tweetsCall)

        val tweets = networkRepository.tweets(accountName)

        tweets shouldEqual Right(emptyList<Tweets>())
        verify(service).getTweets(accountName)
    }

    @Test fun `should get tweets list from service`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { tweetsResponse.body() }.willReturn(listOf(Tweets(text = "message")))
        given { tweetsResponse.isSuccessful }.willReturn(true)
        given { tweetsCall.execute() }.willReturn(tweetsResponse)
        given { service.getTweets(accountName) }.willReturn(tweetsCall)

        val tweets = networkRepository.tweets(accountName)

        tweets shouldEqual Right(listOf(Tweets(text = "message")))
        verify(service).getTweets(accountName)
    }

    @Test fun `tweets service should return network failure when no connection`() {
        given { networkHandler.isConnected }.willReturn(false)

        val tweets = networkRepository.tweets(accountName)

        tweets shouldBeInstanceOf Either::class.java
        tweets.isLeft shouldEqual true
        tweets.either({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test fun `tweets service should return network failure when undefined connection`() {
        given { networkHandler.isConnected }.willReturn(null)

        val tweets = networkRepository.tweets(accountName)

        tweets shouldBeInstanceOf Either::class.java
        tweets.isLeft shouldEqual true
        tweets.either({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test fun `tweets service should return server error if no successful response`() {
        given { networkHandler.isConnected }.willReturn(true)

        val tweets = networkRepository.tweets(accountName)

        tweets shouldBeInstanceOf Either::class.java
        tweets.isLeft shouldEqual true
        tweets.either({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }

    @Test fun `tweets request should catch exceptions`() {
        given { networkHandler.isConnected }.willReturn(true)

        val tweets = networkRepository.tweets(accountName)

        tweets shouldBeInstanceOf Either::class.java
        tweets.isLeft shouldEqual true
        tweets.either({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }
}