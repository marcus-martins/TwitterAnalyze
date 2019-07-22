package com.br.marcusmartins.twitteranalyze.core.platform

import android.content.Context
import javax.inject.Inject

class DataStore
@Inject constructor(private val context: Context) {

    private val tokenPreferenceName = "tokenPreferenceName"
    private val twitterAuthorizeToken = "twitterAuthorizeToken"

    private val preferences = context.getSharedPreferences(tokenPreferenceName, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        preferences.edit()
            .putString(twitterAuthorizeToken, token)
            .apply()
    }

    fun getToken(): String? {
        return preferences.getString(twitterAuthorizeToken, "")
    }
}