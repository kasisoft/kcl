package com.kasisoft.libs.common.functional;

import static org.junit.jupiter.api.Assertions.*;

import com.kasisoft.libs.common.*;

import org.junit.jupiter.api.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class KRunnableTest {

    private KRunnable runnable = () -> { throw new DummyException("error"); };

    @Test
    public void run() throws Exception {
        assertThrows(DummyException.class, () -> {
            runnable.run();
        });
    }

    @Test
    public void protect() {
        assertThrows(KclException.class, () -> {
            runnable.protect().run();
        });
    }

} /* ENDCLASS */
