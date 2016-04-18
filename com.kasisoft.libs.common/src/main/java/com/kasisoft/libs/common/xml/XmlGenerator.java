package com.kasisoft.libs.common.xml;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.util.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.*;

/**
 * Helper class which allows to generate xml files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "builder") @ToString(of = "builder")
public class XmlGenerator implements AutoCloseable {

  StringFBuilder                      builder;
  Encoding                            encoding;
  Stack<String>                       tags;
  StringBuilder                       indentation;
  String                              indent;
  
  BiConsumer<Object, Object>          handleInvalidAttribute;
  BiFunction<String, Object, String>  attributeValueConverter;
  
  /**
   * Creates this generator using a certain encoding and a specific indentation size.
   * 
   * @param encoding     The encoding to be used. If <code>null</code> the default encoding UTF-8 is used.  
   * @param indentsize   The indentation size. If <code>null</code> the default 2 is being used.
   */
  public XmlGenerator() {
    this( null, null );
  }

  /**
   * Creates this generator using a certain encoding and the default indentation size.
   * 
   * @param encoding   The encoding to be used. If <code>null</code> the default encoding UTF-8 is used.  
   */
  public XmlGenerator( Encoding encoding ) {
    this( encoding, null );
  }

  /**
   * Creates this generator using the default encoding and a specific indentation size.
   * 
   * @param indent   The indentation size.
   */
  public XmlGenerator( int indent ) {
    this( null, indent );
  }

  /**
   * Creates this generator using a certain encoding and a specific indentation size.
   * 
   * @param encoding     The encoding to be used. If <code>null</code> the default encoding UTF-8 is used.  
   * @param indentsize   The indentation size. If <code>null</code> the default 2 is being used.
   */
  public XmlGenerator( Encoding encoding, Integer indentsize ) {
    builder                 = new StringFBuilder();
    encoding                = encoding != null ? encoding : Encoding.UTF8;
    tags                    = new Stack<>();
    indentation             = new StringBuilder();
    indent                  = StringFunctions.fillString( indentsize != null ? indentsize.intValue() : 2, ' ' );
    handleInvalidAttribute  = this::handleInvalidAttribute;
    attributeValueConverter = this::attributeValueConverter;
  }
  
  /**
   * Changes the handler which is used when an invalid attribute key has been detected.
   * 
   * @param handler   The handler to be used. If <code>null</code> the default handler is used (ignores such cases).
   * 
   * @return   this
   */
  public synchronized <T extends XmlGenerator> T withInvalidAttributeHandler( BiConsumer<Object, Object> handler ) {
    if( handler != null ) {
      handleInvalidAttribute = handler;
    } else {
      handleInvalidAttribute = this::handleInvalidAttribute;
    }
    return (T) this;
  }
  
  /**
   * Changes the converter used for attribute values.
   * 
   * @param converter   The converter which is used for attribute values. If <code>null</code> the default
   *                    converter is being used.
   * 
   * @return   this
   */
  public synchronized <T extends XmlGenerator> T withAttributeValueConverter( BiFunction<String, Object, String> converter ) {
    if( converter != null ) {
      attributeValueConverter = converter;
    } else {
      attributeValueConverter = this::attributeValueConverter;
    }
    return (T) this;
  }
  
  /**
   * The default implementation allowing to deal with unsupported attribute keys.
   * 
   * @param key   The invalid attribute key.
   * @param val   The corresponding attribute value.
   */
  private void handleInvalidAttribute( Object key, Object val ) {
  }
  
  /**
   * This converter simply provides the textual presentation of the value.
   * 
   * @param key   The attribute name that is currently being handled. Neither <code>null</code> nor empty.
   * @param val   The attribute value. Maybe <code>null</code>.
   * 
   * @return   The converted attribute value. Maybe <code>null</code>.
   */
  private String attributeValueConverter( String key, Object val ) {
    return String.valueOf( val );
  }
  
  /**
   * Resets the inner state of this generator allowing it to be reused.
   */
  public synchronized void reset() {
    builder.setLength(0);
    tags.clear();
    indentation.setLength(0);
  }
  
  /**
   * Performs an indent for the upcoming generation.
   */
  private void indent() {
    indentation.append( indent );
  }
  
  /**
   * Performs a dedent for the upcoming generation.
   */
  private void dedent() {
    int newlength = Math.max( indentation.length() - indent.length(), 0 );
    indentation.setLength( newlength );
  }
  
  /**
   * Generates a map of attributes from the supplied list of pairs.
   * 
   * @param attributes   A list of pairs establishing the attribute map. Maybe <code>null</code>.
   * 
   * @return   A map of attributes. Not <code>null</code>.
   */
  private Map<String, Object> asMap( Object ... attributes ) {
    Map<String, Object> result = null;
    if( (attributes != null) && (attributes.length >= 2) ) {
      result = new HashMap<>();
      for( int i = 0; i < attributes.length; i += 2 ) {
        Object key  = attributes[i];
        Object val  = attributes[i + 1];
        String name = key instanceof String ? StringFunctions.cleanup( (String) key ) : null;
        if( name != null ) {
          result.put( name, val ); 
        } else {
          handleInvalidAttribute.accept( key, val );
        }
      }
    }
    return asMap( result );
  }
  
  /**
   * Ensures that a non-null map is returned.
   * 
   * @param map   The map that's supposed to be returned if possible. Maybe <code>null</code>.
   * 
   * @return   The supplied map or a substitute. Not <code>null</code>.
   */
  private Map<String, Object> asMap( Map<String, Object> map ) {
    if( (map == null) || map.isEmpty() ) {
      return Collections.emptyMap();
    } else {
      return map;
    }
  }

  /**
   * Writes a complete tag. 
   * 
   * @param tag          The tag name. Neither <code>null</code> nor empty.
   * @param attributes   The attributes associated with this tag. Maybe <code>null</code>.
   * 
   * @return   this
   */
  public synchronized <T extends XmlGenerator> T tagV( String tag, Object ... attributes ) {
    tag( tag, null, asMap( attributes ) );
    return (T) this;
  }

  /**
   * Writes a complete tag. 
   * 
   * @param tag          The tag name. Neither <code>null</code> nor empty.
   * @param text         The text contained with this tag. Maybe <code>null</code>.
   * @param attributes   The attributes associated with this tag. Maybe <code>null</code>.
   * 
   * @return   this
   */
  public synchronized <T extends XmlGenerator> T tagV( String tag, String text, Object ... attributes ) {
    tag( tag, text, asMap( attributes ) );
    return (T) this;
  }

  /**
   * Writes a complete tag. 
   * 
   * @param tag          The tag name. Neither <code>null</code> nor empty.
   * @param attributes   The attributes associated with this tag. Maybe <code>null</code>.
   * 
   * @return   this
   */
  public synchronized <T extends XmlGenerator> T tag( String tag, Object[] attributes ) {
    tag( tag, null, asMap( attributes ) );
    return (T) this;
  }

  /**
   * Writes a complete tag. 
   * 
   * @param tag          The tag name. Neither <code>null</code> nor empty.
   * @param text         The text contained with this tag. Maybe <code>null</code>.
   * @param attributes   The attributes associated with this tag. Maybe <code>null</code>.
   * 
   * @return   this
   */
  public synchronized <T extends XmlGenerator> T tag( String tag, String text, Object[] attributes ) {
    tag( tag, text, asMap( attributes ) );
    return (T) this;
  }
  
  /**
   * Writes a complete tag. 
   * 
   * @param tag          The tag name. Neither <code>null</code> nor empty.
   * @param attributes   The attributes associated with this tag. Maybe <code>null</code>.
   * 
   * @return   this
   */
  public synchronized <T extends XmlGenerator> T tag( String tag, Map<String, Object> attributes ) {
    tag( tag, null, attributes );
    return (T) this;
  }
  
  /**
   * Writes a complete tag. 
   * 
   * @param tag          The tag name. Neither <code>null</code> nor empty.
   * @param text         The text contained with this tag. Maybe <code>null</code>.
   * @param attributes   The attributes associated with this tag. Maybe <code>null</code>.
   * 
   * @return   this
   */
  public synchronized <T extends XmlGenerator> T tag( String tag, String text, Map<String, Object> attributes ) {
    tag        = StringFunctions.cleanup( tag );
    attributes = asMap( attributes );
    text       = StringFunctions.cleanup( text );
    builder.append( indentation );
    builder.appendF( "<%s", tag );
    if( ! attributes.isEmpty() ) {
      writeAttributes( attributes );
    }
    if( text != null ) {
      if( text.indexOf( '\n' ) != -1 ) {
        builder.append( ">\n" );
        indent();
        builder.appendF( "%s%s\n", indentation, escapeXml( text ) );
        dedent();
        builder.append( indentation );
      } else {
        builder.appendF( ">%s", escapeXml( text ) );
      }
      builder.appendF( "</%s>\n", tag );
    } else {
      builder.append( "/>\n" );
    }
    return (T) this;
  }

  /**
   * Opens a tag which potentially contains other tags. 
   * 
   * @param tag          The tag name. Neither <code>null</code> nor empty.
   * @param attributes   The attributes associated with this tag. Maybe <code>null</code>.
   * 
   * @return   this
   */
  public synchronized <T extends XmlGenerator> T openTagV( String tag, Object ... attributes ) {
    openTag( tag, asMap( attributes ) );
    return (T) this;
  }

  /**
   * Opens a tag which potentially contains other tags. 
   * 
   * @param tag          The tag name. Neither <code>null</code> nor empty.
   * @param attributes   The attributes associated with this tag. Maybe <code>null</code>.
   * 
   * @return   this
   */
  public synchronized <T extends XmlGenerator> T openTag( String tag, Object[] attributes ) {
    openTag( tag, asMap( attributes ) );
    return (T) this;
  }
  
  /**
   * Opens a tag which potentially contains other tags. 
   * 
   * @param tag          The tag name. Neither <code>null</code> nor empty.
   * @param attributes   The attributes associated with this tag. Maybe <code>null</code>.
   * 
   * @return   this
   */
  public synchronized <T extends XmlGenerator> T openTag( String tag, Map<String, Object> attributes ) {
    tag        = StringFunctions.cleanup( tag );
    attributes = asMap( attributes );
    builder.append( indentation );
    builder.appendF( "<%s", tag );
    if( ! attributes.isEmpty() ) {
      writeAttributes( attributes );
    }
    builder.appendF( ">\n" );
    indent();
    tags.push( tag );
    return (T) this;
  }

  /**
   * Closes the last tag that had been opened. 
   * 
   * @return   this
   */
  public synchronized <T extends XmlGenerator> T closeTag() {
    if( ! tags.isEmpty() ) {
      dedent();
      String tag = tags.pop();
      builder.appendF( "%s</%s>\n", indentation, tag );
    }
    return (T) this;
  }

  /**
   * Writes the supplied text in a CDATA block.
   * 
   * @param text   The text that has to be written in a CDATA block.
   * 
   * @return   this
   */
  public synchronized <T extends XmlGenerator> T cdata( String text ) {
    if( text != null ) {
      builder.appendF( "<![CDATA[%s]]>\n", text );
    }
    return (T) this;
  }
  
  /**
   * Writes a processing instruction.
   * 
   * @return   this
   */
  public synchronized <T extends XmlGenerator> T processingInstruction() {
    builder.appendF( "<?xml version\"=1.0\" encoding=\"%s\"?>\n", encoding.getEncoding() );
    return (T) this;
  }

  /**
   * Writes all attributes sorted by their key names.
   * 
   * @param attributes   The attributes that shall be written. Neither <code>null</code> nor empty.
   */
  private void writeAttributes( Map<String, Object> attributes ) {
    List<String> keys = new ArrayList<>( attributes.keySet() );
    Collections.sort( keys );
    for( String key : keys ) {
      String val = attributeValueConverter.apply( key, attributes.get( key ) );
      if( val != null ) {
        builder.appendF( " %s=\"%s\"", key, escapeXml( val ) );
      }
    }
  }
  
  @Override
  public void close() {
    while( ! tags.isEmpty() ) {
      closeTag();
    }
  }
  
  /**
   * Returns the current xml content.
   * 
   * @return   The current xml content. Not <code>null</code>.
   */
  public synchronized String toXml() {
    close();
    return builder.toString();
  }

  /**
   * Escapes the special characters for XML.
   * 
   * @param text  The text that will require escaping. Not <code>null</code>.
   * 
   * @return   The escaped character. Not <code>null</code>.
   */
  private String escapeXml( String text ) {
    if( text.length() > 0 ) {
      text = XmlFunctions.escapeXml( text );
    }
    return text;
  }
  
} /* ENDCLASS */
