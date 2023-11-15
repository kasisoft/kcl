package com.kasisoft.libs.common.test.functional;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.functional.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.awt.event.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FunctionsTest {

    @Test
    public static @NotNull void adaptConsumerToActionListener() {
        var builder  = new StringBuilder();
        var listener = Functions.adaptConsumerToActionListener($ -> builder.append($.getActionCommand()));
        var event    = new ActionEvent(new Object(), 1, "ADAPT_TO_ACTION_LISTENER");
        listener.actionPerformed(event);
        assertThat(builder.toString(), is("ADAPT_TO_ACTION_LISTENER"));
    }

    @Test
    public static void nullSafeToFunction() {
        Function<String, Integer> toLength         = $ -> $.length();
        Function<String, Integer> nullSafeToLength = Functions.nullSafeToFunction(toLength);
        assertThat(nullSafeToLength.apply("Hallo"), is(5));
        assertNull(nullSafeToLength.apply(null));
    }

    @Test
    public void adaptConsumerToTriConsumer() {

        var builder     = new StringBuilder();
        var triConsumer = Functions.adaptConsumerToTriConsumer($ -> builder.append($));

        triConsumer.accept("Hello", "Bibo", "Suppe");
        assertThat(builder.toString(), is("Hello"));

        builder.setLength(0);
        triConsumer.accept(null, "Bibo", "Suppe");
        assertThat(builder.toString(), is("null"));

    }

    @Test
    public void adaptBiConsumerToTriConsumer() {

        var builder     = new StringBuilder();
        var triConsumer = Functions.<String, String, String> adaptBiConsumerToTriConsumer(($a, $b) -> builder.append($a).append($b));

        triConsumer.accept("Hello", "Bibo", "Suppe");
        assertThat(builder.toString(), is("HelloBibo"));

        builder.setLength(0);
        triConsumer.accept(null, "Bibo", "Suppe");
        assertThat(builder.toString(), is("nullBibo"));

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
        var biConsumer = Functions.adaptConsumerToBiConsumer($ -> builder.append($));

        biConsumer.accept("Hello", "Bibo");

        assertThat(builder.toString(), is("Hello"));

        builder.setLength(0);
        biConsumer.accept(null, "Bibo");
        assertThat(builder.toString(), is("null"));

    }

    @Test
    public void adaptTriConsumerToBiConsumer() {
        var builder     = new StringBuilder();
        var triConsumer = Functions.adaptTriConsumerToBiConsumer(($a, $b, $_2) -> builder.append($a).append($b));
        triConsumer.accept("Hello", "Bibo");
        assertThat(builder.toString(), is("HelloBibo"));
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

    @Test
    public void adaptTriConsumerToConsumer() {
        var builder  = new StringBuilder();
        var consumer = Functions.adaptTriConsumerToConsumer(($a, $_1, $_2) -> builder.append($a));
        consumer.accept("Hello");
        assertThat(builder.toString(), is("Hello"));
    }

} /* ENDCLASS */
