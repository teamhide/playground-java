## Bulkhead
### Semaphore
```java
final SemaphoreBulkhead bulkhead = new SemaphoreBulkhead(1, 1_000_000); // 1초
final String result = bulkhead.execute(() -> "success");
```

### Thread pool
```java
final ThreadPoolBulkhead bulkhead = new ThreadPoolBulkhead(2, 1_000_000); // 1s
final String result = bulkhead.execute(() -> "ok");
```
