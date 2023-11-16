package com.kasisoft.libs.common.test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class KclExceptionTest {

    @Test
    public void wrap() {
        assertThrows(KclException.class, () -> {
            try {
                throw new RuntimeException("simple text");
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        });
    }

    @Test
    public void wrap__KclException() {
        assertThrows(KclException.class, () -> {
            try {
                throw new KclException("simple text");
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        });
    }

    @Test
    public void wrap__KclExceptionWithMessage() {
        assertThrows(KclException.class, () -> {
            try {
                throw new KclException("simple text");
            } catch (Exception ex) {
                throw KclException.wrap(ex, "Error Message: %s".formatted(ex.getLocalizedMessage()));
            }
        });
    }

    @Test
    public void unwrap() {
        try {
            wrap();
        } catch (KclException ex) {
            var cause = KclException.unwrap(ex);
            assertNotNull(cause);
            assertTrue(cause instanceof RuntimeException);
            assertThat(cause.getLocalizedMessage(), is("java.lang.RuntimeException: simple text"));
        }
    }

    @Test
    public void unwrap__NotAKclException() {
        try {
            try {
                throw new KclException("simple text");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            var unwrapped = KclException.unwrap(ex);
            assertThat(unwrapped.getLocalizedMessage(), is("simple text"));
        }
    }

    @Test
    public void defaultConstructor() {
        assertThrows(KclException.class, () -> {
            throw new KclException();
        });
    }

    @Test
    public void constructor__Formatting() {
        assertThrows(KclException.class, () -> {
            throw new KclException("Message: Value");
        });
    }

    @Test
    public void constructor__Message() {
        assertThrows(KclException.class, () -> {
            throw new KclException("Message");
        });
    }

} /* ENDCLASS */
