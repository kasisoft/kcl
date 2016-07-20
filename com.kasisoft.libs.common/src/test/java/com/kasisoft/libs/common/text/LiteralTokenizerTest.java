package com.kasisoft.libs.common.text;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

import java.util.*;

/**
 * Testcases for the class 'LiteralTokenizer'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class LiteralTokenizerTest {

  @DataProvider(name="tokenizeDataProvider")
  public Object[][] tokenizeDataProvider() {
    
    String input1 = "@PART@ ist @FLUPPE@ heute hier";
    String input2 = "Fred ist @FLUPPE@ heute @PART@";
    String input3 = "Fred @PART@ @FLUPPE@ heute hier";
    String input4 = "#PART# ist #FLUPPE# heute hier";
    String input5 = "";
    
    return new Object[][] {
        
      { input1, Boolean.FALSE , new String[] { " ist ", " heute hier" } },
      { input1, Boolean.TRUE  , new String[] { "@PART@", " ist ", "@FLUPPE@", " heute hier" } },
      
      { input2, Boolean.FALSE , new String[] { "Fred ist ", " heute " } },
      { input2, Boolean.TRUE  , new String[] { "Fred ist ", "@FLUPPE@", " heute ", "@PART@" } },

      { input3, Boolean.FALSE , new String[] { "Fred ", " ", " heute hier" } },
      { input3, Boolean.TRUE  , new String[] { "Fred ", "@PART@", " ", "@FLUPPE@", " heute hier" } },

      { input4, Boolean.FALSE , new String[] { "#PART# ist #FLUPPE# heute hier" } },
      { input4, Boolean.TRUE  , new String[] { "#PART# ist #FLUPPE# heute hier" } },

      { input5, Boolean.FALSE , new String[] { "" } },
      { input5, Boolean.TRUE  , new String[] { "" } },

    };
    
  }
  
  @Test(dataProvider="tokenizeDataProvider", groups="all")
  public void tokenize( String input, boolean returndelimiters, Object[] expected ) {
    LiteralTokenizer tokenizer = new LiteralTokenizer( input, returndelimiters, "@PART@", "@FLUPPE@" );
    List<String>     tokens    = new ArrayList<>();
    while( tokenizer.hasMoreElements() ) {
      tokens.add( tokenizer.nextElement() );
    }
    assertThat( tokens.toArray(), is( expected ) );
  }

} /* ENDCLASS */
