package com.kasisoft.libs.common.functional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PredicatesTest {

  @DataProvider(name = "data_isMaven")
  public Object[][] data_isMaven() {
    return new Object[][] {
      {"com/sample/Bibo.class"       , false},
      {"Bibo.class"                  , false},
      {"com/sample/Bibo$1.class"     , false},
      {"Bibo$1.class"                , false},
      {"com/sample/Bibo$Sample.class", false},
      {"Bibo$Sample.class"           , false},
      {"pom.xml"                     , true },
      {"/pom.xml"                    , true },
      {"pom.properties"              , true },
      {"/pom.properties"             , true },
      {""                            , false},
    };
  }
  
  @Test(dataProvider = "data_isMaven", groups = "all")
  public void isMaven(String classname, boolean expected) {
    assertThat(Predicates.IS_MAVEN_FILE.test(classname), is(expected));
  }
  
  @DataProvider(name = "data_isSPIFile")
  public Object[][] data_isSPIFile() {
    return new Object[][] {
      {"META-INF/services/-klddd"           , false},
      {"META-INF/services/com.Bibo"         , true },
      {"META-INF/services/com.sample.Bibo"  , true },
      {"META-INF/services/com.sample.Bibo$1", false},
      {"META-INF/com.Bibo"                  , false},
      {"META-INF/com.sample.Bibo"           , false},
      {""                                   , false},
    };
  }
  
  @Test(dataProvider = "data_isSPIFile", groups = "all")
  public void isSPIFile(String classname, boolean expected) {
    assertThat(Predicates.IS_SPI_FILE.test(classname), is(expected));
  }

  @DataProvider(name = "data_isMagnoliaFile")
  public Object[][] data_isMagnoliaFile() {
    return new Object[][] {
      {"META-INF/magnolia/"               , false},
      {"META-INF/magnolia/com.Bibo"       , true },
      {"META-INF/magnolia/com.sample.Bibo", true },
      {""                                 , false},
    };
  }
  
  @Test(dataProvider = "data_isMagnoliaFile", groups = "all")
  public void isMagnoliaFile(String classname, boolean expected) {
    assertThat(Predicates.IS_MAGNOLIA_FILE.test(classname), is(expected));
  }

  @DataProvider(name = "data_isJavaFqdn")
  public Object[][] data_isJavaFqdn() {
    return new Object[][] {
      {"com.sample.Bibo.class"           , false},
      {"Bibo"                            , true },
      {"com.sample.Bibo$1"               , true },
      {"Bibo$1"                          , true },
      {"com.sample.Bibo$Sample"          , true },
      {"Bibo$Sample"                     , true },
      {"com/sample/Bibo"                 , false},
      {""                                , false},
    };
  }
  
  @Test(dataProvider = "data_isJavaFqdn", groups = "all")
  public void isJavaFqdn(String classname, boolean expected) {
    assertThat(Predicates.IS_JAVA_FQDN.test(classname), is(expected));
  }
  
  @DataProvider(name = "data_isJavaClassFile")
  public Object[][] data_isJavaClassFile() {
    return new Object[][] {
      {"com/sample/Bibo.class"           , true },
      {"Bibo.class"                      , true },
      {"com/sample/Bibo$1.class"         , true },
      {"Bibo$1.class"                    , true },
      {"com/sample/Bibo$Sample.class"    , true },
      {"Bibo$Sample.class"               , true },
      {"com.sample.Bibo"                 , false},
      {"Bibo"                            , false},
      {""                                , false},
    };
  }
  
  @Test(dataProvider = "data_isJavaClassFile", groups = "all")
  public void isJavaClassFile(String classname, boolean expected) {
    assertThat(Predicates.IS_JAVA_CLASS_FILE.test(classname), is(expected));
  }
  
  @DataProvider(name = "data_isInnerJavaClassFile")
  public Object[][] data_isInnerJavaClassFile() {
    return new Object[][] {
      {"com/sample/Bibo.class"           , false},
      {"Bibo.class"                      , false},
      {"com/sample/Bibo$1.class"         , true },
      {"Bibo$1.class"                    , true },
      {"com/sample/Bibo$Sample.class"    , true },
      {"Bibo$Sample.class"               , true },
      {"com.sample.Bibo"                 , false},
      {"Bibo"                            , false},
      {""                                , false},
    };
  }
  
  @Test(dataProvider = "data_isInnerJavaClassFile", groups = "all")
  public void isInnerJavaClassFile(String classname, boolean expected) {
    assertThat(Predicates.IS_INNER_JAVA_CLASS_FILE.test(classname), is(expected));
  }

  @DataProvider(name = "data_isEnclosingJavaClassFile")
  public Object[][] data_isEnclosingJavaClassFile() {
    return new Object[][] {
      {"com/sample/Bibo.class"           , true },
      {"Bibo.class"                      , true },
      {"com/sample/Bibo$1.class"         , false},
      {"Bibo$1.class"                    , false},
      {"com/sample/Bibo$Sample.class"    , false},
      {"Bibo$Sample.class"               , false},
      {"com.sample.Bibo"                 , false},
      {"Bibo"                            , false},
      {""                                , false},
    };
  }
  
  @Test(dataProvider = "data_isEnclosingJavaClassFile", groups = "all")
  public void isEnclosingJavaClassFile(String classname, boolean expected) {
    assertThat(Predicates.IS_ENCLOSING_JAVA_CLASS_FILE.test(classname), is(expected));
  }

  @SuppressWarnings("unused")
  @Test(dataProvider = "data_isEnclosingJavaClassFile", groups = "all")
  public void acceptAll(String classname, boolean ignore) {
    assertTrue(Predicates.acceptAll().test(classname));
  }

  @SuppressWarnings("unused")
  @Test(dataProvider = "data_isEnclosingJavaClassFile", groups = "all")
  public void acceptNone(String classname, boolean ignore) {
    assertFalse(Predicates.acceptNone().test(classname));
  }

} /* ENDCLASS */
