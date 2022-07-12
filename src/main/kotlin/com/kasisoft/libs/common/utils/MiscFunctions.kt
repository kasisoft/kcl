package com.kasisoft.libs.common.utils

import com.kasisoft.libs.common.functional.*

import com.kasisoft.libs.common.constants.*

import com.kasisoft.libs.common.types.*

import com.kasisoft.libs.common.text.*

import javax.validation.constraints.*

import java.util.function.Function
import java.util.function.*

import java.util.*

import java.security.*
import java.time.*

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
object MiscFunctions {

    @JvmStatic
    fun <R: Any?> trimLeading(input: MutableList<R>): MutableList<R> {
        while ((!input.isEmpty()) && (input.get(0) == null)) {
            input.removeFirst()
        }
        return input
    }

    @JvmStatic
    fun <R: Any?> trimTrailing(input: MutableList<R>): MutableList<R> {
        while ((!input.isEmpty()) && (input.get(input.lastIndex) == null)) {
            input.removeLast()
        }
        return input
    }

    @JvmStatic
    fun <R: Any?> trim(input: MutableList<R>): MutableList<R> = trimTrailing(trimLeading(input))

    /**
     * Returns an adjusted index since this extension supports negative indices as well.
     *
     * @param length  The length to be used for the calculation.
     * @param index   The index supplied by the user.
     *
     * @return  The index to use for the original implementation.
     */
    @JvmStatic
    fun adjustIndex(length: Int, index: Int, isEnd: Boolean = false): Int =
        if (index < 0) {
            length + index
        } else if ((index == 0) && isEnd) {
            length
        } else {
            index
        }


    /** @spec [22-JUN-2020:KASI] https://en.gravatar.com/site/implement/hash/ */
    @JvmStatic
    fun getGravatarLink(email: String, size: Int? = null): String {
        val lowercase = email.lowercase()
        val hash      = Digest.MD5.digestToString(lowercase.toByteArray())
        if (size != null) {
            return "https://www.gravatar.com/avatar/$hash?s=$size"
        } else {
            return "https://www.gravatar.com/avatar/$hash"
        }
    }

    /**
     * Creates a set from the supplied elements.
     *
     * @param elements   The elements that shall be collected within a set.
     *
     * @return   The set created from the supplied elements.
     */
    @JvmStatic
    fun <T: Any?> toSet(vararg elements: T): Set<T> {
        var result = HashSet<T>()
        elements?.forEach { result.add(it) }
        return result
    }

    /**
     * Returns <code>true</code> if the supplied year is a leap year.
     *
     * @param year   The year which has to be tested.
     *
     * @return   <code>true</code> <=> The supplied year is a leap year.
     */
    @JvmStatic
    fun isLeapYear(year: Int): Boolean =
        if ((year % 400) == 0) {
          true
        } else if ((year % 100) == 0) {
          false
        } else {
          (year % 4) == 0
        }

    /**
     * Returns <code>true</code> if the supplied date is a leap year.
     *
     * @param date   The date which has to be tested.
     *
     * @return   <code>true</code> <=> The supplied date is a leap year.
     */
    @SuppressWarnings("deprecation")
    @JvmStatic
    fun isLeapYear(date: Date): Boolean = isLeapYear(date.year + 1900)

    @JvmStatic
    fun isLeapYear(date: OffsetDateTime): Boolean = isLeapYear(date.getYear())

    @JvmStatic
    fun isLeapYear(date: LocalDateTime): Boolean = isLeapYear(date.getYear())

    /**
     * Calculates the biggest common divisor.
     *
     * @param a   One number.
     * @param b   Another number.
     *
     * @return   The biggest common divisor.
     */
    @JvmStatic
    fun gcd(a: Int, b: Int): Int =
        if (b == 0) {
          a
        } else {
          gcd(b, a % b)
        }

    @JvmStatic
    fun <R: Any?> toUniqueList(list: List<R>): MutableList<R> = ArrayList<R>(TreeSet(list))

    /**
     * Creates a list of pairs from the supplied entries.
     *
     * @param entries   The entries that will be returned as list of pairs.
     *
     * @return   A list of pairs.
     */
    @JvmStatic
    fun <R: Any?> toPairs(vararg entries: R): MutableList<Pair<R, R>> {
        val count  = entries.size / 2
        val result = ArrayList<Pair<R, R>>(count)
        if (count > 0) {
            val top = count * 2 -1
            for (i in 0..top step 2) {
                result.add(Pair<R, R>(entries[i], entries[i + 1]))
            }
        }
        return result;
    }

    /**
     * Creates a map from the supplied entries.
     *
     * @param entries   The entries that will be returned as a map.
     *
     * @return   A map providing all entries (unless the list length is odd).
     */
    @JvmStatic
    fun <K, V: Any?> toMap(vararg entries: Any?): MutableMap<K, V> {
        val count = entries.size / 2
        val result= HashMap<K, V>(count)
        for (i in 0..entries.lastIndex step 2) {
            result.put(entries[i] as K,entries[i + 1] as V)
        }
        return result
    }


    /**
     * Convenience function which waits until the supplied Thread finishes his task or will be interrupted.
     *
     * @param thread   The Thread that will be executed.
     */
    @JvmStatic
    fun joinThread(thread: Thread?) {
        if (thread != null) {
            try {
                thread.join()
            } catch (ex: InterruptedException) {
            }
        }
    }

