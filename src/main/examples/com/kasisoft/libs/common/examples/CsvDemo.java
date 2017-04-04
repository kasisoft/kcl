package com.kasisoft.libs.common.examples;

import com.kasisoft.libs.common.csv.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.*;

import java.awt.event.*;

import java.awt.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CsvDemo extends AbstractDemo {

  JFileChooser  filechooser;
  JButton       select;
  JTable        table;
  
  public CsvDemo() {
    super( "CsvTable Demo" );
  }
  
  public void setModel( CsvTableModel model ) {
    table.setModel( model );
  }
  
  @Override
  protected void components() {
    super.components();
    table        = new JTable();
    select       = new JButton( "Select file" );
    filechooser  = new JFileChooser();
  }
  
  @Override
  protected void arrange() {
    
    super.arrange();
    
    JPanel panel = newJPanel( new BorderLayout( 4, 4 ) );
    panel.add( select                   , BorderLayout.NORTH  );
    panel.add( new JScrollPane( table ) , BorderLayout.CENTER );
    table.setFillsViewportHeight( true );
    
    addTab( "Csv Table", panel );
    
  }

  @Override
  protected void listeners() {
    super.listeners();
    select.addActionListener( this::chooseFile );
  }
  
  @SuppressWarnings("unused")
  private void chooseFile( ActionEvent $_ ) {
    
    int result = filechooser.showOpenDialog( this );
    if( result == JFileChooser.APPROVE_OPTION ) {
      
      File file = filechooser.getSelectedFile();
      
      CsvOptions options = CsvOptions.builder()
        .fillMissingColumns()
        .build();
      
      CsvTableModel tableModel = new CsvTableModel( options );
      tableModel.load( file.toPath() );
    
      setModel( tableModel );
      
    }
    
  }

  public static void main( String[] args ) throws Exception {
    UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
    CsvDemo demo = new CsvDemo();
    demo.setVisible( true );
  }
  
} /* ENDCLASS */
