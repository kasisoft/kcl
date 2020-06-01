package com.kasisoft.libs.common.old.text;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.text.StringFBuilder;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.function.Function;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

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
