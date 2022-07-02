package com.kasisoft.libs.common.functional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import com.kasisoft.libs.common.*;

import org.junit.jupiter.api.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class KFunctionTest {

    private KFunction<String, String> function            = $ -> $;

    private KFunction<String, String> functionWithError   = $ -> { throw new DummyException("error"); };

    @Test
    public void apply() throws Exception {
        assertThat(function.apply("dodo"), is("dodo"));
        assertThrows(DummyException.class, () -> {
            functionWithError.apply(null);
        });
    }

    @Test
    public void protect() {
        assertThat(function.protect().apply("dodo"), is("dodo"));
        assertThrows(KclException.class, () -> {
            functionWithError.protect().apply(null);
        });
    }

} /* ENDCLASS */
