package com.teamhide.playground.gatekeeper.kotlin

import com.teamhide.playground.gatekeeper.ReactiveLockManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext

suspend fun <T> ReactiveLockManager.executeSuspendWithLock(
    identifier: String,
    dispatcher: CoroutineDispatcher? = null,
    block: suspend () -> T,
): T {
    val reactorCtx: CoroutineContext = when {
        dispatcher != null -> dispatcher
        else -> coroutineContext[ContinuationInterceptor]
            ?: EmptyCoroutineContext
    }

    return executeWithLock(identifier) {
        if (reactorCtx == EmptyCoroutineContext) {
            mono { block() }
        } else {
            mono(reactorCtx) { block() }
        }
    }.awaitSingle()
}
