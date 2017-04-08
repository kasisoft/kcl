package com.kasisoft.libs.common.text;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

/**
 * Tests for the class 'TextCodec'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TextCodecTest {

  static final String TEXT = ""
      + "<?xml version='1.0' encoding='UTF-8'?>\n"
      + "<body>\n"
      + "  <tag attr='value'>content: sm&#246;rebr&#246;d</tag>\n"
      + "</body>\n"
      ;
  
  @DataProvider(name="createTextProcessingFactories")
  public Object[][] createTextProcessingFactories() {
    return new Object[][] {
      { TextProcessingFactory.STRING         , String         . class, TEXT , (Function<String, String>)         $ -> $                     },
      { TextProcessingFactory.STRINGBUFFER   , StringBuffer   . class, TEXT , (Function<String, StringBuffer>)   $ -> new StringBuffer($)   },
      { TextProcessingFactory.STRINGBUILDER  , StringBuilder  . class, TEXT , (Function<String, StringBuilder>)  $ -> new StringBuilder($)  },
      { TextProcessingFactory.STRINGFBUFFER  , StringFBuffer  . class, TEXT , (Function<String, StringFBuffer>)  $ -> new StringFBuffer($)  },
      { TextProcessingFactory.STRINGFBUILDER , StringFBuilder . class, TEXT , (Function<String, StringFBuilder>) $ -> new StringFBuilder($) }
    };
  }
  
  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void codec( TextProcessingFactory<T> factory, Class<T> type, String text, Function<String, T> supplier ) {
    
    TextCodec<T> codec = TextCodec.builder( type )
      .defaultCodec( factory.xmlEncoder          (), factory.xmlDecoder          () )
      .defaultCodec( factory.xmlNumericalDecoder (), factory.xmlNumericalEncoder () )
      .replacement( "content:", "{content}" )
      .build();
    
    T encoded = codec.getEncoder().apply( supplier.apply( text ) );
    T decoded = codec.getDecoder().apply( encoded );
    
    assertThat( decoded.toString(), is( text ) );
    
  }

  
} /* ENDCLASS */