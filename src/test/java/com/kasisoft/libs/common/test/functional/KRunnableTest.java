package com.kasisoft.libs.common.test.functional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.functional.*;

import com.kasisoft.libs.common.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class KRunnableTest {

    private KRunnable runnable = () -> {
        throw new RuntimeException("error");
    };

    @Test
    public void run() throws Exception {
        assertThrows(RuntimeException.class, runnable::run);
    }

    @Test
    public void protect() {
        assertThrows(KclException.class, runnable.protect()::run);
    }

} /* ENDCLASS */
