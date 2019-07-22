package com.br.marcusmartins.twitteranalyze.core.platform

import android.arch.lifecycle.MutableLiveData
import com.br.marcusmartins.twitteranalyze.AndroidTest
import com.br.marcusmartins.twitteranalyze.core.exception.Failure
import com.br.marcusmartins.twitteranalyze.core.exception.Failure.NetworkConnection
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Test

class BaseViewModelTest : AndroidTest() {

    @Test fun `should handle failure by updating live data`() {
        val viewModel = MyViewModel()

        viewModel.handleError(NetworkConnection)

        val failure = viewModel.failure
        val error = viewModel.failure.value

        failure shouldBeInstanceOf MutableLiveData::class.java
        error shouldBeInstanceOf NetworkConnection::class.java
    }

    private class MyViewModel : BaseViewModel() {
        override fun cancelRequest() {}

        fun handleError(failure: Failure) = handleFailure(failure)
    }
}