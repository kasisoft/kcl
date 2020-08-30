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
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class StringFunctionsTest {

  @DataProvider(name = "data_removeSuffix")
  public Object[][] data_removeSuffix() {
    return new Object[][] {
      {"a/b/c/test"    , "a/b/c/test"},
      {"a/b/c/test."   , "a/b/c/test"},
      {"a/b/c/test.txt", "a/b/c/test"},
    };
  }

  @Test(dataProvider = "data_removeSuffix", groups = "all")
  public void removeSuffix(String name, String expected) {
    assertThat(StringFunctions.removeSuffix(name), is(expected));
  }

  @DataProvider(name = "data_changeSuffix")
  public Object[][] data_changeSuffix() {
    return new Object[][] {
      {"a/b/c/test"    , "jpg" , "a/b/c/test.jpg"},
      {"a/b/c/test."   , "jpg" , "a/b/c/test.jpg"},
      {"a/b/c/test.txt", "jpg" , "a/b/c/test.jpg"},
      {"a/b/c/test.txt", ".jpg" , "a/b/c/test..jpg"},
    };
  }

  @Test(dataProvider = "data_changeSuffix", groups = "all")
  public void changeSuffix(String name, String suffix, String expected) {
    assertThat(StringFunctions.changeSuffix(name, suffix), is(expected));
  }
  
    
  @DataProvider(name = "data_cleanup")
  public Object[][] data_cleanup() {
    return new Object[][] {
      {null    , null},
      {""      , null},
      {"\t"    , null},
      {" "     , null},
      {"\r\t\n", null},
      {"a"     , "a"},
      {"\ra\n" , "a"},
      {" a "   , "a"},
      { " ab " , "ab"},
      {" a c " , "a c"},
    };
  }
  
  @Test(dataProvider = "data_cleanup", groups = "all")
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
    replacements.put("name", "Daniel Kasmeroglu");
    replacements.put("company", "Kasisoft");
    
    var value1 = StringFunctions.replaceAll("The pseudo company company is driven by name [company]", replacements);
    assertThat(value1, is("The pseudo Kasisoft Kasisoft is driven by Daniel Kasmeroglu [Kasisoft]"));

    var value2 = StringFunctions.replaceAll("The pseudo company ${company} is driven by ${name} [${company}]", replacements, "${%s}");
    assertThat(value2, is("The pseudo company Kasisoft is driven by Daniel Kasmeroglu [Kasisoft]"));

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
  
  @DataProvider(name = "data_regionReplaceSimple")
  public Object[][] data_regionReplaceSimple() {
    return new Object[][] {
      {"this is // my text // without a section // dodo //", "", "this is  without a section "},
      {"this is // // my text // without a section // dodo //", "", "this is  my text  dodo //"},
      {"this is // // my text // without a section // dodo // //", "", "this is  my text  dodo "},
      {"this is // my text // without a section // dodo //", "bibo", "this is bibo without a section bibo"},
      {"this is // // my text // without a section // dodo //", "bibo", "this is bibo my text bibo dodo //"},
      {"this is // // my text // without a section // dodo // //", "bibo", "this is bibo my text bibo dodo bibo"},
    };
  }
  
  @Test(groups = "all", dataProvider = "data_regionReplaceSimple")
  public void regionReplaceSimple(String text, String replacement, String expected) {
    assertThat(StringFunctions.replaceRegions(text, "//", replacement), is( expected ) );
  }

  @DataProvider(name = "data_regionReplaceFunction")
  public Object[][] data_regionReplaceFunction() {
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
  
  @Test(groups = "all", dataProvider = "data_regionReplaceFunction")
  public void regionReplaceFunction(String text, Function<String, CharSequence> replacement, String expected) {
    assertThat(StringFunctions.replaceRegions(text, "//", replacement), is(expected));
  }
  
  @DataProvider(name = "data_endsWithMany")
  public Object[][] data_endsWithMany() {
    return new Object[][] {
      {"20 Frösche fliegen über den Ozean", new String[] {"20", "Ozean"}, Boolean.TRUE},
      {"20 Frösche fliegen über den Ozean", new String[] {"blau", "den"}, Boolean.FALSE},
    };
  }

    
  @Test(dataProvider = "data_endsWithMany", groups = "all")
  public void endsWithMany(String text, String[] candidates, boolean contained) {
    if (contained) {
      assertNotNull(StringFunctions.endsWithMany(text, candidates ));
    } else {
      assertNull(StringFunctions.endsWithMany(text, candidates ));
    }
    
  }

  @DataProvider(name = "data_startsWithMany")
  public Object[][] data_startsWithMany() {
    return new Object[][] {
      {"20 Frösche fliegen über den Ozean", new String[] {"20", "fliegen"}, Boolean.TRUE},
      {"20 Frösche fliegen über den Ozean", new String[] {"blau", "den"}, Boolean.FALSE},
    };
  }
  
  @Test(dataProvider = "data_startsWithMany", groups = "all")
  public void startsWithMany(String text, String[] candidates, boolean contained) {
    if (contained) {
      assertNotNull(StringFunctions.startsWithMany(text, candidates ));
    } else {
      assertNull(StringFunctions.startsWithMany(text, candidates ));
    }
  }

  @Test(groups = "all")
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
  
  @DataProvider(name="data_repeat")
  public Object[][] data_repeat() {
    return new Object[][] {
      {Integer.valueOf(0), null, ""},
      {Integer.valueOf(1), null, ""},
      {Integer.valueOf(2), null, ""},
      {Integer.valueOf(0), "", ""},
      {Integer.valueOf(1), "", ""},
      {Integer.valueOf(2), "", ""},
      {Integer.valueOf(0), "A", ""},
      {Integer.valueOf(1), "A", "A"},
      {Integer.valueOf(2), "A", "AA"},
    };
  }

  @Test(dataProvider = "data_repeat", groups = "all")
  public void repeat(int n, String text, String expected) {
    assertThat(StringFunctions.repeat(n, text), is(expected));
  }

  @Test(groups = "all")
  public void padding__WithChar() {
    assertThat(StringFunctions.padding(null, 20, ' ', true), is("                    "));
    assertThat(StringFunctions.padding("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 20, ' ', true), is("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    assertThat(StringFunctions.padding("Hello", 20, ' ', true), is("               Hello"));
    assertThat(StringFunctions.padding("Hello", 20, ' ', false), is("Hello               "));
  }

  @Test(groups = "all")
  public void padding() {
    assertThat(StringFunctions.padding(null, 20, true), is("                    "));
    assertThat(StringFunctions.padding("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 20, true), is("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    assertThat(StringFunctions.padding("Hello", 20, true), is("               Hello"));
    assertThat(StringFunctions.padding("Hello", 20, false), is("Hello               "));
  }
  
  @DataProvider(name = "data_objectToString")
  public Object[][] data_objectToString() {
    return new Object[][] {
      
      {null, "null"},
      
      {"", ""},
      {"Hello", "Hello"},
      
      {false, "false"},
      {true, "true"},
      {'a', "a"},
      {(byte) 12, "12"},
      {(short) 13, "13"},
      {(int) 14, "14"},
      {(long) 15, "15"},
      {(float) 16.1, "16.1"},
      {(double) 17.2, "17.2"},
      
      {new String[] {"Hello", "Buffalo"}, "[Hello,Buffalo]"},
      {new boolean[] {true, false}, "[true,false]"},
      {new char[] {'c', 'k', 'l'}, "['c','k','l']"},
      {new byte[] {13, 18, 19}, "[(byte)13,(byte)18,(byte)19]"},
      {new short[] {23, 28, 29}, "[(short)23,(short)28,(short)29]"},
      {new int[] {33, 38, 39}, "[33,38,39]"},
      {new long[] {43, 48, 49}, "[43l,48l,49l]"},
      {new float[] {53.1f, 58.2f, 59.3f}, "[53.1f,58.2f,59.3f]"},
      {new double[] {63.1, 68.2, 69.3}, "[63.1,68.2,69.3]"},
      
    };
  }

  @Test(groups = "all", dataProvider = "data_objectToString")
  public static void objectToString(Object obj, String expected) {
    assertThat(StringFunctions.objectToString(obj), is(expected));
  }
  
} /* ENDCLASS */
