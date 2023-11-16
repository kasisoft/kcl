package com.kasisoft.libs.common.test.functional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import com.kasisoft.libs.common.functional.*;

import java.util.stream.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class PredicatesTest {

    public static Stream<Arguments> data_isMaven() {
        return Stream.of(Arguments.of("com/sample/Bibo.class", false), Arguments.of("Bibo.class", false), Arguments.of("com/sample/Bibo$1.class", false), Arguments.of("Bibo$1.class", false), Arguments.of("com/sample/Bibo$Sample.class", false), Arguments.of("Bibo$Sample.class", false), Arguments.of("pom.xml", true), Arguments.of("/pom.xml", true), Arguments.of("pom.properties", true), Arguments.of("/pom.properties", true), Arguments.of("", false));
    }

    @ParameterizedTest
    @MethodSource("data_isMaven")
    public void isMaven(String classname, boolean expected) throws Exception {
        assertThat(Predicates.IS_MAVEN_FILE.test(classname), is(expected));
    }

    public static Stream<Arguments> data_isSPIFile() {
        return Stream.of(Arguments.of("META-INF/services/-klddd", false), Arguments.of("META-INF/services/com.Bibo", true), Arguments.of("META-INF/services/com.sample.Bibo", true), Arguments.of("META-INF/services/com.sample.Bibo$1", false), Arguments.of("META-INF/com.Bibo", false), Arguments.of("META-INF/com.sample.Bibo", false), Arguments.of("", false));
    }

    @ParameterizedTest
    @MethodSource("data_isSPIFile")
    public void isSPIFile(String classname, boolean expected) throws Exception {
        assertThat(Predicates.IS_SPI_FILE.test(classname), is(expected));
    }

    public static Stream<Arguments> data_isMagnoliaFile() {
        return Stream.of(Arguments.of("META-INF/magnolia/", false), Arguments.of("META-INF/magnolia/com.Bibo", true), Arguments.of("META-INF/magnolia/com.sample.Bibo", true), Arguments.of("", false));
    }

    @ParameterizedTest
    @MethodSource("data_isMagnoliaFile")
    public void isMagnoliaFile(String classname, boolean expected) throws Exception {
        assertThat(Predicates.IS_MAGNOLIA_FILE.test(classname), is(expected));
    }

    public static Stream<Arguments> data_isJavaFqdn() {
        return Stream.of(Arguments.of("com.sample.Bibo.class", false), Arguments.of("Bibo", true), Arguments.of("com.sample.Bibo$1", true), Arguments.of("Bibo$1", true), Arguments.of("com.sample.Bibo$Sample", true), Arguments.of("Bibo$Sample", true), Arguments.of("com/sample/Bibo", false), Arguments.of("", false));
    }

    @ParameterizedTest
    @MethodSource("data_isJavaFqdn")
    public void isJavaFqdn(String classname, boolean expected) throws Exception {
        assertThat(Predicates.IS_JAVA_FQDN.test(classname), is(expected));
    }

    public static Stream<Arguments> data_isJavaClassFile() {
        return Stream.of(Arguments.of("com/sample/Bibo.class", true), Arguments.of("Bibo.class", true), Arguments.of("com/sample/Bibo$1.class", true), Arguments.of("Bibo$1.class", true), Arguments.of("com/sample/Bibo$Sample.class", true), Arguments.of("Bibo$Sample.class", true), Arguments.of("com.sample.Bibo", false), Arguments.of("Bibo", false), Arguments.of("", false));
    }

    @ParameterizedTest
    @MethodSource("data_isJavaClassFile")
    public void isJavaClassFile(String classname, boolean expected) throws Exception {
        assertThat(Predicates.IS_JAVA_CLASS_FILE.test(classname), is(expected));
    }

    public static Stream<Arguments> data_isInnerJavaClassFile() {
        return Stream.of(Arguments.of("com/sample/Bibo.class", false), Arguments.of("Bibo.class", false), Arguments.of("com/sample/Bibo$1.class", true), Arguments.of("Bibo$1.class", true), Arguments.of("com/sample/Bibo$Sample.class", true), Arguments.of("Bibo$Sample.class", true), Arguments.of("com.sample.Bibo", false), Arguments.of("Bibo", false), Arguments.of("", false));
    }

    @ParameterizedTest
    @MethodSource("data_isInnerJavaClassFile")
    public void isInnerJavaClassFile(String classname, boolean expected) throws Exception {
        assertThat(Predicates.IS_INNER_JAVA_CLASS_FILE.test(classname), is(expected));
    }

    public static Stream<Arguments> data_isEnclosingJavaClassFile() {
        return Stream.of(Arguments.of("com/sample/Bibo.class", true), Arguments.of("Bibo.class", true), Arguments.of("com/sample/Bibo$1.class", false), Arguments.of("Bibo$1.class", false), Arguments.of("com/sample/Bibo$Sample.class", false), Arguments.of("Bibo$Sample.class", false), Arguments.of("com.sample.Bibo", false), Arguments.of("Bibo", false), Arguments.of("", false));
    }

    @ParameterizedTest
    @MethodSource("data_isEnclosingJavaClassFile")
    public void isEnclosingJavaClassFile(String classname, boolean expected) throws Exception {
        assertThat(Predicates.IS_ENCLOSING_JAVA_CLASS_FILE.test(classname), is(expected));
    }

    @ParameterizedTest
    @MethodSource("data_isEnclosingJavaClassFile")
    public void acceptAll(String classname, boolean ignore) {
        assertTrue(Predicates.acceptAll().test(classname));
    }

    @ParameterizedTest
    @MethodSource("data_isEnclosingJavaClassFile")
    public void acceptNone(String classname, boolean ignore) {
        assertFalse(Predicates.acceptNone().test(classname));
    }

} /* ENDCLASS */
