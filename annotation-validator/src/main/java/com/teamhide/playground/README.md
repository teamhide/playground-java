## Annotation validator
Similar to spring-validation

```java
public class RequestDto {
    @NotNull
    private final String id;

    @NotBlank
    private final String name;

    public RequestDto(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
}

final RequestDto requestDto = new RequestDto("id", "name");
Validator.validate(requestDto);
```
