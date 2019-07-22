package com.br.marcusmartins.twitteranalyze.core.functional

import com.br.marcusmartins.twitteranalyze.UnitTest
import com.br.marcusmartins.twitteranalyze.core.functional.Either.Left
import com.br.marcusmartins.twitteranalyze.core.functional.Either.Right
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqualTo
import org.junit.Test

class EitherTest : UnitTest() {

    @Test fun `Either Right should return correct type`() {
        val result = Right("userAccount")

        result shouldBeInstanceOf Either::class.java
        result.isRight shouldBe true
        result.isLeft shouldBe false
        result.either({},
                { right ->
                    right shouldBeInstanceOf String::class.java
                    right shouldEqualTo "userAccount"
                })
    }

    @Test fun `Either Left should return correct type`() {
        val result = Left("userAccount")

        result shouldBeInstanceOf Either::class.java
        result.isLeft shouldBe true
        result.isRight shouldBe false
        result.either(
                { left ->
                    left shouldBeInstanceOf String::class.java
                    left shouldEqualTo "userAccount"
                }, {})
    }
}