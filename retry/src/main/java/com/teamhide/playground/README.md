## Retryer
Simple retryer

```java
final RetryConfig config = RetryConfig.custom()
        .maxAttempts(3)
        .waitDuration(Duration.ofMillis(100))
        .ignoreExceptions(IllegalStateException.class, IllegalArgumentException.class)
        .build();

final String sut = Retryer.execute(config, () -> "SUCCESS");
```
