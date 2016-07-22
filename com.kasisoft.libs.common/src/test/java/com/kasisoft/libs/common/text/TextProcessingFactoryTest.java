package com.kasisoft.libs.common.text;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

import com.kasisoft.libs.common.model.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.regex.*;

import java.util.*;

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

  private static final String XML_TEXT = ""
    + "<tag>This is my &#252; and ö text</tag>"
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
  public <T extends CharSequence> void xmlDecoder( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {
    
    T text1    = supplier.apply( text );
    T outcome1 = factory.xmlDecoder().apply( text1 );
    assertThat( outcome1.toString(), is( "<tag>This is my ü and ö text</tag>" ) );

    T text2    = supplier.apply( text );
    T outcome2 = factory.xmlDecoder( $ -> $.intValue() > 255, true ).apply( text2 );
    assertThat( outcome2.toString(), is( "<tag>This is my &#252; and ö text</tag>" ) );

    T text3    = supplier.apply( text );
    T outcome3 = factory.xmlDecoder( $ -> $.intValue() > 255, false ).apply( text3 );
    assertThat( outcome3.toString(), is( "<tag>This is my ü and ö text</tag>" ) );

  }

  @Test(dataProvider="createTextProcessingFactoriesXmlText", groups="all")
  public <T extends CharSequence> void xmlEncoder( TextProcessingFactory<T> factory, String text, Function<String, T> supplier ) {

    T text1    = supplier.apply( text );
    T outcome1 = factory.xmlEncoder().apply( text1 );
    assertThat( outcome1.toString(), is( "<tag>This is my &#252; and &#246; text</tag>" ) );

    T text2    = supplier.apply( text );
    T outcome2 = factory.xmlEncoder( $ -> $.intValue() > 255 && $.intValue() != 246 ).apply( text2 );
    assertThat( outcome2.toString(), is( "<tag>This is my &#252; and ö text</tag>" ) );

  }

} /* ENDCLASS */
