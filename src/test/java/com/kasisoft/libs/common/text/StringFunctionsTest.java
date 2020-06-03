package com.kasisoft.libs.common.text;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.function.Function;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the class 'StringFunctions'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class StringFunctionsTest {

  @DataProvider(name = "createGetBasename")
  public Object[][] createGetBasename() {
    return new Object[][] {
      { "a/b/c/test"     , "a/b/c/test" },  
      { "a/b/c/test."    , "a/b/c/test" },  
      { "a/b/c/test.txt" , "a/b/c/test" },  
    };
  }

  @Test(dataProvider = "createGetBasename", groups = "all")
  public void getBasename(String name, String expected) {
    assertThat(StringFunctions.getBasename(name), is(expected));
  }

  @DataProvider(name = "createChangeSuffix")
  public Object[][] createChangeSuffix() {
    return new Object[][] {
      { "a/b/c/test"     , "jpg"  , "a/b/c/test.jpg" },  
      { "a/b/c/test."    , "jpg"  , "a/b/c/test.jpg" },  
      { "a/b/c/test.txt" , "jpg"  , "a/b/c/test.jpg" },  
      { "a/b/c/test.txt" , ".jpg" , "a/b/c/test..jpg"},
    };
  }

  @Test(dataProvider = "createChangeSuffix", groups = "all")
  public void changeSuffix(String name, String suffix, String expected) {
    assertThat(StringFunctions.changeSuffix(name, suffix), is(expected));
  }
  
    
  @DataProvider(name = "createCleanup")
  public Object[][] createCleanup() {
    return new Object[][] {
      { null      , null    },
      { ""        , null    },
      { "\t"      , null    },
      { " "       , null    },
      { "\r\t\n"  , null    },
      { "a"       , "a"     },
      { "\ra\n"   , "a"     },
      { " a "     , "a"     },
      { " ab "    , "ab"    },
      { " a c "   , "a c"   },
    };
  }
  
  @Test(dataProvider = "createCleanup", groups = "all")
  public void cleanup(String current, String expected) {
    String result = StringFunctions.cleanup(current);
    assertThat(result, is(expected));
  }
  
  @Test(groups = "all")
  public void firstUp() {
    assertThat(StringFunctions.firstUp("Dummy"), is("Dummy"));
    assertThat(StringFunctions.firstUp("dummy"), is("Dummy"));
  }
  
  @Test(groups = "all")
  public void firstDown() {
    assertThat(StringFunctions.firstDown("Dummy"), is("dummy"));
    assertThat(StringFunctions.firstDown("dummy"), is("dummy"));
  }
  
  @Test(groups = "all")
  public void replaceAll() {
    
    var replacements = new HashMap<String, String>();
    replacements.put( "name"    , "Daniel Kasmeroglu" );
    replacements.put( "company" , "Kasisoft"          );
    
    var value1 = StringFunctions.replaceAll("The pseudo company company is driven by name [company]", replacements);
    assertThat(value1, is( "The pseudo Kasisoft Kasisoft is driven by Daniel Kasmeroglu [Kasisoft]" ) );

    var value2 = StringFunctions.replaceAll("The pseudo company ${company} is driven by ${name} [${company}]", replacements, "${%s}");
    assertThat(value2, is( "The pseudo company Kasisoft is driven by Daniel Kasmeroglu [Kasisoft]" ) );

  }
    
  @Test(groups="all")
  public void camelCase() {
    assertThat(StringFunctions.camelCase(""), is(""));
    assertThat(StringFunctions.camelCase("simple"), is( "simple"));
    assertThat(StringFunctions.camelCase("simpleTon"), is("simpleTon"));
    assertThat(StringFunctions.camelCase("simple_ton"), is("simpleTon"));
    assertThat(StringFunctions.camelCase("simple__ton"), is("simpleTon"));
    assertThat(StringFunctions.camelCase("simple ton"), is("simpleTon"));
    assertThat(StringFunctions.camelCase("Simple-ton"), is("simpleTon"));
  }
  
  @DataProvider(name = "createRegionReplaceSimple")
  private Object[][] createRegionReplaceSimple() {
    return new Object[][] {
      {"this is // my text // without a section // dodo //", "", "this is  without a section "},
      {"this is // // my text // without a section // dodo //", "", "this is  my text  dodo //"},
      {"this is // // my text // without a section // dodo // //", "", "this is  my text  dodo "},
      {"this is // my text // without a section // dodo //", "bibo", "this is bibo without a section bibo"},
      {"this is // // my text // without a section // dodo //", "bibo", "this is bibo my text bibo dodo //"},
      {"this is // // my text // without a section // dodo // //", "bibo", "this is bibo my text bibo dodo bibo"},
    };
  }
  
  @Test(groups = "all", dataProvider = "createRegionReplaceSimple")
  public void regionReplaceSimple(String text, String replacement, String expected) {
    assertThat(StringFunctions.replaceRegions(text, "//", replacement), is( expected ) );
  }

  @DataProvider(name = "createRegionReplaceFunction")
  private Object[][] createRegionReplaceFunction() {
    Map<String, String> mapping = new HashMap<>();
    mapping.put("my text", "changed text");
    mapping.put("dodo", "dodo text");
    Function<String, CharSequence> replacement = $ -> mapping.get($.trim());
    return new Object[][] {
      {"this is // my text // without a section // dodo //", replacement, "this is changed text without a section dodo text"},
      {"this is // // my text // without a section // dodo //", replacement, "this is  my text  dodo //"},
      {"this is // // my text // without a section // dodo // //", replacement, "this is  my text  dodo "},
    };
  }
  
  @Test(groups = "all", dataProvider = "createRegionReplaceFunction")
  public void regionReplaceFunction(String text, Function<String, CharSequence> replacement, String expected) {
    assertThat(StringFunctions.replaceRegions(text, "//", replacement), is(expected));
  }
  
  @DataProvider(name = "createEndsWith")
  public Object[][] createEndsWith() {
    return new Object[][] {
      {"20 Frösche fliegen über den Ozean", new String[] {"20", "Ozean"}, Boolean.TRUE},
      {"20 Frösche fliegen über den Ozean", new String[] {"blau", "den"}, Boolean.FALSE},
    };
  }

    
  @Test(dataProvider = "createEndsWith", groups = "all")
  public void endsWithMany(String text, String[] candidates, boolean contained) {
    if (contained) {
      assertNotNull(StringFunctions.endsWithMany(text, candidates ));
    } else {
      assertNull(StringFunctions.endsWithMany(text, candidates ));
    }
    
  }

  @DataProvider(name = "createStartsWith")
  public Object[][] createStartsWith() {
    return new Object[][] {
      {"20 Frösche fliegen über den Ozean", new String[] {"20", "fliegen"}, Boolean.TRUE},
      {"20 Frösche fliegen über den Ozean", new String[] {"blau", "den"}, Boolean.FALSE},
    };
  }
  
  @Test(dataProvider = "createStartsWith", groups = "all")
  public void startsWithMany(String text, String[] candidates, boolean contained) {
    if (contained) {
      assertNotNull(StringFunctions.startsWithMany(text, candidates ));
    } else {
      assertNull(StringFunctions.startsWithMany(text, candidates ));
    }
    
  }


  @Test(groups="all")
  public void concatenate() {

    // String array
    
    // without delimiter
    assertThat( StringFunctions.concatenate(null), is(""));
    assertThat( StringFunctions.concatenate(null, new String[0]), is(""));
    assertThat( StringFunctions.concatenate(null, "A"), is("A"));
    assertThat( StringFunctions.concatenate(null, "A", "B"), is("AB"));
    assertThat( StringFunctions.concatenate(null, "A", "B", "C"), is("ABC"));
    assertThat( StringFunctions.concatenate(null, "A", null, "C"), is( "AC"));
    assertThat( StringFunctions.concatenate(null, "A", "", "C"), is( "AC"));

    // with delimiter
    assertThat( StringFunctions.concatenate("#"), is(""));
    assertThat( StringFunctions.concatenate("#", new String[0]), is(""));
    assertThat( StringFunctions.concatenate("#", "A"), is("A"));
    assertThat( StringFunctions.concatenate("#", "A", "B"), is("A#B"));
    assertThat( StringFunctions.concatenate("#", "A", "B", "C"), is("A#B#C"));
    assertThat( StringFunctions.concatenate("#", "A", null, "C"), is( "A#C"));
    assertThat( StringFunctions.concatenate("#", "A", "", "C"), is( "A#C"));

    // String collection
    
    // without delimiter
    assertThat( StringFunctions.concatenate(null), is(""));
    assertThat( StringFunctions.concatenate(null, Arrays.asList(new String[0])), is(""));
    assertThat( StringFunctions.concatenate(null, Arrays.asList("A")), is("A"));
    assertThat( StringFunctions.concatenate(null, Arrays.asList("A", "B")), is("AB"));
    assertThat( StringFunctions.concatenate(null, Arrays.asList("A", "B", "C")), is("ABC"));
    assertThat( StringFunctions.concatenate(null, Arrays.asList("A", null, "C") ), is("AC"));
    assertThat( StringFunctions.concatenate(null, Arrays.asList("A", "", "C")), is("AC"));

    // with delimiter
    assertThat( StringFunctions.concatenate("#"), is(""));
    assertThat( StringFunctions.concatenate("#", Arrays.asList(new String[0])), is(""));
    assertThat( StringFunctions.concatenate("#", Arrays.asList("A")), is("A"));
    assertThat( StringFunctions.concatenate("#", Arrays.asList("A", "B")), is( "A#B" ) );
    assertThat( StringFunctions.concatenate("#", Arrays.asList("A", "B", "C")), is("A#B#C"));
    assertThat( StringFunctions.concatenate("#", Arrays.asList("A", null, "C")), is("A#C"));
    assertThat( StringFunctions.concatenate("#", Arrays.asList("A", "", "C")), is("A#C"));

  }
  
  @DataProvider(name="createRepeat")
  public Object[][] createRepeat() {
    return new Object[][] {
      { Integer.valueOf(0) , null , ""   },
      { Integer.valueOf(1) , null , ""   },
      { Integer.valueOf(2) , null , ""   },
      { Integer.valueOf(0) , ""   , ""   },
      { Integer.valueOf(1) , ""   , ""   },
      { Integer.valueOf(2) , ""   , ""   },
      { Integer.valueOf(0) , "A"  , ""   },
      { Integer.valueOf(1) , "A"  , "A"  },
      { Integer.valueOf(2) , "A"  , "AA" },
    };
  }

  @Test(dataProvider = "createRepeat", groups = "all")
  public void repeat(int n, String text, String expected) {
    assertThat(StringFunctions.repeat(n, text), is(expected));
  }

  @Test(groups = "all")
  public void padding() {
    assertThat(StringFunctions.padding(null, 20, ' ', true), is("                    "));
    assertThat(StringFunctions.padding("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 20, ' ', true), is("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    assertThat(StringFunctions.padding("Hello", 20, ' ', true), is("               Hello"));
    assertThat(StringFunctions.padding("Hello", 20, ' ', false), is("Hello               "));
  }

} /* ENDCLASS */
