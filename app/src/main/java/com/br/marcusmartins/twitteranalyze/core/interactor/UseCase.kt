package com.br.marcusmartins.twitteranalyze.core.interactor

import com.br.marcusmartins.twitteranalyze.core.exception.Failure
import com.br.marcusmartins.twitteranalyze.core.functional.Either
import kotlinx.coroutines.*

abstract class UseCase<out Type, in Params>(private val scope: CoroutineScope,
                                            private val dispatcher: CoroutineDispatcher)
        where Type : Any {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}) {
        scope.launch {
            val result = run(params)

            withContext(dispatcher) {
                onResult(result)
            }
        }
    }

    fun cancel() {
        dispatcher.cancel()
        scope.cancel()
    }

    class None
}
