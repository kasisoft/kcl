package com.kasisoft.libs.common.test.csv;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.utils.*;

import com.kasisoft.libs.common.test.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;
import com.kasisoft.libs.common.converters.*;
import com.kasisoft.libs.common.csv.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class CsvTableModelTest {

    private static final TestResources TEST_RESOURCES = TestResources.createTestResources(CsvTableModelTest.class);

    @Test
    public void loading() {

        var source  = TEST_RESOURCES.getResource("text1.csv");

        var options = CsvOptions.builder().fillMissingColumns().build();

        var model   = new CsvTableModel(options);

        IoSupportFunctions.forInputStreamDo(source, model::load);

        assertThat(model.getColumnCount(), is(3));
        assertThat(model.getRowCount(), is(8));
        assertThat(model.getColumnClass(0).getName(), is(String.class.getName()));
        assertThat(model.getColumnClass(1).getName(), is(String.class.getName()));
        assertThat(model.getColumnClass(2).getName(), is(Short.class.getName()));

    }

    @Test
    public void enforceType() {

        var source  = TEST_RESOURCES.getResource("text1.csv");

        var options = CsvOptions.builder()
            .column(null)
            .column(null)
            // we're enforcing the type Long for this column
            .column(CsvColumn.<Long> builder().type(Long.class).adapter(TypeConverters::convertStringToLong).defaultValue(0L).nullable().title("longVal").build())
            .fillMissingColumns()
            .build();

        var model   = new CsvTableModel(options);

        IoSupportFunctions.forInputStreamDo(source, model::load);

        assertThat(model.getColumnCount(), is(3));
        assertThat(model.getRowCount(), is(8));
        assertEquals(model.getColumnClass(0), String.class);
        assertEquals(model.getColumnClass(1), String.class);
        assertEquals(model.getColumnClass(2), Long.class);

    }

    @Test
    public void inconsistentColumnNumbers() {
        assertThrows(KclException.class, () -> {
            var source  = TEST_RESOURCES.getResource("text2.csv");
            var options = CsvOptions.builder().build();
            var model   = new CsvTableModel(options);
            IoSupportFunctions.forInputStreamDo(source, model::load);
        });
    }

    @Test
    public void inconsistentColumnNumbersWithErrorHandling() {

        var source  = TEST_RESOURCES.getResource("text2.csv");
        var options = CsvOptions.builder().build();
        var model   = new CsvTableModel(options);

        model.setErrorHandlerForInconsistentColumnCount($ -> {/* do nothing */});

        IoSupportFunctions.forInputStreamDo(source, model::load);

        assertThat(model.getColumnCount(), is(3));

        // one less row as the row without the right amount of columns must be filtered out
        assertThat(model.getRowCount(), is(7));

        assertEquals(model.getColumnClass(0), String.class);
        assertEquals(model.getColumnClass(1), String.class);
        assertEquals(model.getColumnClass(2), Short.class);

    }

} /* ENDCLASS */
