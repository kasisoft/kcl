package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.DummyException;
import com.kasisoft.libs.common.KclException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class KTriFunctionTest {

    private KTriFunction<String, String, String, String> function            = ($a, $b, $c) -> $a;

    private KTriFunction<String, String, String, String> functionWithError   = ($a, $b, $c) -> { throw new DummyException("error"); };

    @Test
    public void apply() throws Exception {
        assertThat(function.apply("dodo", null, null), is("dodo"));
        assertThrows(DummyException.class, () -> {
            functionWithError.apply(null, null, null);
        });
    }

    @Test
    public void protect() {
        assertThat(function.protect().apply("dodo", null, null), is("dodo"));
        assertThrows(KclException.class, () -> {
            functionWithError.protect().apply(null, null, null);
        });
    }

} /* ENDCLASS */
