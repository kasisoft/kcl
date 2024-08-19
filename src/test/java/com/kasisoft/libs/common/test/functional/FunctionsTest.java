package com.kasisoft.libs.common.test.functional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.functional.*;

import java.awt.event.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class FunctionsTest {

    @Test
    public static void adaptConsumerToActionListener() {
        var builder  = new StringBuilder();
        var listener = Functions.adaptConsumerToActionListener($ -> builder.append($.getActionCommand()));
        var event    = new ActionEvent(new Object(), 1, "ADAPT_TO_ACTION_LISTENER");
        listener.actionPerformed(event);
        assertThat(builder.toString(), is("ADAPT_TO_ACTION_LISTENER"));
    }

    @Test
    public static void nullSafeToFunction() {
        var nullSafeToLength = Functions.nullSafeToFunction(String::length);
        assertThat(nullSafeToLength.apply("Hallo"), is(5));
        assertNull(nullSafeToLength.apply(null));
    }

    @Test
    public void adaptFunctionToBiFunction() {
        var biFunction = Functions.adaptFunctionToBiFunction($ -> $);
        assertThat(biFunction.apply("Hello", "Bibo"), is("Hello"));
        assertNull(biFunction.apply(null, "Bibo"));
    }

    @Test
    public void adaptConsumerToBiConsumer() {

        var builder    = new StringBuilder();
        var biConsumer = Functions.adaptConsumerToBiConsumer(builder::append);

        biConsumer.accept("Hello", "Bibo");

        assertThat(builder.toString(), is("Hello"));

        builder.setLength(0);
        biConsumer.accept(null, "Bibo");
        assertThat(builder.toString(), is("null"));

    }

    @Test
    public void adaptBiFunctionToFunction() {
        var function = Functions.adaptBiFunctionToFunction(($a, $_) -> $a);
        assertThat(function.apply("Hello"), is("Hello"));
    }

    @Test
    public void adaptBiConsumerToConsumer() {
        var builder  = new StringBuilder();
        var consumer = Functions.adaptBiConsumerToConsumer(($a, $_) -> builder.append($a));
        consumer.accept("Hello");
        assertThat(builder.toString(), is("Hello"));
    }

} /* ENDCLASS */
