package com.kasisoft.libs.common.functional;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface PrimitiveInterfaces {

    @FunctionalInterface
    public interface KSupplierBoolean {

        boolean get() throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierChar {

        char get() throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierByte {

        byte get() throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierShort {

        short get() throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierInt {

        int get() throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierLong {

        long get() throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierFloat {

        float get() throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KSupplierDouble {

        double get() throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerBoolean {

        void accept(boolean value) throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerChar {

        void accept(char value) throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerByte {

        void accept(byte value) throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerShort {

        void accept(short value) throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerInt {

        void accept(int value) throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerLong {

        void accept(long value) throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerFloat {

        void accept(float value) throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KConsumerDouble {

        void accept(double value) throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionBoolean<I> {

        boolean apply(I input) throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionChar<I> {

        char apply(I input) throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionByte<I> {

        byte apply(I input) throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionShort<I> {

        short apply(I input) throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionInt<I> {

        int apply(I input) throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionLong<I> {

        long apply(I input) throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionFloat<I> {

        float apply(I input) throws Exception;

    } /* ENDINTERFACE */

    @FunctionalInterface
    public interface KFunctionDouble<I> {

        double apply(I input) throws Exception;

    } /* ENDINTERFACE */

} /* ENDINTERFACE */
