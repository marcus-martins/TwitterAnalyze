package com.br.marcusmartins.twitteranalyze.features.tweets.usecases

import com.br.marcusmartins.twitteranalyze.UnitTest
import com.br.marcusmartins.twitteranalyze.core.functional.Either.Right
import com.br.marcusmartins.twitteranalyze.features.tweets.repositories.TweetsRepository
import com.br.marcusmartins.twitteranalyze.features.tweets.usecases.GetTweets
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class GetTweetsTest : UnitTest() {

    private val accountName = "account"

    private lateinit var getTweets: GetTweets

    @Mock private lateinit var tweetsRepository: TweetsRepository

    @Mock private lateinit var scope: CoroutineScope

    @Mock private lateinit var dispatcher: CoroutineDispatcher

    @Before fun setUp() {
        getTweets = GetTweets(
            tweetsRepository,
            scope,
            dispatcher
        )
        given { tweetsRepository.tweets(accountName) }.willReturn(Right(listOf()))
    }

    @Test fun `should get data from repository`() {
        runBlocking { getTweets.run(GetTweets.Params(accountName)) }

        verify(tweetsRepository).tweets(accountName)
        verifyNoMoreInteractions(tweetsRepository)
    }
}
