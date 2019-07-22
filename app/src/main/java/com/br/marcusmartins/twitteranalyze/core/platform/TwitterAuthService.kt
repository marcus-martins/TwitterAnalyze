package com.br.marcusmartins.twitteranalyze.core.platform

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface TwitterAuthService {
    companion object {
        private const val TWITTER_TOKEN = "oauth2/token"
        private const val TWITTER_AUTHORIZATION = "Authorization"
        private const val TWITTER_GRANT_TYPE = "grant_type"
        private const val TWITTER_CREDENTIALS = "client_credentials"
    }

    @POST(TWITTER_TOKEN)
    @FormUrlEncoded
    fun getToken(@Header(TWITTER_AUTHORIZATION) authorization: String,
                 @Field(TWITTER_GRANT_TYPE) grantType: String = TWITTER_CREDENTIALS
    ): Call<TwitterTokenResponse>
}