## Rule engine

```java
final UserAgeRule userAgeRule = new UserAgeRule(1);
final Runnable onSuccess = mock(Runnable.class);
final Runnable onFailure = mock(Runnable.class);
final Rule ruleWithCallback = new Callback(userAgeRule)
        .onSuccess(onSuccess)
        .onFailure(onFailure);

ruleWithCallback.evaluate();
```
