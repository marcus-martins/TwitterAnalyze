package com.br.marcusmartins.twitteranalyze.features.search

import android.os.Bundle
import android.view.View
import com.br.marcusmartins.twitteranalyze.R
import com.br.marcusmartins.twitteranalyze.core.navigation.Navigator
import com.br.marcusmartins.twitteranalyze.core.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment : BaseFragment() {

    @Inject lateinit var navigator: Navigator

    override fun layoutId() = R.layout.fragment_search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        twitter_user_query.isIconified = false
        twitter_user_query.clearFocus()

        twitter_user_query.setOnSearchClickListener {
            navigator.showTweetsActivity(activity!!, twitter_user_query.query.toString())
        }

        btn_go.setOnClickListener {
            navigator.showTweetsActivity(activity!!, twitter_user_query.query.toString())
        }
    }
}