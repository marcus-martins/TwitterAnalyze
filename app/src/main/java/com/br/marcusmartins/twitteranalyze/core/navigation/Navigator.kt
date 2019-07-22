package com.br.marcusmartins.twitteranalyze.core.navigation

import android.content.Context
import com.br.marcusmartins.twitteranalyze.features.search.SearchActivity
import com.br.marcusmartins.twitteranalyze.features.tweets.TweetsActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator
@Inject constructor() {
    fun showMain(context: Context) {
        showSearch(context)
    }

    private fun showSearch(context: Context) = context.startActivity(SearchActivity.callingIntent(context))

    fun showTweetsActivity(context: Context, accountName: String) {
        val intent = TweetsActivity.callingIntent(context, accountName)
        context.startActivity(intent)
    }
}


