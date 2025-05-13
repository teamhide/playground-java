package teamhide.playground.hunit5;

import org.junit.jupiter.api.Test;

import static teamhide.playground.hunit5.Assertions.assertThat;
import static teamhide.playground.hunit5.Assertions.assertThrows;

class AssertionsTest {
    @Test
    void testIsEqualToIsTrue() {
        // Given
        final String actual = "a";
        final String expected = "a";

        // When, Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testIsEqualToIsFalse() {
        // Given
        final String actual = "a";
        final String expected = "b";

        // When, Then
        assertThrows(AssertionError.class, () -> assertThat(actual).isEqualTo(expected));
    }

    @Test
    void testIsNotEqualIsTrue() {
        // Given
        final String actual = "a";
        final String expected = "b";

        // When, Then
        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    void testIsNotEqualIsFalse() {
        // Given
        final String actual = "a";
        final String expected = "a";

        // When, Then
        assertThrows(AssertionError.class, () -> assertThat(actual).isNotEqualTo(expected));
    }
}