    @JvmStatic
    fun sleep(millis: Long) {
        var towait = millis
        while (towait> 0) {
            val before = System.currentTimeMillis()
            try {
                Thread.sleep(millis);
            } catch (ex: InterruptedException) {
            }
            val after = System.currentTimeMillis()
            val done  = after - before
            towait   -= done
        }
    }

    /**
     * Interpretes a value as a boolean. Causes an exception if the value isn't recognized.
     *
     * @param value   The value which has to be parsed.
     *
     * @return   A boolean value if recognized or null if the argument is null as well.
     */
    @JvmStatic
    fun parseBoolean(value: String?): Boolean? = parse(value, PrimitiveFunctions::parseBoolean)

    /**
     * Interpretes a value as a byte. Causes an exception if the value isn't recognized.
     *
     * @param value   The value which has to be parsed.
     *
     * @return   A byte value if recognized or null if the argument is null as well.
     */
    @JvmStatic
    fun parseByte(value: String?): Byte? = parse(value, String::toByte)

    /**
     * Interpretes a value as a short. Causes an exception if the value isn't recognized.
     *
     * @param value   The value which has to be parsed.
     *
     * @return   A short value if recognized or null if the argument is null as well.
     */
    @JvmStatic
    fun parseShort(value: String?): Short? = parse(value, String::toShort)

    /**
     * Interpretes a value as a integer. Causes an exception if the value isn't recognized.
     *
     * @param value   The value which has to be parsed.
     *
     * @return   An int value if recognized or null if the argument is null as well.
     */
    @JvmStatic
    fun parseInt(value: String?): Int? = parse(value, Integer::parseInt)

    /**
     * Interpretes a value as a long. Causes an exception if the value isn't recognized.
     *
     * @param value   The value which has to be parsed.
     *
     * @return   A long value if recognized or null if the argument is null as well.
     */
    @JvmStatic
    fun parseLong(value: String?): Long? = parse(value, String::toLong)

    /**
     * Interpretes a value as a float. Causes an exception if the value isn't recognized.
     *
     * @param value   The value which has to be parsed.
     *
     * @return   A float value if recognized or null if the argument is null as well.
     */
    @JvmStatic
    fun parseFloat(value: String?): Float? = parse(value, String::toFloat)

    /**
     * Interpretes a value as a double. Causes an exception if the value isn't recognized.
     *
     * @param value   The value which has to be parsed.
     *
     * @return   A double value if recognized or null if the argument is null as well.
     */
    @JvmStatic
    fun parseDouble(value: String?): Double? = parse(value, String::toDouble)

  /**
   * Parses a number and returns null if the value is invalid.
   *
   * @param value   The value shall be parsed.
   * @param parse   The function used to parse the value.
   *
   * @return   The parsed value or null.
   */
    @JvmStatic
    fun <T: Any?> parse(value: String?, convert: Function<String, T>): T? =
        if (value != null) {
            try {
                convert.apply(value);
            } catch (ex: Exception) {
                // cause to return null
                null
            }
        } else {
            null
        }

    /**
     * Creates a list of <param>count</param> elements while repeating the supplied one.
     *
     * @param count     The number of elements that shall be created.
     * @param element   The element that shall be repeated.
     *
     * @return   A list with the supplied amount of elements.
     */
    @JvmStatic
    fun <T: Any?> repeat(count: Int, element: T): MutableList<T> {
        val result = ArrayList<T>(count)
        for (i in 1..count) {
            result.add(element)
        }
        return result;
    }

    @JvmStatic
    fun propertiesToMap(properties: Properties?): MutableMap<String, String?> {
        val result = HashMap<String, String?>(100)
        properties?.stringPropertyNames()?.forEach {
            result[it] = StringFunctions.cleanup(properties.getProperty(it))
        }
        return result;
    }

    @JvmStatic
    fun <R: Any?> wrapToExtendedList(source: MutableList<R>?): ExtendedList<R> =
        if (source == null) {
            ExtendedList<R>(ArrayList())
        } else {
            ExtendedList<R>(source)
        }

    /**
     * Executes the supplied {@link KRunnable} instance while making sure that System.exit calls won't stop the VM.
     *
     * @param runnable   The {@link KRunnable} instance which has to be executed.
     *
     * @return   The exitcode which had been raised.
     */
    @JvmStatic
    fun executeWithoutExit(runnable: KRunnable): Int {
        val oldsecuritymanager = System.getSecurityManager()
        try {
            val sm = CustomSecurityManager()
            System.setSecurityManager(sm)
            val thread = Thread(runnable.protect())
            thread.setUncaughtExceptionHandler(sm)
            thread.start()
            joinThread(thread)
            return sm.exitCode
        } finally {
            System.setSecurityManager(oldsecuritymanager);
        }
    }

    /**
     * This exception indicates that a System.exit call has been intercepted.
     */
    private class ExitTrappedException: SecurityException() {

        companion object {

            private const val serialVersionUID: Long = -3937579776034175019L

        }

    } /* ENDCLASS */

    /**
     * SecurityManager implementation which disables System.exit calls.
     */
    private class CustomSecurityManager(var exitCode: Int = 0): SecurityManager(), Thread.UncaughtExceptionHandler {

        override fun checkExit(exitcode: Int) {
            this.exitCode = exitcode
            throw ExitTrappedException()
        }

        override fun checkPermission(permission: Permission) {}

        override fun uncaughtException(t: Thread, e: Throwable) {
            if (e is RuntimeException) {
                throw e
            }
        }

    } /* ENDCLASS */

} /* ENDOBJECT */
