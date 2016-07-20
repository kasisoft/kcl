
import org.apache.commons.lang3.text.translate.*;

import com.kasisoft.libs.common.text.*;

import lombok.experimental.*;

import lombok.*;

/**
 * An decoded/encoder for texts.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comparison {
  
  public static final CharSequenceTranslator UNESCAPE_XML = 
      new AggregateTranslator(
          new LookupTranslator(EntityArrays.BASIC_UNESCAPE()),
          new LookupTranslator(EntityArrays.APOS_UNESCAPE())
      );
  
  public static void main( String[] args ) {
    
    TextCodec<StringBuilder> codec = TextCodec.builder( StringBuilder.class )
        .replacement( "&apos;", "'" )
        .replacement( "&gt;", ">" )
        .replacement( "&lt;", "<" )
        .replacement( "&amp;", "&" )
        .replacement( "&quot;", "\"" )
        .build();
        
    String        myText  = "Mein &lt;Text\n\n\r &GT;&gt; &apos;Hello&apos; &quot;world&quot; !";
    StringBuilder myText1 = new StringBuilder( myText );
    System.err.println( ":: " + codec.getEncoder().apply( myText1 ));
    System.err.println( ":: " + UNESCAPE_XML.translate( myText ));
    System.err.println( ":: " + UNESCAPE_XML.translate( myText ).equals( codec.getEncoder().apply( myText1 ).toString() ));
    
    for( int i = 0; i < 10000; i++ ) {
      codec.getEncoder().apply( myText1 );
    }
    long l1 = System.currentTimeMillis();
    for( int i = 0; i < 1000000; i++ ) {
      codec.getEncoder().apply( myText1 );
    }
    long l2 = System.currentTimeMillis();
    for( int i = 0; i < 10000; i++ ) {
      UNESCAPE_XML.translate( myText );
    }
    long l3 = System.currentTimeMillis();
    for( int i = 0; i < 1000000; i++ ) {
      UNESCAPE_XML.translate( myText );
    }
    long l4 = System.currentTimeMillis();
    System.err.println( "L1: " + (l2 - l1) );
    System.err.println( "L2: " + (l4 - l3) );
 
  }
  
} /* ENDCLASS */
