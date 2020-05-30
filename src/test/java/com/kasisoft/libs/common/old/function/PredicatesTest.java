package com.kasisoft.libs.common.old.function;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * Tests for 'Predicates'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PredicatesTest {

  @DataProvider(name="isMaven")
  public Object[][] isMavenData() {
    return new Object[][] {
      { "com/sample/Bibo.class"           , false  },
      { "Bibo.class"                      , false  },
      { "com/sample/Bibo$1.class"         , false  },
      { "Bibo$1.class"                    , false  },
      { "com/sample/Bibo$Sample.class"    , false  },
      { "Bibo$Sample.class"               , false  },
      { "pom.xml"                         , true   },
      { "/pom.xml"                        , true   },
      { "pom.properties"                  , true   },
      { "/pom.properties"                 , true   },
      { ""                                , false  },
    };
  }
  
  @Test(dataProvider = "isMaven", groups = "all")
  public void isMaven( String classname, boolean expected ) {
    assertThat( Predicates.IS_MAVEN_FILE.test( classname ), is( expected ) );
  }
  
  @DataProvider(name="isSPIFile")
  public Object[][] isSPIFileData() {
    return new Object[][] {
      { "META-INF/services/-klddd"            , false  },
      { "META-INF/services/com.Bibo"          , true   },
      { "META-INF/services/com.sample.Bibo"   , true   },
      { "META-INF/services/com.sample.Bibo$1" , false  },
      { "META-INF/com.Bibo"                   , false  },
      { "META-INF/com.sample.Bibo"            , false  },
      { ""                                    , false  },
    };
  }
  
  @Test(dataProvider = "isSPIFile", groups = "all")
  public void isSPIFile( String classname, boolean expected ) {
    assertThat( Predicates.IS_SPI_FILE.test( classname ), is( expected ) );
  }

  @DataProvider(name="isMagnoliaFile")
  public Object[][] isMagnoliaFileData() {
    return new Object[][] {
      { "META-INF/magnolia/"                  , false  },
      { "META-INF/magnolia/com.Bibo"          , true   },
      { "META-INF/magnolia/com.sample.Bibo"   , true   },
      { ""                                    , false  },
    };
  }
  
  @Test(dataProvider = "isMagnoliaFile", groups = "all")
  public void isMagnoliaFile( String classname, boolean expected ) {
    assertThat( Predicates.IS_MAGNOLIA_FILE.test( classname ), is( expected ) );
  }

  @DataProvider(name="isJavaFqdn")
  public Object[][] isJavaFqdnData() {
    return new Object[][] {
      { "com.sample.Bibo.class"           , false },
      { "Bibo"                            , true  },
      { "com.sample.Bibo$1"               , true  },
      { "Bibo$1"                          , true  },
      { "com.sample.Bibo$Sample"          , true  },
      { "Bibo$Sample"                     , true  },
      { "com/sample/Bibo"                 , false },
      { ""                                , false },
    };
  }
  
  @Test(dataProvider = "isJavaFqdn", groups = "all")
  public void isJavaFqdn( String classname, boolean expected ) {
    assertThat( Predicates.IS_JAVA_FQDN.test( classname ), is( expected ) );
  }
  
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
  
  @Test(dataProvider = "isJavaClassFile", groups = "all")
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
  
  @Test(dataProvider = "isInnerJavaClassFile", groups = "all")
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
  
  @Test(dataProvider = "isEnclosingJavaClassFile", groups = "all")
  public void isEnclosingJavaClassFile( String classname, boolean expected ) {
    assertThat( Predicates.IS_ENCLOSING_JAVA_CLASS_FILE.test( classname ), is( expected ) );
  }

  @SuppressWarnings("unused")
  @Test(dataProvider = "isEnclosingJavaClassFile", groups = "all")
  public void acceptAll( String classname, boolean ignore ) {
    assertThat( Predicates.acceptAll().test( classname ), is( true ) );
  }

  @SuppressWarnings("unused")
  @Test(dataProvider = "isEnclosingJavaClassFile", groups = "all")
  public void acceptNone( String classname, boolean ignore ) {
    assertThat( Predicates.acceptNone().test( classname ), is( false ) );
  }

}
