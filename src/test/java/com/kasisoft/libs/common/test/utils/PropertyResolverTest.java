package com.kasisoft.libs.common.test.utils;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.utils.*;

import com.kasisoft.libs.common.test.*;

import com.kasisoft.libs.common.io.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class PropertyResolverTest {

    private static final TestResources TEST_RESOURCES = TestResources.createTestResources(PropertyResolverTest.class);

    @Test
    public void apply__env() {

        var text        = TEST_RESOURCES.getResource("text_01.txt");
        var loaded      = IoSupportFunctions.readText(text);

        var resolver    = new PropertyResolver().withEnvironment();

        var substituted = resolver.apply(loaded);
        assertThat(substituted, is("My username is: %s".formatted(System.getenv().get("USER"))));

    }

    @Test
    public void apply__sys() {

        var text        = TEST_RESOURCES.getResource("text_02.txt");
        var loaded      = IoSupportFunctions.readText(text);

        var resolver    = new PropertyResolver().withSysProperties();

        var substituted = resolver.apply(loaded).trim();
        assertThat(substituted, is("My encoding is: %s".formatted(System.getProperty("file.encoding"))));

    }

    @Test
    public void apply__envAndPrefix() {

        var text        = TEST_RESOURCES.getResource("text_03.txt");
        var loaded      = IoSupportFunctions.readText(text);

        var resolver    = new PropertyResolver().withEnvironment("bibo");

        var substituted = resolver.apply(loaded);
        assertThat(substituted, is("My username is: %s".formatted(System.getenv().get("USER"))));

    }

    @Test
    public void apply__sysAndPrefix() {

        var text        = TEST_RESOURCES.getResource("text_04.txt");
        var loaded      = IoSupportFunctions.readText(text);

        var resolver    = new PropertyResolver().withSysProperties("frog");

        var substituted = resolver.apply(loaded).trim();
        assertThat(substituted, is("My encoding is: %s".formatted(System.getProperty("file.encoding"))));

    }

    @Test
    public void apply__custom() {

        var text   = TEST_RESOURCES.getResource("text_05.txt");
        var loaded = IoSupportFunctions.readText(text);

        var map    = new HashMap<String, String>();
        map.put("val", "dodo");

        var resolver    = new PropertyResolver().withMap(map);

        var substituted = resolver.apply(loaded).trim();
        assertThat(substituted, is("Here is my value dodo. What ${fluffy}?"));

    }

    @Test
    public void apply__envAndCustomVarFormat() {

        var text        = TEST_RESOURCES.getResource("text_06.txt");
        var loaded      = IoSupportFunctions.readText(text);

        var resolver    = new PropertyResolver().withVarFormat("#{%s}").withEnvironment();

        var substituted = resolver.apply(loaded);
        assertThat(substituted, is("My username is: %s".formatted(System.getenv().get("USER"))));

    }

    @Test
    public void apply__sysAndCustomVarFormat() {

        var text        = TEST_RESOURCES.getResource("text_07.txt");
        var loaded      = IoSupportFunctions.readText(text);

        var resolver    = new PropertyResolver().withVarFormat("#{%s}").withSysProperties();

        var substituted = resolver.apply(loaded).trim();
        assertThat(substituted, is("My encoding is: %s".formatted(System.getProperty("file.encoding"))));

    }

    @Test
    public void apply__envAndPrefixAndCustomVarFormat() {

        var text        = TEST_RESOURCES.getResource("text_08.txt");
        var loaded      = IoSupportFunctions.readText(text);

        var resolver    = new PropertyResolver().withVarFormat("#{%s}").withEnvironment("bibo");

        var substituted = resolver.apply(loaded);
        assertThat(substituted, is("My username is: %s".formatted(System.getenv().get("USER"))));

    }

    @Test
    public void apply__sysAndPrefixAndCustomVarFormat() {

        var text        = TEST_RESOURCES.getResource("text_09.txt");
        var loaded      = IoSupportFunctions.readText(text);

        var resolver    = new PropertyResolver().withVarFormat("#{%s}").withSysProperties("frog");

        var substituted = resolver.apply(loaded).trim();
        assertThat(substituted, is("My encoding is: %s".formatted(System.getProperty("file.encoding"))));

    }

    @Test
    public void apply__customAndCustomVarFormat() {

        var text   = TEST_RESOURCES.getResource("text_10.txt");
        var loaded = IoSupportFunctions.readText(text);

        var map    = new HashMap<String, String>();
        map.put("val", "dodo");

        var resolver    = new PropertyResolver().withVarFormat("#{%s}").withMap(map);

        var substituted = resolver.apply(loaded).trim();
        assertThat(substituted, is("Here is my value dodo. What #{fluffy}?"));

    }

    @Test
    public void apply__all() {

        var text   = TEST_RESOURCES.getResource("text_11.txt");
        var loaded = IoSupportFunctions.readText(text);

        var map    = new HashMap<String, String>();
        map.put("val", "dodo");

        var resolver    = new PropertyResolver().withEnvironment().withSysProperties().withMap(map);

        var substituted = resolver.apply(loaded).trim();
        assertThat(substituted, is("My username is: %s, encoding=%s, value=%s, unreplaced=${dodo}".formatted(System.getenv().get("USER"), System.getProperty("file.encoding"), map.get("val"))));

    }

    @Test
    public void apply__allAndCustomVarFormat() {

        var text   = TEST_RESOURCES.getResource("text_12.txt");
        var loaded = IoSupportFunctions.readText(text);

        var map    = new HashMap<String, String>();
        map.put("val", "dodo");

        var resolver    = new PropertyResolver().withVarFormat("#{%s}").withEnvironment().withSysProperties().withMap(map);

        var substituted = resolver.apply(loaded).trim();
        assertThat(substituted, is("My username is: %s, encoding=%s, value=%s, unreplaced=#{dodo}".formatted(System.getenv().get("USER"), System.getProperty("file.encoding"), map.get("val"))));

    }

} /* ENDCLASS */
