package com.br.marcusmartins.twitteranalyze

import org.junit.rules.TestRule
import org.mockito.MockitoAnnotations

class InjectMocksRule {

    companion object {
        fun create(testClass: Any) = TestRule { statement, _ ->
            MockitoAnnotations.initMocks(testClass)
            statement
        }
    }
}
