package com.kasisoft.libs.common.old.csv;

import static com.kasisoft.libs.common.old.io.DefaultIO.URL_INPUTSTREAM_EX;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertEquals;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.old.util.MiscFunctions;

import org.testng.annotations.Test;

import org.testng.Assert;

import java.net.URL;

/**
 * Tests for 'CsvTableModel'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class CsvTableModelTest {

  @Test(groups = "all")
  public void loading() {
    URL source = getClass().getClassLoader().getResource( "old/csv/text1.csv" );
    Assert.assertNotNull( source );
    
    CsvOptions    options = CsvOptions.builder()
        .fillMissingColumns()
        .build();
    
    CsvTableModel model   = new CsvTableModel( options );
    
    URL_INPUTSTREAM_EX.forInputStreamDo( source, model::load );
    
    assertThat( model.getColumnCount(), is(3) );
    assertThat( model.getRowCount(), is(8) );
    assertEquals( model.getColumnClass(0), String.class );
    assertEquals( model.getColumnClass(1), String.class );
    assertEquals( model.getColumnClass(2), Short.class );
    
  }

  @Test(groups = "all")
  public void enforceType() {
    
    URL source = getClass().getClassLoader().getResource( "old/csv/text1.csv" );
    Assert.assertNotNull( source );
    
    CsvOptions    options = CsvOptions.builder()
        .column( null )
        .column( null )
        // we're enforcing the type Long for this column
        .column( CsvColumn.<Long>builder().type( Long.class ).adapter( MiscFunctions::parseLong ).defaultValue( 0L ).nullable().title( "longVal" ).build() )
        .fillMissingColumns()
        .build();
    CsvTableModel model   = new CsvTableModel( options );
    
    URL_INPUTSTREAM_EX.forInputStreamDo( source, model::load );
    
    assertThat( model.getColumnCount(), is(3) );
    assertThat( model.getRowCount(), is(8) );
    assertEquals( model.getColumnClass(0), String.class );
    assertEquals( model.getColumnClass(1), String.class );
    assertEquals( model.getColumnClass(2), Long.class );
    
  }

  @Test(groups = "all", expectedExceptions = KclException.class)
  public void inconsistentColumnNumbers() {
    URL source = getClass().getClassLoader().getResource( "old/csv/text2.csv" );
    Assert.assertNotNull( source );
    
    CsvOptions    options = CsvOptions.builder()
        .build();
    CsvTableModel model   = new CsvTableModel( options );
    
    URL_INPUTSTREAM_EX.forInputStreamDo( source, model::load );
    
    assertThat( model.getColumnCount(), is(3) );
    assertThat( model.getRowCount(), is(8) );
    assertEquals( model.getColumnClass(0), String.class );
    assertEquals( model.getColumnClass(1), String.class );
    assertEquals( model.getColumnClass(2), Short.class );
    
  }

  @Test(groups = "all")
  public void inconsistentColumnNumbersWithErrorHandling() {
    URL source = getClass().getClassLoader().getResource( "old/csv/text2.csv" );
    Assert.assertNotNull( source );
    
    CsvOptions    options = CsvOptions.builder()
        .build();
    CsvTableModel model   = new CsvTableModel( options );
    model.setErrorHandlerForInconsistentColumnCount( $ -> { /* do nothing */ } );
    
    URL_INPUTSTREAM_EX.forInputStreamDo( source, model::load );
    
    assertThat( model.getColumnCount(), is(3) );
    
    // one less row as the row without the right amount of columns must be filtered out
    assertThat( model.getRowCount(), is(7) );
    
    assertEquals( model.getColumnClass(0), String.class );
    assertEquals( model.getColumnClass(1), String.class );
    assertEquals( model.getColumnClass(2), Short.class );
    
  }

} /* ENDCLASS */
