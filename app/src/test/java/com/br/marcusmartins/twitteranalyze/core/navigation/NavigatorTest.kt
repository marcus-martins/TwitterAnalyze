package com.br.marcusmartins.twitteranalyze.core.navigation

import com.br.marcusmartins.twitteranalyze.AndroidTest
import com.br.marcusmartins.twitteranalyze.features.search.SearchActivity
import com.br.marcusmartins.twitteranalyze.features.tweets.TweetsActivity
import com.br.marcusmartins.twitteranalyze.shouldNavigateTo
import org.junit.Before
import org.junit.Test


class NavigatorTest : AndroidTest() {

    private lateinit var navigator: Navigator

    @Before fun setup() {
        navigator = Navigator()
    }

    @Test fun `should forward user to search screen`() {
        navigator.showMain(activityContext())
        RouteActivity::class shouldNavigateTo SearchActivity::class
    }

    @Test fun `should forward user to movies screen`() {
        navigator.showTweetsActivity(activityContext(), "accountName")
        RouteActivity::class shouldNavigateTo TweetsActivity::class
    }
}
