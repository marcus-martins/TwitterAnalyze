package com.br.marcusmartins.twitteranalyze.features.tweets

import com.br.marcusmartins.twitteranalyze.AndroidTest
import com.br.marcusmartins.twitteranalyze.R
import com.br.marcusmartins.twitteranalyze.core.exception.Failure
import com.br.marcusmartins.twitteranalyze.core.functional.Either
import com.br.marcusmartins.twitteranalyze.core.functional.Either.Right
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.SentimentType
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.Tweets
import com.br.marcusmartins.twitteranalyze.features.tweets.usecases.GetAnalyze
import com.br.marcusmartins.twitteranalyze.features.tweets.usecases.GetTweets
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqualTo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class TweetsViewModelTest : AndroidTest() {
    private val accountName = "name"

    private lateinit var tweetsViewModel: TweetsViewModel

    private val tweetsList = listOf(
        Tweets(
            created_at = "Wed May 01 16:28:04 +0000 2019",
            id = 10L,
            text = "Teste"

        )
    )

    @Mock private lateinit var getTweets: GetTweets

    @Mock private lateinit var getAnalyze: GetAnalyze

    @Before
    fun setUp() {
        whenever(getTweets(any(), any())).thenAnswer { answer ->
            answer.getArgument<(Either<Failure, List<Tweets>>) -> Unit>(1)(Right(tweetsList))
        }
        whenever(getAnalyze(any(), any())).thenAnswer { answer ->
            answer.getArgument<(Either<Failure, SentimentType>) -> Unit>(1)(Right(SentimentType.POSITIVE))
        }
        tweetsViewModel = TweetsViewModel(getTweets, getAnalyze)
    }

    @Test fun `loading tweets list should update live data`() {
        tweetsViewModel.tweets.observeForever {
            with(it!![0]) {
                id shouldEqualTo 10L
                created_at shouldEqualTo "05/01/2019 13:28"
                text shouldEqualTo "Teste"
            }
        }

        runBlocking { tweetsViewModel.loadTweets(accountName) }
    }

    @Test fun `loading sentiment should update live data`() {
        tweetsViewModel.analyze.observeForever {
            it?.run {
                message shouldEqualTo  R.string.positive_toast
                backgroundColor shouldEqualTo R.color.positive_color
                textColor shouldEqualTo R.color.white_color
            }
        }

        runBlocking { tweetsViewModel.onTweetClicked(accountName) }
    }
}