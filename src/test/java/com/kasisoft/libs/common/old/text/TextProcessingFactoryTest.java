package com.kasisoft.libs.common.old.text;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.old.model.Pair;
import com.kasisoft.libs.common.text.StringFBuffer;
import com.kasisoft.libs.common.text.StringFBuilder;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.function.Function;

import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the class 'TextProcessingFactory'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TextProcessingFactoryTest {

  private static final String TEXT = ""
    + "  This is my text.\t\r"
    ;

  private static final String CAMELCASE_TEXT = ""
    + " fredo-MarchZ_dk"
    ;

  private static final String XML_TEXT = ""
    + "<tag>This is my &#252; and ö text</tag>"
    ;

  private static final String XML_CONTENT = ""
    + "<Bla\nBlub\r\n>"
    ;
  
  @DataProvider(name="createTextProcessingFactories")
  public Object[][] createTextProcessingFactories() {
    return new Object[][] {
      { TextProcessingFactory.STRING         , TEXT , (Function<String, String>)         $ -> $                     },
      { TextProcessingFactory.STRINGBUFFER   , TEXT , (Function<String, StringBuffer>)   $ -> new StringBuffer($)   },
      { TextProcessingFactory.STRINGBUILDER  , TEXT , (Function<String, StringBuilder>)  $ -> new StringBuilder($)  },
      { TextProcessingFactory.STRINGFBUFFER  , TEXT , (Function<String, StringFBuffer>)  $ -> new StringFBuffer($)  },
      { TextProcessingFactory.STRINGFBUILDER , TEXT , (Function<String, StringFBuilder>) $ -> new StringFBuilder($) }
    };
  }

  @DataProvider(name="createTextProcessingFactoriesXmlText")
  public Object[][] createTextProcessingFactoriesXmlText() {
    return new Object[][] {
      { TextProcessingFactory.STRING         , XML_TEXT , (Function<String, String>)         $ -> $                     },
      { TextProcessingFactory.STRINGBUFFER   , XML_TEXT , (Function<String, StringBuffer>)   $ -> new StringBuffer($)   },
      { TextProcessingFactory.STRINGBUILDER  , XML_TEXT , (Function<String, StringBuilder>)  $ -> new StringBuilder($)  },
      { TextProcessingFactory.STRINGFBUFFER  , XML_TEXT , (Function<String, StringFBuffer>)  $ -> new StringFBuffer($)  },
      { TextProcessingFactory.STRINGFBUILDER , XML_TEXT , (Function<String, StringFBuilder>) $ -> new StringFBuilder($) }
    };
  }

  @DataProvider(name="createTextProcessingFactoriesXmlContent")
  public Object[][] createTextProcessingFactoriesXmlContent() {
    return new Object[][] {
      { TextProcessingFactory.STRING         , XML_CONTENT , (Function<String, String>)         $ -> $                     },
      { TextProcessingFactory.STRINGBUFFER   , XML_CONTENT , (Function<String, StringBuffer>)   $ -> new StringBuffer($)   },
      { TextProcessingFactory.STRINGBUILDER  , XML_CONTENT , (Function<String, StringBuilder>)  $ -> new StringBuilder($)  },
      { TextProcessingFactory.STRINGFBUFFER  , XML_CONTENT , (Function<String, StringFBuffer>)  $ -> new StringFBuffer($)  },
      { TextProcessingFactory.STRINGFBUILDER , XML_CONTENT , (Function<String, StringFBuilder>) $ -> new StringFBuilder($) }
    };
  }

  @DataProvider(name="createCamelCaseContent")
  public Object[][] createCamelCaseContent() {
    return new Object[][] {
      { TextProcessingFactory.STRING         , CAMELCASE_TEXT , (Function<String, String>)         $ -> $                     },
      { TextProcessingFactory.STRINGBUFFER   , CAMELCASE_TEXT , (Function<String, StringBuffer>)   $ -> new StringBuffer($)   },
      { TextProcessingFactory.STRINGBUILDER  , CAMELCASE_TEXT , (Function<String, StringBuilder>)  $ -> new StringBuilder($)  },
      { TextProcessingFactory.STRINGFBUFFER  , CAMELCASE_TEXT , (Function<String, StringFBuffer>)  $ -> new StringFBuffer($)  },
      { TextProcessingFactory.STRINGFBUILDER , CAMELCASE_TEXT , (Function<String, StringFBuilder>) $ -> new StringFBuilder($) }
    };
  }
  
  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void replaceByKeyValue( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    text = text.trim();
    
    // 1 match
    T text1    = supplier.apply( text );
    T outcome1 = factory.replace( "my", "our" ).apply( text1 );
    assertThat( outcome1.toString(), is( "This is our text." ) );

    // 2 matches
    T text2    = supplier.apply( text );
    T outcome2 = factory.replace( "is", "os" ).apply( text2 );
    assertThat( outcome2.toString(), is( "Thos os my text." ) );

    // no matches
    T text3    = supplier.apply( text );
    T outcome3 = factory.replace( "gollum", "os" ).apply( text3 );
    assertThat( outcome3.toString(), is( "This is my text." ) );

    // null replacement
    T text4    = supplier.apply( text );
    T outcome4 = factory.replace( "my ", null ).apply( text4 );
    assertThat( outcome4.toString(), is( "This is text.") );

  }

  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void replaceByKeyValueCaseInsensitive( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    text = text.trim();

    // 1 match
    T text1    = supplier.apply( text );
    T outcome1 = factory.replace( "MY", "our", false ).apply( text1 );
    assertThat( outcome1.toString(), is( "This is our text." ) );

    // 2 matches
    T text2    = supplier.apply( text );
    T outcome2 = factory.replace( "IS", "os", false ).apply( text2 );
    assertThat( outcome2.toString(), is( "Thos os my text." ) );

    // no matches
    T text3    = supplier.apply( text );
    T outcome3 = factory.replace( "GOLLUM", "os", false ).apply( text3 );
    assertThat( outcome3.toString(), is( "This is my text." ) );

    // null replacement
    T text4    = supplier.apply( text );
    T outcome4 = factory.replace( "MY ", null, false ).apply( text4 );
    assertThat( outcome4.toString(), is( "This is text." ) );

  }

  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void replaceByKeyValuesMap( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    text = text.trim();

    Map<String, String> replacements = new HashMap<>();
    replacements.put( "my"    , "out"  );
    replacements.put( "is"    , "os"   );
    replacements.put( "gollum", "bibo" );
    replacements.put( "text"  , null   );
    
    T text1    = supplier.apply( text );
    T outcome1 = factory.replace( replacements ).apply( text1 );
    assertThat( outcome1.toString(), is( "Thos os out ." ) );

  }

  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void replaceByKeyValuesMapCaseInsensitive( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    text = text.trim();

    Map<String, String> replacements = new HashMap<>();
    replacements.put( "MY"    , "out"  );
    replacements.put( "IS"    , "os"   );
    replacements.put( "GOLLUM", "bibo" );
    replacements.put( "TEXT"  , null   );
    
    T text1    = supplier.apply( text );
    T outcome1 = factory.replace( replacements, false ).apply( text1 );
    assertThat( outcome1.toString(), is( "Thos os out ." ) );

  }

  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void replaceByKeyValuesPairs( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    text = text.trim();

    List<Pair<String, String>> replacements = new ArrayList<>();
    replacements.add( new Pair<>( "my"    , "out"  ) );
    replacements.add( new Pair<>( "is"    , "os"   ) );
    replacements.add( new Pair<>( "gollum", "bibo" ) );
    replacements.add( new Pair<>( "text"  , null   ) );
    
    T text1    = supplier.apply( text );
    T outcome1 = factory.replace( replacements ).apply( text1 );
    assertThat( outcome1.toString(), is( "Thos os out ." ) );

  }

  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void replaceByKeyValuesPairsCaseInsensitive( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    text = text.trim();

    List<Pair<String, String>> replacements = new ArrayList<>();
    replacements.add( new Pair<>( "MY"    , "out"  ) );
    replacements.add( new Pair<>( "IS"    , "os"   ) );
    replacements.add( new Pair<>( "GOLLUM", "bibo" ) );
    replacements.add( new Pair<>( "TEXT"  , null   ) );
    
    T text1    = supplier.apply( text );
    T outcome1 = factory.replace( replacements, false ).apply( text1 );
    assertThat( outcome1.toString(), is( "Thos os out ." ) );

  }

  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void trim( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    T text1    = supplier.apply( text );
    T outcome1 = factory.trim().apply( text1 );
    assertThat( outcome1.toString(), is( "This is my text." ) );

    T text2    = supplier.apply( text );
    T outcome2 = factory.trim( "\r\t" ).apply( text2 );
    assertThat( outcome2.toString(), is( "  This is my text." ) );

  }

  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void trimLeft( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    T text1    = supplier.apply( text );
    T outcome1 = factory.trimLeft().apply( text1 );
    assertThat( outcome1.toString(), is( "This is my text.\t\r" ) );

    T text2    = supplier.apply( text );
    T outcome2 = factory.trimLeft( " " ).apply( text2 );
    assertThat( outcome2.toString(), is( "This is my text.\t\r" ) );

  }

  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void trimRight( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    T text1    = supplier.apply( text );
    T outcome1 = factory.trimRight().apply( text1 );
    assertThat( outcome1.toString(), is( "  This is my text." ) );

    T text2    = supplier.apply( text );
    T outcome2 = factory.trimRight( "\r\t" ).apply( text2 );
    assertThat( outcome2.toString(), is( "  This is my text." ) );

  }

  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void toLowerCase( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    T text1    = supplier.apply( text );
    T outcome1 = factory.toLowerCase().apply( text1 );
    assertThat( outcome1.toString(), is( "  this is my text.\t\r" ) );
  }

  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void toUpperCase( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    T text1    = supplier.apply( text );
    T outcome1 = factory.toUpperCase().apply( text1 );
    assertThat( outcome1.toString(), is( "  THIS IS MY TEXT.\t\r" ) );
  }

  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void replaceAllWithValue( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    T text1    = supplier.apply( text );
    T outcome1 = factory.replaceAll( Pattern.compile( Pattern.quote( "is" ) ), (String) null ).apply( text1 );
    assertThat( outcome1.toString(), is( "  Th  my text.\t\r" ) );

    T text2    = supplier.apply( text );
    T outcome2 = factory.replaceAll( Pattern.compile( Pattern.quote( "is" ) ), "os" ).apply( text2 );
    assertThat( outcome2.toString(), is( "  Thos os my text.\t\r" ) );

  }

  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void replaceAllWithFunction( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    Function<String, String> retNull  = $ -> null;
    Function<String, String> retValue = $ -> "os";
    
    T text1    = supplier.apply( text );
    T outcome1 = factory.replaceAll( Pattern.compile( Pattern.quote( "is" ) ), retNull ).apply( text1 );
    assertThat( outcome1.toString(), is( "  Th  my text.\t\r" ) );

    T text2    = supplier.apply( text );
    T outcome2 = factory.replaceAll( Pattern.compile( Pattern.quote( "is" ) ), retValue ).apply( text2 );
    assertThat( outcome2.toString(), is( "  Thos os my text.\t\r" ) );

  }

  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void replaceFirstWithValue( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    T text1    = supplier.apply( text );
    T outcome1 = factory.replaceFirst( Pattern.compile( Pattern.quote( "is" ) ), (String) null ).apply( text1 );
    assertThat( outcome1.toString(), is( "  Th is my text.\t\r" ) );

    T text2    = supplier.apply( text );
    T outcome2 = factory.replaceFirst( Pattern.compile( Pattern.quote( "is" ) ), "os" ).apply( text2 );
    assertThat( outcome2.toString(), is( "  Thos is my text.\t\r" ) );

  }

  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void replaceFirstWithFunction( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    Function<String, String> retNull  = $ -> null;
    Function<String, String> retValue = $ -> "os";
    
    T text1    = supplier.apply( text );
    T outcome1 = factory.replaceFirst( Pattern.compile( Pattern.quote( "is" ) ), retNull ).apply( text1 );
    assertThat( outcome1.toString(), is( "  Th is my text.\t\r" ) );

    T text2    = supplier.apply( text );
    T outcome2 = factory.replaceFirst( Pattern.compile( Pattern.quote( "is" ) ), retValue ).apply( text2 );
    assertThat( outcome2.toString(), is( "  Thos is my text.\t\r" ) );

  }

  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void replaceLastWithValue( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    T text1    = supplier.apply( text );
    T outcome1 = factory.replaceLast( Pattern.compile( Pattern.quote( "is" ) ), (String) null ).apply( text1 );
    assertThat( outcome1.toString(), is( "  This  my text.\t\r" ) );

    T text2    = supplier.apply( text );
    T outcome2 = factory.replaceLast( Pattern.compile( Pattern.quote( "is" ) ), "os" ).apply( text2 );
    assertThat( outcome2.toString(), is( "  This os my text.\t\r" ) );

  }

  @Test(dataProvider="createTextProcessingFactories", groups="all")
  public <T extends CharSequence> void replaceLastWithFunction( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    Function<String, String> retNull  = $ -> null;
    Function<String, String> retValue = $ -> "os";
    
    T text1    = supplier.apply( text );
    T outcome1 = factory.replaceLast( Pattern.compile( Pattern.quote( "is" ) ), retNull ).apply( text1 );
    assertThat( outcome1.toString(), is( "  This  my text.\t\r" ) );

    T text2    = supplier.apply( text );
    T outcome2 = factory.replaceLast( Pattern.compile( Pattern.quote( "is" ) ), retValue ).apply( text2 );
    assertThat( outcome2.toString(), is( "  This os my text.\t\r" ) );

  }

  @Test(dataProvider="createTextProcessingFactoriesXmlText", groups="all")
  public <T extends CharSequence> void xmlNumericalDecoder( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    T text1    = supplier.apply( text );
    T outcome1 = factory.xmlNumericalDecoder().apply( text1 );
    assertThat( outcome1.toString(), is( "<tag>This is my ü and ö text</tag>" ) );

    T text2    = supplier.apply( text );
    T outcome2 = factory.xmlNumericalDecoder( $ -> $.intValue() > 255, true ).apply( text2 );
    assertThat( outcome2.toString(), is( "<tag>This is my &#252; and ö text</tag>" ) );

    T text3    = supplier.apply( text );
    T outcome3 = factory.xmlNumericalDecoder( $ -> $.intValue() > 255, false ).apply( text3 );
    assertThat( outcome3.toString(), is( "<tag>This is my ü and ö text</tag>" ) );

  }

  @Test(dataProvider="createTextProcessingFactoriesXmlText", groups="all")
  public <T extends CharSequence> void xmlNumericalEncoder( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {

    T text1    = supplier.apply( text );
    T outcome1 = factory.xmlNumericalEncoder().apply( text1 );
    assertThat( outcome1.toString(), is( "<tag>This is my &#252; and &#246; text</tag>" ) );

    T text2    = supplier.apply( text );
    T outcome2 = factory.xmlNumericalEncoder( $ -> $.intValue() > 255 && $.intValue() != 246 ).apply( text2 );
    assertThat( outcome2.toString(), is( "<tag>This is my &#252; and ö text</tag>" ) );

  }

  @Test(dataProvider="createTextProcessingFactoriesXmlContent", groups="all")
  public <T extends CharSequence> void xmlEncodingDecoding( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {

    T text1    = supplier.apply( text );
    T outcome1 = factory.xmlEncoder( false ).apply( text1 );
    assertThat( outcome1.toString(), is( "&lt;Bla\nBlub\r\n&gt;" ) );

    T text2    = supplier.apply( text1.toString() );
    T outcome2 = factory.xmlDecoder( false ).apply( text2 );
    assertThat( outcome2.toString(), is( text ) );

    T text3    = supplier.apply( text );
    T outcome3 = factory.xmlEncoder( true ).apply( text3 );
    assertThat( outcome3.toString(), is( "&lt;Bla&#10;Blub&#13;&#10;&gt;" ) );

    T text4    = supplier.apply( text3.toString() );
    T outcome4 = factory.xmlDecoder( true ).apply( text4 );
    assertThat( outcome4.toString(), is( text ) );

  }

  @Test(dataProvider="createCamelCaseContent", groups="all")
  public <T extends CharSequence> void camelCase( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {

    T text1    = supplier.apply( text );
    T outcome1 = factory.camelCase().apply( text1 );
    assertThat( outcome1.toString(), is( "fredoMarchZDk" ) );

  }

} /* ENDCLASS */
