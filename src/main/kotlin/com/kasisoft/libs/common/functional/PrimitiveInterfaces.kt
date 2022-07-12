package com.kasisoft.libs.common.functional

fun interface KSupplierBoolean {

    @Throws(Exception::class)
    fun get(): Boolean

} /* ENDINTERFACE */

fun interface KSupplierChar {

    @Throws(Exception::class)
    fun get(): Char

} /* ENDINTERFACE */

fun interface KSupplierByte {

    @Throws(Exception::class)
    fun get(): Byte

} /* ENDINTERFACE */

fun interface KSupplierShort {

    @Throws(Exception::class)
    fun get(): Short

} /* ENDINTERFACE */

fun interface KSupplierInt {

    @Throws(Exception::class)
    fun get(): Int

} /* ENDINTERFACE */

fun interface KSupplierLong {

    @Throws(Exception::class)
    fun get(): Long

} /* ENDINTERFACE */

fun interface KSupplierFloat {

    @Throws(Exception::class)
    fun get(): Float

} /* ENDINTERFACE */

fun interface KSupplierDouble {

    @Throws(Exception::class)
    fun get(): Double

} /* ENDINTERFACE */

fun interface KConsumerBoolean {

    @Throws(Exception::class)
    fun accept(value: Boolean)

} /* ENDINTERFACE */

fun interface KConsumerChar {

    @Throws(Exception::class)
    fun accept(value: Char)

} /* ENDINTERFACE */

fun interface KConsumerByte {

    @Throws(Exception::class)
    fun accept(value: Byte)

} /* ENDINTERFACE */

fun interface KConsumerShort {

    @Throws(Exception::class)
    fun accept(value: Short)

} /* ENDINTERFACE */

fun interface KConsumerInt {

    @Throws(Exception::class)
    fun accept(value: Int)

} /* ENDINTERFACE */

fun interface KConsumerLong {

    @Throws(Exception::class)
    fun accept(value: Long)

} /* ENDINTERFACE */

fun interface KConsumerFloat {

    @Throws(Exception::class)
    fun accept(value: Float)

} /* ENDINTERFACE */

fun interface KConsumerDouble {

    @Throws(Exception::class)
    fun  accept(value: Double)

} /* ENDINTERFACE */

fun interface KFunctionBoolean<I: Any?> {

    @Throws(Exception::class)
    fun apply(input: I): Boolean

} /* ENDINTERFACE */

fun interface KFunctionChar<I: Any?> {

    @Throws(Exception::class)
    fun apply(input: I): Char

} /* ENDINTERFACE */

fun interface KFunctionByte<I: Any?> {

    @Throws(Exception::class)
    fun apply(input: I): Byte

} /* ENDINTERFACE */

fun interface KFunctionShort<I: Any?> {

    @Throws(Exception::class)
    fun apply(input: I): Short

} /* ENDINTERFACE */

fun interface KFunctionInt<I: Any?> {

    @Throws(Exception::class)
    fun apply(input: I): Int

} /* ENDINTERFACE */

fun interface KFunctionLong<I: Any?> {

    @Throws(Exception::class)
    fun apply(input: I): Long

} /* ENDINTERFACE */

fun interface KFunctionFloat<I: Any?> {

    @Throws(Exception::class)
    fun apply(input: I): Float

} /* ENDINTERFACE */

fun interface KFunctionDouble<I: Any?> {

    @Throws(Exception::class)
    fun apply(input: I): Double

} /* ENDINTERFACE */
