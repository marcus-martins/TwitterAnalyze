package com.br.marcusmartins.twitteranalyze.features.search

import android.content.Context
import android.content.Intent
import com.br.marcusmartins.twitteranalyze.core.platform.BaseActivity

class SearchActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, SearchActivity::class.java)
    }

    override fun fragment() = SearchFragment()
}