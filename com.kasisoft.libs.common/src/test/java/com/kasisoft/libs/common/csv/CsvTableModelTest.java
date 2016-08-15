package com.kasisoft.libs.common.csv;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

import org.testng.*;

import com.kasisoft.libs.common.util.*;

import com.kasisoft.libs.common.base.*;

import com.kasisoft.libs.common.io.*;

import java.net.*;

/**
 * Tests for 'CsvTableModel'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class CsvTableModelTest {

  @Test(groups = "all")
  public void loading() {
    URL source = getClass().getClassLoader().getResource( "csv/text1.csv" );
    Assert.assertNotNull( source );
    
    CsvTableModel model   = new CsvTableModel();
    CsvOptions    options = CsvOptions.builder()
        .fillMissingColumns()
        .build();
    
    IoFunctions.forInputStreamDo( source, options, model::load );
    
    assertThat( model.getColumnCount(), is(3) );
    assertThat( model.getRowCount(), is(8) );
    assertEquals( model.getColumnClass(0), String.class );
    assertEquals( model.getColumnClass(1), String.class );
    assertEquals( model.getColumnClass(2), Short.class );
    
  }

  @Test(groups = "all")
  public void enforceType() {
    
    URL source = getClass().getClassLoader().getResource( "csv/text1.csv" );
    Assert.assertNotNull( source );
    
    CsvTableModel model   = new CsvTableModel();
    CsvOptions    options = CsvOptions.builder()
        .column( null )
        .column( null )
        // we're enforcing the type Long for this column
        .column( CsvColumn.<Long>builder().type( Long.class ).adapter( MiscFunctions::parseLong ).defaultValue( 0L ).nullable().title( "longVal" ).build() )
        .fillMissingColumns()
        .build();
    
    IoFunctions.forInputStreamDo( source, options, model::load );
    
    assertThat( model.getColumnCount(), is(3) );
    assertThat( model.getRowCount(), is(8) );
    assertEquals( model.getColumnClass(0), String.class );
    assertEquals( model.getColumnClass(1), String.class );
    assertEquals( model.getColumnClass(2), Long.class );
    
  }

  @Test(groups = "all", expectedExceptions = FailureException.class)
  public void inconsistentColumnNumbers() {
    URL source = getClass().getClassLoader().getResource( "csv/text2.csv" );
    Assert.assertNotNull( source );
    
    CsvTableModel model   = new CsvTableModel();
    CsvOptions    options = CsvOptions.builder()
        .build();
    
    IoFunctions.forInputStreamDo( source, options, model::load );
    
    assertThat( model.getColumnCount(), is(3) );
    assertThat( model.getRowCount(), is(8) );
    assertEquals( model.getColumnClass(0), String.class );
    assertEquals( model.getColumnClass(1), String.class );
    assertEquals( model.getColumnClass(2), Short.class );
    
  }

  @Test(groups = "all", expectedExceptions = FailureException.class)
  public void inconsistentColumnNumbersWithErrorHandling() {
    URL source = getClass().getClassLoader().getResource( "csv/text2.csv" );
    Assert.assertNotNull( source );
    
    CsvTableModel model   = new CsvTableModel();
    model.setErrorHandlerForInconsistentColumnCount( $ -> { /* do nothing */ } );
    CsvOptions    options = CsvOptions.builder()
        .build();
    
    IoFunctions.forInputStreamDo( source, options, model::load );
    
    assertThat( model.getColumnCount(), is(3) );
    
    // one less row as the row without the right amount of columns must be filtered out
    assertThat( model.getRowCount(), is(7) );
    
    assertEquals( model.getColumnClass(0), String.class );
    assertEquals( model.getColumnClass(1), String.class );
    assertEquals( model.getColumnClass(2), Short.class );
    
  }

} /* ENDCLASS */
