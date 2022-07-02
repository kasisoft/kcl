package com.kasisoft.libs.common.functional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

import com.kasisoft.libs.common.*;
import org.junit.jupiter.api.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class KBiFunctionTest {

    private KBiFunction<String, String, String> function            = ($a, $b) -> $a;

    private KBiFunction<String, String, String> functionWithError   = ($a, $b) -> { throw new DummyException("error"); };

    @Test
    public void apply() throws Exception {
        assertThat(function.apply("dodo", null), is("dodo"));
        assertThrows(DummyException.class, () -> {
            functionWithError.apply(null, null);
        });
    }

    @Test
    public void protect() {
        assertThat(function.protect().apply("dodo", null), is("dodo"));
        assertThrows(KclException.class, () -> {
            functionWithError.protect().apply(null, null);
        });
    }

} /* ENDCLASS */
