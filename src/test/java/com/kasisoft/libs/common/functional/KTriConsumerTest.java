package com.kasisoft.libs.common.functional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class KTriConsumerTest {

    private KTriConsumer<String, String, String> consumer            = ($a, $b, $c) -> {};

    private KTriConsumer<String, String, String> consumerWithError   = ($a, $b, $c) -> { throw new DummyException("error"); };

    @Test
    public void accept() throws Exception {
        consumer.accept(null, null, null);
        assertThrows(DummyException.class, () -> {
            consumerWithError.accept(null, null, null);
        });
    }

    @Test
    public void protect() {
        consumer.protect().accept(null, null,  null);
        assertThrows(KclException.class, () -> {
            consumerWithError.protect().accept(null, null, null);
        });
    }

} /* ENDCLASS */
