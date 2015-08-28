package com.kasisoft.libs.common.function;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * Tests for 'Predicates'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PredicatesTest {

  @DataProvider(name="isJavaClassFile")
  public Object[][] isJavaClassFileData() {
    return new Object[][] {
      { "com/sample/Bibo.class"           , true  },
      { "Bibo.class"                      , true  },
      { "com/sample/Bibo$1.class"         , true  },
      { "Bibo$1.class"                    , true  },
      { "com/sample/Bibo$Sample.class"    , true  },
      { "Bibo$Sample.class"               , true  },
      { "com.sample.Bibo"                 , false },
      { "Bibo"                            , false },
      { ""                                , false },
    };
  }
  
  @Test(dataProvider = "isJavaClassFile")
  public void isJavaClassFile( String classname, boolean expected ) {
    assertThat( Predicates.IS_JAVA_CLASS_FILE.test( classname ), is( expected ) );
  }
  
  @DataProvider(name="isInnerJavaClassFile")
  public Object[][] isInnerJavaClassFileData() {
    return new Object[][] {
      { "com/sample/Bibo.class"           , false },
      { "Bibo.class"                      , false },
      { "com/sample/Bibo$1.class"         , true  },
      { "Bibo$1.class"                    , true  },
      { "com/sample/Bibo$Sample.class"    , true  },
      { "Bibo$Sample.class"               , true  },
      { "com.sample.Bibo"                 , false },
      { "Bibo"                            , false },
      { ""                                , false },
    };
  }
  
  @Test(dataProvider = "isInnerJavaClassFile")
  public void isInnerJavaClassFile( String classname, boolean expected ) {
    assertThat( Predicates.IS_INNER_JAVA_CLASS_FILE.test( classname ), is( expected ) );
  }

  @DataProvider(name="isEnclosingJavaClassFile")
  public Object[][] isEnclosingJavaClassFileData() {
    return new Object[][] {
      { "com/sample/Bibo.class"           , true  },
      { "Bibo.class"                      , true  },
      { "com/sample/Bibo$1.class"         , false },
      { "Bibo$1.class"                    , false },
      { "com/sample/Bibo$Sample.class"    , false },
      { "Bibo$Sample.class"               , false },
      { "com.sample.Bibo"                 , false },
      { "Bibo"                            , false },
      { ""                                , false },
    };
  }
  
  @Test(dataProvider = "isEnclosingJavaClassFile")
  public void isEnclosingJavaClassFile( String classname, boolean expected ) {
    assertThat( Predicates.IS_ENCLOSING_JAVA_CLASS_FILE.test( classname ), is( expected ) );
  }
  
}
