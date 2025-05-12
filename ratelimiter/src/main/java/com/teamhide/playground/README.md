## Rate limiter
### Leaky bucket
```java
final int capacity = 0;
final int leakRateSeconds = 1;
final LeakyBucketRateLimiter<Void> rateLimiter = new LeakyBucketRateLimiter<>(capacity, leakRateSeconds);

rateLimiter.execute(this::run);
```

### Token bucket
```java
final int capacity = 10;
final int refillRateSeconds = 10;
final TokenBucketRateLimiter<Void> rateLimiter = new TokenBucketRateLimiter<>(capacity, refillRateSeconds);

rateLimiter.execute(this::run);
```
