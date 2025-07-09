package com.teamhide.playground.gatekeeper.kotlin

import com.teamhide.playground.gatekeeper.ReactiveLockManager
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Supplier
import kotlin.coroutines.ContinuationInterceptor

class ReactiveLockManagerTest {
    private val lockManager = FakeReactiveLockManager()

    @Test
    fun `dispatcher 파라미터가 null이면 상속받은 부모 dispatcher를 사용한다`() = runTest {
        // Given
        val parentDispatcher = coroutineContext[ContinuationInterceptor]
        val captured = AtomicReference<ContinuationInterceptor>(null)

        // When
        lockManager.executeSuspendWithLock("identifier") {
            captured.set(coroutineContext[ContinuationInterceptor])
        }

        // Then
        assertThat(parentDispatcher).isEqualTo(captured.get())
    }

    @Test
    fun `dispatcher를 지정하면 그 디스패처를 사용한다`() = runTest {
        // Given
        val customDispatcher = StandardTestDispatcher(testScheduler)
        val captured = AtomicReference<ContinuationInterceptor>()

        // When
        lockManager.executeSuspendWithLock("identifier", dispatcher = customDispatcher) {
            captured.set(coroutineContext[ContinuationInterceptor])
        }

        // Then: 같은 TestCoroutineScheduler 를 공유하는지 확인
        val actual = captured.get() as TestDispatcher
        assertThat(actual.scheduler).isSameAs(testScheduler)
    }

    class FakeReactiveLockManager : ReactiveLockManager {
        override fun <T> executeWithLock(
            identifier: String,
            supplier: Supplier<Mono<T>>
        ): Mono<T> = supplier.get()

        override fun <T> executeManyWithLock(
            identifier: String,
            supplier: Supplier<Flux<T>>
        ): Flux<T> = supplier.get()
    }
}
