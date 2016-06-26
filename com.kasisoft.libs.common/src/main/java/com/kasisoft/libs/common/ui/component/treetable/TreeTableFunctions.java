package com.kasisoft.libs.common.ui.component.treetable;

import org.w3c.dom.*;

import com.kasisoft.libs.common.xml.*;

import lombok.*;

import java.util.function.*;

import java.util.*;

/**
 * Collection of support functions to be used in conjunction with the tree table.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class TreeTableFunctions {

  public static TreeTableNode readXml( @NonNull Document doc, @NonNull Function<Node, String> naming, @NonNull Function<Node, Object[]> columns ) {
    return buildTree( null, doc.getDocumentElement(), naming, columns );
  }
  
  private static TreeTableNode buildTree( DefaultTreeTableNode parent, Element base, Function<Node, String> naming, Function<Node, Object[]> columns ) {
    
    DefaultTreeTableNode child = newNode( base, naming, columns );
    if( parent != null ) {
      parent.add( child );
    }
    
    Map<String, Attr> attributes = XmlFunctions.getAttributes( base, false );
    List<String>      names      = new ArrayList<>( attributes.keySet() );
    Collections.sort( names );
    
    names.forEach( $ -> child.add( newNode( attributes.get($), $_ -> "@" + naming.apply($_), columns ) ) );
    
    XmlFunctions.getChildElements( base ).forEach( $ -> buildTree( child, $, naming, columns ) );
    
    return child;
    
  }
  
  private static DefaultTreeTableNode newNode( Node node, Function<Node, String> naming, Function<Node, Object[]> columns ) {
    return new DefaultTreeTableNode( naming.apply( node ), columns.apply( node ) );
  }
  
} /* ENDCLASS */
