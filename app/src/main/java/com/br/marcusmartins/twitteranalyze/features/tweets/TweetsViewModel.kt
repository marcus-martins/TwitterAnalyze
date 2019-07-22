package com.br.marcusmartins.twitteranalyze.features.tweets

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.br.marcusmartins.twitteranalyze.R
import com.br.marcusmartins.twitteranalyze.core.extension.formatDate
import com.br.marcusmartins.twitteranalyze.core.platform.BaseViewModel
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.Tweets
import com.br.marcusmartins.twitteranalyze.features.tweets.usecases.GetTweets.Params
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.SentimentType
import com.br.marcusmartins.twitteranalyze.features.tweets.usecases.GetAnalyze
import com.br.marcusmartins.twitteranalyze.features.tweets.usecases.GetTweets
import javax.inject.Inject

class TweetsViewModel
@Inject constructor(
    private val getTweets: GetTweets,
    private val getAnalyze: GetAnalyze
) : BaseViewModel() {

    private val internalTweets: MutableLiveData<List<Tweets>> = MutableLiveData()
    private val internalAnalyze: MutableLiveData<SentimentView> = MutableLiveData()

    val tweets: LiveData<List<Tweets>>
        get() = internalTweets

    val analyze: LiveData<SentimentView>
        get() = internalAnalyze

    fun loadTweets(accountName: String) = getTweets(Params(accountName)) { it.either(::handleFailure, ::handleTweetsList) }

    fun onTweetClicked(text: String) = getAnalyze(GetAnalyze.Params(text)) { it.either(::handleFailure, ::handleAnalyzeSentiment) }

    private fun handleTweetsList(tweets: List<Tweets>) {
        internalTweets.value = tweets.map {
            Tweets(
                it.created_at.formatDate(),
                it.id,
                it.text
            )
        }
    }

    private fun handleAnalyzeSentiment(sentimentType: SentimentType) {
        internalAnalyze.value = when(sentimentType) {
            SentimentType.NEUTRAL -> SentimentView(R.string.neutral_toast, R.color.neutral_color)
            SentimentType.POSITIVE -> SentimentView(R.string.positive_toast, R.color.positive_color)
            SentimentType.NEGATIVE -> SentimentView(R.string.negative_toast, R.color.negative_color)
        }
    }

    override fun cancelRequest() {
        getAnalyze.cancel()
        getTweets.cancel()
    }
}