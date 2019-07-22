package com.br.marcusmartins.twitteranalyze.core.interactor

import com.br.marcusmartins.twitteranalyze.AndroidTest
import com.br.marcusmartins.twitteranalyze.core.exception.Failure
import com.br.marcusmartins.twitteranalyze.core.functional.Either
import com.br.marcusmartins.twitteranalyze.core.functional.Either.Right
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test

class UseCaseTest : AndroidTest() {

    private val TYPE_TEST = "Test"
    private val TYPE_PARAM = "ParamTest"

    private lateinit var useCase: UseCase<MyType, MyParams>

    @Before
    fun setUp() {
        useCase = mock { onBlocking { run(any()) } doReturn Right(MyType(TYPE_TEST)) }
    }

    @Test fun `running use case should return 'Either' of use case type`() {
        val params = MyParams(TYPE_PARAM)
        val result = runBlocking { useCase.run(params) }

        result shouldEqual Right(MyType(TYPE_TEST))
    }

    @Test fun `should return correct data when executing use case`() {
        var result: Either<Failure, MyType>? = null

        val params = MyParams("TestParam")
        val onResult = { myResult: Either<Failure, MyType> -> result = myResult }

        whenever(runBlocking { useCase(params, onResult) })
                .then { result shouldEqual Right(MyType(TYPE_TEST)) }
    }

    data class MyType(val name: String)
    data class MyParams(val name: String)

}
