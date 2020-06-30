package com.kasisoft.libs.common.csv;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertEquals;

import com.kasisoft.libs.common.io.IoFunctions;

import com.kasisoft.libs.common.AbstractTestCase;
import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.utils.MiscFunctions;

import org.testng.annotations.Test;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class CsvTableModelTest extends AbstractTestCase {

  @Test(groups = "all")
  public void loading() {
    
    var source = getResource( "text1.csv" );
    
    var options = CsvOptions.builder()
      .fillMissingColumns()
      .build();
    
    var model = new CsvTableModel(options);
    
    IoFunctions.forInputStreamDo(source, model::load);
    
    assertThat(model.getColumnCount(), is(3));
    assertThat(model.getRowCount(), is(8));
    assertEquals(model.getColumnClass(0), String.class);
    assertEquals(model.getColumnClass(1), String.class);
    assertEquals(model.getColumnClass(2), Short.class);
    
  }

  @Test(groups = "all")
  public void enforceType() {
    
    var source = getResource("text1.csv");
    
    var options = CsvOptions.builder()
      .column(null)
      .column(null)
      // we're enforcing the type Long for this column
      .column(CsvColumn.<Long>builder().type(Long.class).adapter(MiscFunctions::parseLong).defaultValue(0L).nullable().title("longVal").build())
      .fillMissingColumns()
      .build();
    
    var model = new CsvTableModel(options);
    
    IoFunctions.forInputStreamDo(source, model::load);
    
    assertThat(model.getColumnCount(), is(3));
    assertThat(model.getRowCount(), is(8));
    assertEquals(model.getColumnClass(0), String.class);
    assertEquals(model.getColumnClass(1), String.class);
    assertEquals(model.getColumnClass(2), Long.class);
    
  }

  @Test(groups = "all", expectedExceptions = KclException.class)
  public void inconsistentColumnNumbers() {
    
    var source  = getResource("text2.csv");
    var options = CsvOptions.builder().build();
    var model   = new CsvTableModel(options);
    
    IoFunctions.forInputStreamDo(source, model::load);
    
  }

  @Test(groups = "all")
  public void inconsistentColumnNumbersWithErrorHandling() {
    
    var source  = getResource( "text2.csv" );
    var options = CsvOptions.builder().build();
    var model   = new CsvTableModel(options);
    
    model.setErrorHandlerForInconsistentColumnCount($ -> {/* do nothing */});
    
    IoFunctions.forInputStreamDo(source, model::load);
    
    assertThat(model.getColumnCount(), is(3));
    
    // one less row as the row without the right amount of columns must be filtered out
    assertThat(model.getRowCount(), is(7));
    
    assertEquals(model.getColumnClass(0), String.class);
    assertEquals(model.getColumnClass(1), String.class);
    assertEquals(model.getColumnClass(2), Short.class);
    
  }

} /* ENDCLASS */
