package com.br.marcusmartins.twitteranalyze.features.tweets

import android.content.Context
import android.content.Intent
import com.br.marcusmartins.twitteranalyze.core.platform.BaseActivity

class TweetsActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_USER_ACCOUNT = "br.com.marcusmartins.USER_ACCOUNT"

        fun callingIntent(context: Context, accountName: String): Intent {
            val intent = Intent(context, TweetsActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_USER_ACCOUNT, accountName)
            return intent
        }
    }

    override fun fragment() = TweetsFragment.newInstance(intent.getStringExtra(INTENT_EXTRA_PARAM_USER_ACCOUNT))
}
