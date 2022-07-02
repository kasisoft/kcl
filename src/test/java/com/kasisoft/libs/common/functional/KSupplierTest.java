package com.kasisoft.libs.common.functional;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.*;

import org.junit.jupiter.api.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class KSupplierTest {

    private KSupplier<String> getName           = () -> "name";

    private KSupplier<String> getNameWithError  = () -> { throw new DummyException("error"); };

    @Test
    public void get() throws Exception {
        assertThat(getName.get(), is("name"));
        assertThrows(DummyException.class, () -> {
           getNameWithError.get();
        });
    }

    @Test
    public void protect() {
        assertThat(getName.protect().get(), is("name"));
        assertThrows(KclException.class, () -> {
            getNameWithError.protect().get();
        });
    }

} /* ENDCLASS */
