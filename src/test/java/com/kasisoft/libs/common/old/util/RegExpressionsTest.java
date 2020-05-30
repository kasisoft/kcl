package com.kasisoft.libs.common.old.util;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Test(groups="all")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegExpressionsTest {

  @DataProvider(name = "extractYoutubeIdData")
  public Object[][] extractYoutubeIdData() {
    return new Object[][] {
      { "https://www.youtube.com/watch?v=LoT1DHwA7ds" , "LoT1DHwA7ds" },
      { "https://www.youtube.com/watch?v=ZedhlLvph0g" , "ZedhlLvph0g" },
      { "https://www.amiga-news.de"                   , null          },
      { ""                                            , null          },
      { null                                          , null          },
    };
  }
  
  @Test(dataProvider = "extractYoutubeIdData")
  public void extractYoutubeId( String url, String expected ) {
    assertThat( RegExpressions.extractYoutubeId( url ), is( expected ) );
  }

  @DataProvider(name = "isEmailData")
  public Object[][] isEmailData() {
    return new Object[][] {
      { "daniel.kasmeroglu@kasisoft.net"              , "daniel.kasmeroglu@kasisoft.net"  },
      { "daniel.kasmeroglu@.net"                      , null                              },
      { "daniel.kasmeroglu"                           , null                              },
      { ""                                            , null                              },
      { null                                          , null                              },
    };
  }
  
  @Test(dataProvider = "isEmailData")
  public void isEmail( String value, String expected ) {
    assertThat( RegExpressions.isEmail( value ), is( expected ) );
  }

} /* ENDCLASS */
