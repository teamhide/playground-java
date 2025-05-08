package com.teamhide.playground.fallbackcache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FallbackCacheTest {
    private CacheManager<String> firstCacheManager = new LocalCacheManager<>();
    private CacheManager<String> secondCacheManager = new RedisCacheManager<>();
    private FallbackCache<String> fallbackCache = new FallbackCache<>(firstCacheManager, secondCacheManager);

    @BeforeEach
    void setUp() {
        firstCacheManager.clear();
        secondCacheManager.clear();
    }

    @Test
    @DisplayName("1차 캐시와 2차 캐시에 모두 데이터가 없을 때 조회하면 fallback 메소드의 리턴값이 반환되고 1/2차 캐시에 데이터가 저장된다")
    void testGet_1() {
        // Given
        final String key = "key";
        final String fallbackValue = "data";
        final Supplier<String> fallback = () -> fallbackValue;
        assertThat(firstCacheManager.get(key)).isNull();
        assertThat(secondCacheManager.get(key)).isNull();

        // When
        final String sut = fallbackCache.get(key, fallback);

        // Then
        assertThat(sut).isEqualTo("data");
        assertThat(firstCacheManager.get(key)).isEqualTo(fallbackValue);
        assertThat(secondCacheManager.get(key)).isEqualTo(fallbackValue);
    }

    @Test
    @DisplayName("2차 캐시에만 데이터가 존재하면 1차 캐시에도 데이터를 저장한다")
    void testGet_2() {
        // Given
        final String key = "key";
        final String value = "data";
        @SuppressWarnings("unchecked")
        final Supplier<String> fallback = mock(Supplier.class);
        secondCacheManager.put(key, value);
        assertThat(firstCacheManager.get(key)).isNull();
        assertThat(secondCacheManager.get(key)).isNotNull();

        // When
        final String sut = fallbackCache.get(key, fallback);

        // Then
        assertThat(sut).isEqualTo("data");
        assertThat(firstCacheManager.get(key)).isEqualTo(value);
        verify(fallback, times(0)).get();
    }

    @Test
    @DisplayName("1차 캐시에 데이터가 존재하면 해당 값을 반환한다")
    void testGet_3() {
        // Given
        final String key = "key";
        final String value = "data";
        @SuppressWarnings("unchecked")
        final Supplier<String> fallback = mock(Supplier.class);
        firstCacheManager.put(key, value);
        secondCacheManager.put(key, value);
        assertThat(firstCacheManager.get(key)).isNotNull();
        assertThat(secondCacheManager.get(key)).isNotNull();

        // When
        final String sut = fallbackCache.get(key, fallback);

        // Then
        assertThat(sut).isEqualTo("data");
        assertThat(firstCacheManager.get(key)).isEqualTo(value);
        assertThat(secondCacheManager.get(key)).isEqualTo(value);
        verify(fallback, times(0)).get();
    }

    @Test
    @DisplayName("1차 캐시와 2차 캐시에 모두 데이터가 없을 때 fallback 메소드의 리턴값이 null이라면 1/2차 캐시에 데이터를 저장하지 않는다")
    void testGet_4() {
        // Given
        final String key = "key";
        final Supplier<String> fallback = () -> null;
        assertThat(firstCacheManager.get(key)).isNull();
        assertThat(secondCacheManager.get(key)).isNull();

        // When
        final String sut = fallbackCache.get(key, fallback);

        // Then
        assertThat(sut).isNull();
        assertThat(firstCacheManager.get(key)).isNull();
        assertThat(secondCacheManager.get(key)).isNull();
    }
}
