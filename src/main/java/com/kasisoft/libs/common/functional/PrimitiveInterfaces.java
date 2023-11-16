package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public interface PrimitiveInterfaces {

    @FunctionalInterface
    public interface KSupplierBoolean {

        boolean get() throws Exception;

        @NotNull
        default KSupplierBoolean protect() {
            return () -> {
                try {
                    return get();
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierChar {

        char get() throws Exception;

        @NotNull
        default KSupplierChar protect() {
            return () -> {
                try {
                    return get();
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierByte {

        byte get() throws Exception;

        @NotNull
        default KSupplierByte protect() {
            return () -> {
                try {
                    return get();
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierShort {

        short get() throws Exception;

        @NotNull
        default KSupplierShort protect() {
            return () -> {
                try {
                    return get();
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierInt {

        int get() throws Exception;

        @NotNull
        default KSupplierInt protect() {
            return () -> {
                try {
                    return get();
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierLong {

        long get() throws Exception;

        @NotNull
        default KSupplierLong protect() {
            return () -> {
                try {
                    return get();
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierFloat {

        float get() throws Exception;

        @NotNull
        default KSupplierFloat protect() {
            return () -> {
                try {
                    return get();
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierDouble {

        double get() throws Exception;

        @NotNull
        default KSupplierDouble protect() {
            return () -> {
                try {
                    return get();
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierBooleanArray {

        boolean[] get() throws Exception;

        @NotNull
        default KSupplierBooleanArray protect() {
            return () -> {
                try {
                    return get();
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierCharArray {

        char[] get() throws Exception;

        @NotNull
        default KSupplierCharArray protect() {
            return () -> {
                try {
                    return get();
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierByteArray {

        byte[] get() throws Exception;

        @NotNull
        default KSupplierByteArray protect() {
            return () -> {
                try {
                    return get();
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierShortArray {

        short[] get() throws Exception;

        @NotNull
        default KSupplierShortArray protect() {
            return () -> {
                try {
                    return get();
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierIntArray {

        int[] get() throws Exception;

        @NotNull
        default KSupplierIntArray protect() {
            return () -> {
                try {
                    return get();
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierLongArray {

        long[] get() throws Exception;

        @NotNull
        default KSupplierLongArray protect() {
            return () -> {
                try {
                    return get();
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierFloatArray {

        float[] get() throws Exception;

        @NotNull
        default KSupplierFloatArray protect() {
            return () -> {
                try {
                    return get();
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierDoubleArray {

        double[] get() throws Exception;

        @NotNull
        default KSupplierDoubleArray protect() {
            return () -> {
                try {
                    return get();
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerBoolean {

        void accept(boolean value) throws Exception;

        @NotNull
        default KConsumerBoolean andThen(@NotNull KConsumerBoolean after) {
            return $value -> {
                accept($value);
                after.accept($value);
            };
        }

        @NotNull
        default KConsumerBoolean protect() {
            return $value -> {
                try {
                    accept($value);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerChar {

        void accept(char value) throws Exception;

        @NotNull
        default KConsumerChar andThen(@NotNull KConsumerChar after) {
            return $value -> {
                accept($value);
                after.accept($value);
            };
        }

        @NotNull
        default KConsumerChar protect() {
            return $value -> {
                try {
                    accept($value);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerByte {

        void accept(byte value) throws Exception;

        @NotNull
        default KConsumerByte andThen(@NotNull KConsumerByte after) {
            return $value -> {
                accept($value);
                after.accept($value);
            };
        }

        @NotNull
        default KConsumerByte protect() {
            return $value -> {
                try {
                    accept($value);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerShort {

        void accept(short value) throws Exception;

        @NotNull
        default KConsumerShort andThen(@NotNull KConsumerShort after) {
            return $value -> {
                accept($value);
                after.accept($value);
            };
        }

        @NotNull
        default KConsumerShort protect() {
            return $value -> {
                try {
                    accept($value);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerInt {

        void accept(int value) throws Exception;

        @NotNull
        default KConsumerInt andThen(@NotNull KConsumerInt after) {
            return $value -> {
                accept($value);
                after.accept($value);
            };
        }

        @NotNull
        default KConsumerInt protect() {
            return $value -> {
                try {
                    accept($value);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerLong {

        void accept(long value) throws Exception;

        @NotNull
        default KConsumerLong andThen(@NotNull KConsumerLong after) {
            return $value -> {
                accept($value);
                after.accept($value);
            };
        }

        @NotNull
        default KConsumerLong protect() {
            return $value -> {
                try {
                    accept($value);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerFloat {

        void accept(float value) throws Exception;

        @NotNull
        default KConsumerFloat andThen(@NotNull KConsumerFloat after) {
            return $value -> {
                accept($value);
                after.accept($value);
            };
        }

        @NotNull
        default KConsumerFloat protect() {
            return $value -> {
                try {
                    accept($value);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerDouble {

        void accept(double value) throws Exception;

        @NotNull
        default KConsumerDouble andThen(@NotNull KConsumerDouble after) {
            return $value -> {
                accept($value);
                after.accept($value);
            };
        }

        @NotNull
        default KConsumerDouble protect() {
            return $value -> {
                try {
                    accept($value);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerBooleanArray {

        void accept(boolean[] value) throws Exception;

        @NotNull
        default KConsumerBooleanArray andThen(@NotNull KConsumerBooleanArray after) {
            return $value -> {
                accept($value);
                after.accept($value);
            };
        }

        @NotNull
        default KConsumerBooleanArray protect() {
            return $value -> {
                try {
                    accept($value);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerCharArray {

        void accept(char[] value) throws Exception;

        @NotNull
        default KConsumerCharArray andThen(@NotNull KConsumerCharArray after) {
            return $value -> {
                accept($value);
                after.accept($value);
            };
        }

        @NotNull
        default KConsumerCharArray protect() {
            return $value -> {
                try {
                    accept($value);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerByteArray {

        void accept(byte[] value) throws Exception;

        @NotNull
        default KConsumerByteArray andThen(@NotNull KConsumerByteArray after) {
            return $value -> {
                accept($value);
                after.accept($value);
            };
        }

        @NotNull
        default KConsumerByteArray protect() {
            return $value -> {
                try {
                    accept($value);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerShortArray {

        void accept(short[] value) throws Exception;

        @NotNull
        default KConsumerShortArray andThen(@NotNull KConsumerShortArray after) {
            return $value -> {
                accept($value);
                after.accept($value);
            };
        }

        @NotNull
        default KConsumerShortArray protect() {
            return $value -> {
                try {
                    accept($value);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerIntArray {

        void accept(int[] value) throws Exception;

        @NotNull
        default KConsumerIntArray andThen(@NotNull KConsumerIntArray after) {
            return $value -> {
                accept($value);
                after.accept($value);
            };
        }

        @NotNull
        default KConsumerIntArray protect() {
            return $value -> {
                try {
                    accept($value);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerLongArray {

        void accept(long[] value) throws Exception;

        @NotNull
        default KConsumerLongArray andThen(@NotNull KConsumerLongArray after) {
            return $value -> {
                accept($value);
                after.accept($value);
            };
        }

        @NotNull
        default KConsumerLongArray protect() {
            return $value -> {
                try {
                    accept($value);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerFloatArray {

        void accept(float[] value) throws Exception;

        @NotNull
        default KConsumerFloatArray andThen(@NotNull KConsumerFloatArray after) {
            return $value -> {
                accept($value);
                after.accept($value);
            };
        }

        @NotNull
        default KConsumerFloatArray protect() {
            return $value -> {
                try {
                    accept($value);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerDoubleArray {

        void accept(double[] value) throws Exception;

        @NotNull
        default KConsumerDoubleArray andThen(@NotNull KConsumerDoubleArray after) {
            return $value -> {
                accept($value);
                after.accept($value);
            };
        }

        @NotNull
        default KConsumerDoubleArray protect() {
            return $value -> {
                try {
                    accept($value);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionBoolean<I> {

        boolean apply(I input) throws Exception;

        @NotNull
        default KFunctionBoolean<I> protect() {
            return $t -> {
                try {
                    return apply($t);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionChar<I> {

        char apply(I input) throws Exception;

        @NotNull
        default KFunctionChar<I> protect() {
            return $t -> {
                try {
                    return apply($t);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionByte<I> {

        byte apply(I input) throws Exception;

        @NotNull
        default KFunctionByte<I> protect() {
            return $t -> {
                try {
                    return apply($t);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionShort<I> {

        short apply(I input) throws Exception;

        @NotNull
        default KFunctionShort<I> protect() {
            return $t -> {
                try {
                    return apply($t);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionInt<I> {

        int apply(I input) throws Exception;

        @NotNull
        default KFunctionInt<I> protect() {
            return $t -> {
                try {
                    return apply($t);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionLong<I> {

        long apply(I input) throws Exception;

        @NotNull
        default KFunctionLong<I> protect() {
            return $t -> {
                try {
                    return apply($t);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionFloat<I> {

        float apply(I input) throws Exception;

        @NotNull
        default KFunctionFloat<I> protect() {
            return $t -> {
                try {
                    return apply($t);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionDouble<I> {

        double apply(I input) throws Exception;

        @NotNull
        default KFunctionDouble<I> protect() {
            return $t -> {
                try {
                    return apply($t);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionBooleanArray<I> {

        boolean[] apply(I input) throws Exception;

        @NotNull
        default KFunctionBooleanArray<I> protect() {
            return $t -> {
                try {
                    return apply($t);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionCharArray<I> {

        char[] apply(I input) throws Exception;

        @NotNull
        default KFunctionCharArray<I> protect() {
            return $t -> {
                try {
                    return apply($t);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionByteArray<I> {

        byte[] apply(I input) throws Exception;

        @NotNull
        default KFunctionByteArray<I> protect() {
            return $t -> {
                try {
                    return apply($t);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionShortArray<I> {

        short[] apply(I input) throws Exception;

        @NotNull
        default KFunctionShortArray<I> protect() {
            return $t -> {
                try {
                    return apply($t);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionIntArray<I> {

        int[] apply(I input) throws Exception;

        @NotNull
        default KFunctionIntArray<I> protect() {
            return $t -> {
                try {
                    return apply($t);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionLongArray<I> {

        long[] apply(I input) throws Exception;

        @NotNull
        default KFunctionLongArray<I> protect() {
            return $t -> {
                try {
                    return apply($t);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionFloatArray<I> {

        float[] apply(I input) throws Exception;

        @NotNull
        default KFunctionFloatArray<I> protect() {
            return $t -> {
                try {
                    return apply($t);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionDoubleArray<I> {

        double[] apply(I input) throws Exception;

        @NotNull
        default KFunctionDoubleArray<I> protect() {
            return $t -> {
                try {
                    return apply($t);
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
            };
        }

    } /* ENDINTERFACE */


    @FunctionalInterface
    public interface ReduceBoolean {

        boolean reduce(boolean a, boolean b);

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface ReduceChar{

        char reduce(char a, char b);

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface ReduceByte {

        byte reduce(byte a, byte b);

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface ReduceShort {

        short reduce(short a, short b);

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface ReduceInt{

        int reduce(int a, int b);

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface ReduceLong {

        long reduce(long a, long b);

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface ReduceFloat {

        float reduce(float a, float b);

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface ReduceDouble{

        double reduce(double a, double b);

    } /* ENDINTERFACE */

} /* ENDINTERFACE */
