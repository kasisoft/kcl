package com.kasisoft.libs.common.functional;

import static org.junit.jupiter.api.Assertions.*;

import com.kasisoft.libs.common.*;
import org.junit.jupiter.api.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class KBiConsumerTest {

    private KBiConsumer<String, String> consumer            = ($a, $b) -> {};

    private KBiConsumer<String, String> consumerWithError   = ($a, $b) -> { throw new DummyException("error"); };

    @Test
    public void accept() throws Exception {
        consumer.accept(null, null);
        assertThrows(DummyException.class, () -> {
            consumerWithError.accept(null, null);
        });
    }

    @Test
    public void protect() {
        consumer.protect().accept(null, null);
        assertThrows(KclException.class, () -> {
            consumerWithError.protect().accept(null, null);
        });
    }

} /* ENDCLASS */
