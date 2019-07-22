package com.br.marcusmartins.twitteranalyze.features.tweets

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import com.br.marcusmartins.twitteranalyze.R

data class SentimentView(
    @StringRes val message: Int = 0,
    @ColorRes val backgroundColor: Int = 0,
    @ColorRes val textColor: Int = R.color.white_color
)