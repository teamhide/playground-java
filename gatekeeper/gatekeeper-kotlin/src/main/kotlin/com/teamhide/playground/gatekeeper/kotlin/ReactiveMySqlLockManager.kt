package com.teamhide.playground.gatekeeper.kotlin

import com.teamhide.playground.gatekeeper.ReactiveLockManager
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono

suspend fun <T> ReactiveLockManager.executeSuspendWithLock(identifier: String, block: suspend () -> T): T {
    return this.executeWithLock(identifier) {
        mono { block() }
    }.awaitSingle()
}
