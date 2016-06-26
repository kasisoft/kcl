package com.kasisoft.libs.common.examples;

import org.w3c.dom.*;

import com.kasisoft.libs.common.xml.*;

import com.kasisoft.libs.common.ui.component.treetable.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.*;

import java.awt.*;

import java.nio.file.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreeTableDemo extends AbstractDemo {

  KTreeTable        treetable;
  
  public TreeTableDemo() {
    super( "Input Fields Demo" );
  }
  
  public void setModel( TreeTableModel model ) {
    treetable.setTreeTableModel( model );
  }
  
  @Override
  protected void components() {
    super.components();
    treetable = new KTreeTable();
  }
  
  @Override
  protected void arrange() {
    
    super.arrange();
    
    JPanel panel = newJPanel( new BorderLayout( 4, 4 ) );
    panel.add( new JScrollPane( treetable ), BorderLayout.CENTER );
    treetable.setFillsViewportHeight( true );
    
    addTab( "Tree Table", panel );
    
  }

  private String nodeNaming( Node node ) {
    return node.getNodeName();
  }

  private Object[] nodeColumns( Node node ) {
    return new Object[] { node.getNodeType(), node.getTextContent() };
  }

  public static void main( String[] args ) throws Exception {
    UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
    XmlParserConfiguration config = XmlParserConfiguration.builder().build();
    Document doc = XmlFunctions.readDocument( Paths.get( "/home/kasimir/Schreibtisch/validFourDWithManyEntriesStarhub.xml" ), config );
    TreeTableDemo demo = new TreeTableDemo();
    DefaultTreeTableModel model = new DefaultTreeTableModel( TreeTableFunctions.readXml( doc, demo::nodeNaming, demo::nodeColumns ) );
    demo.setModel( model );
    demo.setVisible( true );
  }
  
} /* ENDCLASS */
