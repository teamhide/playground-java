package teamhide.playground.hunit5;

import java.util.Objects;

public class Assertions {
    public static <T> Assert<T> assertThat(final T actual) {
        return new Assert<>(actual);
    }

    public static <T extends Throwable> T assertThrows(final Class<T> expectedException, final Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable actual) {
            if (expectedException.isInstance(actual)) {
                return expectedException.cast(actual);
            }
            final String message = String.format("""
                Unexpected exception type thrown:
                Expected: %s
                Actual:   %s
                """, expectedException.getName(), actual.getClass().getName());
            throw new AssertionError(message, actual);
        }
        throw new AssertionError(String.format("""
            Expected exception to be thrown:
            Expected: %s
            But no exception was thrown.
            """, expectedException.getName()));
    }

    public static class Assert<T> {
        private final T actual;

        public Assert(final T actual) {
            this.actual = actual;
        }

        public Assert<T> isEqualTo(final T expected) {
            if (!Objects.equals(actual, expected)) {
                final String message = String.format("""
                    Expected value to be equal:
                    Expected: %s
                    Actual:   %s
                    """, expected, actual);
                throw new AssertionError(message);
            }
            return this;
        }

        public Assert<T> isNotEqualTo(final T expected) {
            if (Objects.equals(actual, expected)) {
                final String message = String.format("""
                    Expected value to be not equal:
                    Both were: %s
                    """, actual);
                throw new AssertionError(message);
            }
            return this;
        }

        public Assert<T> isInstanceOf(final Class<?> expected) {
            if (!expected.isInstance(actual)) {
                final String message = String.format("""
                    Expected instance of:
                    Expected: %s
                    Actual:   %s
                    """, expected.getName(), actual == null ? "null" : actual.getClass().getName());
                throw new AssertionError(message);
            }
            return this;
        }
    }
}
