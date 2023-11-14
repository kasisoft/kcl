package com.kasisoft.libs.common.test.comparator;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.comparator.*;
import com.kasisoft.libs.common.utils.*;

import org.junit.jupiter.api.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ComparatorsTest {

    @Test
    public void classByName() {

        var types = new ArrayList<Class<?>>(Arrays.asList(Type1.class, Type2.class, Type3.class, Type4.class, com.kasisoft.libs.common.test.comparator.subpackage.Type2.class));
        Collections.shuffle(types);

        Collections.sort(types, Comparators.byClassName(true));

        var asArray = types.toArray(new Class<?>[types.size()]);
        assertThat(asArray, is(new Class<?>[] {Type1.class, Type2.class, Type3.class, Type4.class,
            com.kasisoft.libs.common.test.comparator.subpackage.Type2.class}));

    }

    @Test
    public void classByName__CaseInsensitive() {

        var types = new ArrayList<Class<?>>(Arrays.asList(Type1.class, Type2.class, Type3.class, Type4.class, com.kasisoft.libs.common.test.comparator.subpackage.Type2.class));
        Collections.shuffle(types);

        Collections.sort(types, Comparators.byClassName(false));

        var asArray = types.toArray(new Class<?>[types.size()]);
        assertThat(asArray, is(new Class<?>[] {com.kasisoft.libs.common.test.comparator.subpackage.Type2.class,
            Type1.class, Type2.class, Type3.class, Type4.class}));

    }

    @Test
    public void classBySimpleName() {

        var types = new ArrayList<Class<?>>(Arrays.asList(Type1.class, Type2.class, Type3.class, Type4.class, com.kasisoft.libs.common.test.comparator.subpackage.Type2.class));
        Collections.shuffle(types);

        Collections.sort(types, Comparators.byClassSimpleName(true));

        var asArray = types.toArray(new Class<?>[types.size()]);
        assertThat(asArray, is(new Class<?>[] {Type1.class, Type2.class,
            com.kasisoft.libs.common.test.comparator.subpackage.Type2.class, Type3.class, Type4.class}));

    }

    @Test
    public void classBySimpleName__CaseInsensitive() {

        var types = new ArrayList<Class<?>>(Arrays.asList(Type1.class, Type2.class, Type3.class, Type4.class, com.kasisoft.libs.common.test.comparator.subpackage.Type2.class));
        Collections.shuffle(types);

        Collections.sort(types, Comparators.byClassSimpleName(false));

        var asArray = types.toArray(new Class<?>[types.size()]);
        assertThat(asArray, is(new Class<?>[] {Type1.class,
            com.kasisoft.libs.common.test.comparator.subpackage.Type2.class, Type2.class, Type3.class, Type4.class}));

    }

    @Test
    public void classByPrio() {

        var types = new ArrayList<Class<?>>(Arrays.asList(Type1.class, Type2.class, Type3.class, Type4.class, com.kasisoft.libs.common.test.comparator.subpackage.Type2.class));
        Collections.shuffle(types);

        Collections.sort(types, Comparators.byClassPrio());

        var asArray = types.toArray(new Class<?>[types.size()]);
        assertThat(asArray, is(new Class<?>[] {Type3.class, Type2.class, Type1.class,
            com.kasisoft.libs.common.test.comparator.subpackage.Type2.class, Type4.class}));

    }

    @Test
    public void nullSafeIntegers() {

        // create random integer values with some nulls in it
        var nullCount = 50;
        var random    = new Random();
        var data      = new ArrayList<Integer>();
        var nulls     = 0;
        while (nulls < nullCount) {
            var irand = random.nextInt(10000);
            if (irand % 3 == 0) {
                data.add(null);
                nulls++;
            } else {
                data.add(Integer.valueOf(irand));
            }
        }

        Collections.sort(data, Comparators.byInteger());

        var size = data.size();
        // remove the null values, the remaining ones must be sorted
        MiscFunctions.trim(data);

        assertThat(data.size(), is(size - nullCount));
        for (int i = 1; i < data.size(); i++) {
            assertTrue(data.get(i - 1).intValue() <= data.get(i).intValue());
        }

    }

} /* ENDCLASS */
