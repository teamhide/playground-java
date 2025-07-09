package com.teamhide.playground.gatekeeper.kotlin

import com.teamhide.playground.gatekeeper.DistributedLockException
import com.teamhide.playground.gatekeeper.LockRegistry
import com.teamhide.playground.gatekeeper.ReactiveIntegrationTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Component
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestPropertySource
import java.util.concurrent.CountDownLatch

@ReactiveIntegrationTest
@TestPropertySource(
    properties = [
        "lock.instances.count.provider=redis-reactive",
        "lock.instances.count.key=count",
        "lock.instances.count.wait-time=0",
        "lock.instances.count.lease-time=1000",
        "lock.instances.point.provider=mysql-reactive",
        "lock.instances.point.key=point",
        "lock.instances.point.wait-time=1000",
        "lock.instances.point.lease-time=0"
    ]
)
@Import(ReactiveLockConcurrencyTest.TestService::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ReactiveLockConcurrencyTest(
    private val testService: TestService
) {
    @Test
    fun `Redis락 - 함수형 락을 사용할 때 동시에 suspend 블록을 실행하면 하나의 요청만 성공하고 나머지는 실패한다`() {
        // Given
        val concurrency = 10
        val latch = CountDownLatch(1)

        // When
        val results = runBlocking {
            val jobs = (0 until concurrency).map {
                async(Dispatchers.IO) {
                    latch.await()
                    try {
                        testService.runWithFunctionalLock(
                            identifier = "identifier",
                            lockName = REDIS_LOCK_NAME,
                            action = suspend {
                                delay(500)
                                true
                            }
                        )
                    } catch (_: DistributedLockException) {
                        false
                    }
                }
            }
            latch.countDown()
            jobs.awaitAll()
        }

        // Then
        val successCount = results.count { it }
        val failureCount = results.count { !it }

        assertThat(successCount).isEqualTo(1)
        assertThat(failureCount).isEqualTo(concurrency - 1)
    }

    @Test
    fun `MySql락 - 함수형 락을 사용할 때 동시에 suspend 블록을 실행하면 하나의 요청만 성공하고 나머지는 실패한다`() {
        // Given
        val concurrency = 10
        val latch = CountDownLatch(1)

        // When
        val results = runBlocking {
            val jobs = (0 until concurrency).map {
                async(Dispatchers.IO) {
                    latch.await()
                    try {
                        testService.runWithFunctionalLock(
                            identifier = "identifier",
                            lockName = MYSQL_LOCK_NAME,
                            action = suspend {
                                delay(3000)
                                true
                            }
                        )
                    } catch (_: DistributedLockException) {
                        false
                    }
                }
            }
            latch.countDown()
            jobs.awaitAll()
        }

        // Then
        val successCount = results.count { it }
        val failureCount = results.count { !it }

        assertThat(successCount).isEqualTo(1)
        assertThat(failureCount).isEqualTo(concurrency - 1)
    }

    @Component
    class TestService(
        private val lockRegistry: LockRegistry
    ) {
        suspend fun runWithFunctionalLock(identifier: String, lockName: String, action: suspend () -> Boolean): Boolean {
            val lockManager = lockRegistry.reactiveLockManager(lockName)
            return lockManager.executeSuspendWithLock(identifier) {
                action()
            }
        }
    }

    companion object {
        const val REDIS_LOCK_NAME = "count"
        const val MYSQL_LOCK_NAME = "point"
    }
}
