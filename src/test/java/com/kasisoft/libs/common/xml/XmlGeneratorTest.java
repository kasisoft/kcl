package com.kasisoft.libs.common.xml;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

@Test
@SuppressWarnings("preview")
public class XmlGeneratorTest {

  private static final String EXPECTED_TAG = ""
                                             + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                                             + "<bibo>\n"
                                             + "  <dodo alpha=\"beta&lt;&gt;\">\n"
                                             + "    <marker>Wumpi &amp; Stumpi</marker>\n"
                                             + "  </dodo>\n"
                                             + "</bibo>\n"
                                             ;
  
  private static final String EXPECTED_INVALID_ATTRIBUTE = ""
                                                           + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                                                           + "<bibo>\n"
                                                           + "  <dodo>\n"
                                                           + "    <marker>Wumpi &amp; Stumpi</marker>\n"
                                                           + "  </dodo>\n"
                                                           + "</bibo>\n"
                                                           ;
  
  
  @Test(groups = "all")
  public void tag() {
    
    var generator = new XmlGenerator()
      .processingInstruction()
      .openTag("bibo")
        .openTagV("dodo", "alpha", "beta<>")
          .tag("marker", "Wumpi & Stumpi")
        .closeTag()
      .closeTag()
      ;
    
    assertThat(generator.toXml(), is(EXPECTED_TAG));
    
  }

  @Test(groups = "all")
  public void invalidAttribute() {
    
    var generator = new XmlGenerator()
      .processingInstruction()
      .openTag("bibo")
        .openTagV("dodo", new Object(), "beta<>")       // the bad attribute will NOT be part of the tag
          .tag("marker", "Wumpi & Stumpi")
        .closeTag()
      .closeTag()
      ;
    
    assertThat(generator.toXml(), is(EXPECTED_INVALID_ATTRIBUTE));
    
  }

  @Test(groups = "all", expectedExceptions = RuntimeException.class)
  public void invalidAttributeWithException() {
    new XmlGenerator()
      .withInvalidAttributeHandler(this::throwEx)
      .processingInstruction()
      .openTag("bibo")
        .openTagV("dodo", new Object(), "beta<>")
          .tag("marker", "Wumpi & Stumpi")
        .closeTag()
      .closeTag()
      ;
  }
  
  @SuppressWarnings("unused")
  private void throwEx(Object key, Object val) {
    throw new RuntimeException();
  }

} /* ENDCLASS */
