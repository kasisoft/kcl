package com.kasisoft.libs.common.old.xml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.Test;

@Test(groups="all")
public class XmlGeneratorTest {

  @Test
  public void tag() {
    
    XmlGenerator generator = new XmlGenerator()
      .processingInstruction()
      .openTag( "bibo" )
        .openTagV( "dodo", "alpha", "beta<>" )
          .tag( "marker", "Wumpi & Stumpi" )
        .closeTag()
      .closeTag()
      ;
    
    assertThat( generator.toXml(), is( 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
      + "<bibo>\n"
      + "  <dodo alpha=\"beta&lt;&gt;\">\n"
      + "    <marker>Wumpi &amp; Stumpi</marker>\n"
      + "  </dodo>\n"
      + "</bibo>\n"
    ) );
    
  }

  @Test
  public void invalidAttribute() {
    
    XmlGenerator generator = new XmlGenerator()
      .processingInstruction()
      .openTag( "bibo" )
        .openTagV( "dodo", new Object(), "beta<>" )
          .tag( "marker", "Wumpi & Stumpi" )
        .closeTag()
      .closeTag()
      ;
    
    assertThat( generator.toXml(), is( 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
      + "<bibo>\n"
      // the attribute is NOT part of the tag
      + "  <dodo>\n"
      + "    <marker>Wumpi &amp; Stumpi</marker>\n"
      + "  </dodo>\n"
      + "</bibo>\n"
    ) );
    
  }

  @Test(expectedExceptions = RuntimeException.class)
  public void invalidAttributeWithException() {
    new XmlGenerator()
      .withInvalidAttributeHandler( this::throwEx )
      .processingInstruction()
      .openTag( "bibo" )
        .openTagV( "dodo", new Object(), "beta<>" )
          .tag( "marker", "Wumpi & Stumpi" )
        .closeTag()
      .closeTag()
      ;
  }
  
  @SuppressWarnings("unused")
  private void throwEx( Object key, Object val ) {
    throw new RuntimeException();
  }

} /* ENDCLASS */