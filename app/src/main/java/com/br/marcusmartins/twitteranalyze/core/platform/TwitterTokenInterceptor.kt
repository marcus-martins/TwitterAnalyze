package com.br.marcusmartins.twitteranalyze.core.platform

import android.content.Context
import android.util.Base64
import com.br.marcusmartins.twitteranalyze.R
import okhttp3.*
import javax.inject.Inject

class TwitterTokenInterceptor
@Inject constructor(
    private val context: Context,
    private val dataStore: DataStore,
    private val twitterAuthService: TwitterAuthService
) : Interceptor {
    private val apiKey = context.getString(R.string.twitter_api_key)

    override fun intercept(chain: Interceptor.Chain): Response {
        val initialRequest = chain.request()
        var modifiedRequest = chain.request()
        if (hasToken()) {
            modifiedRequest = initialRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        }

        val response = chain.proceed(modifiedRequest)
        val unauthorized = response.code() == 401 || response.code() == 400
        if (unauthorized) {
            clearToken()
            val newToken = refreshToken()
            modifiedRequest = initialRequest.newBuilder()
                .addHeader("Authorization", "Bearer $newToken")
                .build()
            return chain.proceed(modifiedRequest)
        }
        return response
    }

    private val token: String
        get() = dataStore.getToken().orEmpty()

    private fun hasToken() = token.isNotEmpty()

    private fun clearToken() {
        dataStore.saveToken("")
    }

    private fun refreshToken(): String {
        dataStore.saveToken(
            twitterAuthService
                .getToken("Basic ${Base64.encodeToString(apiKey.toByteArray(), Base64.NO_WRAP)}")
                .execute()
                .body()?.access_token.orEmpty()
        )

        return token
    }
}