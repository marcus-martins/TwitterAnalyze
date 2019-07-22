package com.br.marcusmartins.twitteranalyze.features.tweets

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.br.marcusmartins.twitteranalyze.R
import com.br.marcusmartins.twitteranalyze.core.exception.Failure
import com.br.marcusmartins.twitteranalyze.core.extension.*
import com.br.marcusmartins.twitteranalyze.core.platform.BaseFragment
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.Tweets
import kotlinx.android.synthetic.main.fragment_tweets.*
import javax.inject.Inject

class TweetsFragment : BaseFragment() {
    private var query = ""

    companion object {
        private const val PARAM_ACCOUNT_NAME = "param_account_name"

        fun newInstance(accountName: String): TweetsFragment {
            val tweetsFragment = TweetsFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_ACCOUNT_NAME, accountName)
            tweetsFragment.arguments = arguments

            return tweetsFragment
        }
    }

    @Inject lateinit var tweetsAdapter: TweetsAdapter

    private lateinit var tweetsViewModel: TweetsViewModel

    override fun layoutId() = R.layout.fragment_tweets

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        tweetsViewModel = viewModel(viewModelFactory) {
            observe(tweets, ::renderTweetsList)
            observe(analyze, {
                it?.run {  showSnackbar(message, backgroundColor, textColor) }
                hideProgress()
            })
            failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (firstTimeCreated(savedInstanceState)) {
            query = arguments?.getString(PARAM_ACCOUNT_NAME) ?: ""
            showProgress()
            tweetsViewModel.loadTweets(query)
        }
        initializeView()
    }

    private fun initializeView() {
        tweetsList.layoutManager = LinearLayoutManager(activity)
        tweetsList.adapter = tweetsAdapter
        tweetsAdapter.clickListener = {
            showProgress()
            tweetsViewModel.onTweetClicked(it.text)
        }
    }

    private fun renderTweetsList(tweets: List<Tweets>?) {
        if (!tweets.isNullOrEmpty()) {
            tweetsAdapter.collection = tweets.orEmpty()
            tweetsList.visible()
            emptyView.invisible()
        } else {
            tweetsList.invisible()
            emptyView.visible()
        }

        hideProgress()
    }

    private fun renderFailure(@StringRes message: Int) {
        tweetsList.invisible()
        emptyView.visible()
        hideProgress()
        notifyWithAction(message, R.string.action_refresh) { tweetsViewModel.loadTweets(query) }

    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> { renderFailure(R.string.failure_network_connection); close() }
            is Failure.ServerError -> { renderFailure(R.string.failure_server_error); close() }
        }
    }


}