package com.kasisoft.libs.common.functional;

import static org.junit.jupiter.api.Assertions.*;

import com.kasisoft.libs.common.*;

import org.junit.jupiter.api.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class KConsumerTest {

    private KConsumer<String> consumer          = $ -> {};

    private KConsumer<String> consumerWithError = $ -> { throw new DummyException("error"); };

    @Test
    public void accept() throws Exception {
        consumer.accept(null);
        assertThrows(DummyException.class, () -> {
            consumerWithError.accept(null);
        });
    }

    @Test
    public void protect() {
        consumer.protect().accept(null);
        assertThrows(KclException.class, () -> {
            consumerWithError.protect().accept(null);
        });
    }

} /* ENDCLASS */
