package com.kasisoft.libs.common.utils

import com.kasisoft.libs.common.constants.*
import com.kasisoft.libs.common.functional.*

import com.kasisoft.libs.common.*

import javax.validation.constraints.*

import java.util.*

/**
 * Declarations used to identify primitive types.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
object PrimitiveFunctions {

    private val BOOLEAN_TRUES: Set<String>  = HashSet(Arrays.asList("true", "yes", "ja", "Y", "j", "ein", "on", "enabled"))

    private val BOOLEAN_FALSES: Set<String> = HashSet(Arrays.asList("false", "no", "nein", "n", "aus", "off", "disabled"))

    /* BYTE */
    @JvmStatic
    fun min(buffer: ByteArray): Byte =
        if (buffer.size > 0) {
            var result = buffer[0]
            for (i in 1 until buffer.size) {
                result = Math.min(result.toInt(), buffer[i].toInt()).toByte()
            }
            result
        } else {
            throw KclException("API misuse. Buffer must have at least one element.")
        }

    @JvmStatic
    fun max(buffer: ByteArray): Byte =
        if (buffer.size > 0) {
            var result = buffer[0]
            for (i in 1 until buffer.size) {
                result = Math.max(result.toInt(), buffer[i].toInt()).toByte()
            }
            result
        } else {
            throw KclException("API misuse. Buffer must have at least one element.")
        }


    @JvmStatic
    fun compare(buffer: ByteArray, sequence: ByteArray, pos: Int = 0): Boolean {
        if (sequence.size == 0) {
            return true
        }
        val maxPos = buffer.size - sequence.size
        if (maxPos >= 0 && pos <= maxPos) {
            var i = 0
            var j = pos
            while (i < sequence.size) {
                if (buffer[j] != sequence[i]) {
                    return false
                }
                i++
                j++
            }
            return true
        }
        return false
    }

    @JvmStatic
    fun find(buffer: ByteArray, value: Byte, pos: Int = 0): Int {
        for (i in pos until buffer.size) {
            if (buffer[i] == value) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun indexOf(buffer: ByteArray, sequence: ByteArray, pos: @Min(0) Int = 0): @Min(-1) Int {
        if (buffer.size >= sequence.size) {
            val maxPos = buffer.size - sequence.size
            if (maxPos >= pos) {
                var idx = find(buffer, sequence[0], pos)
                while (idx != -1) {
                    if (compare(buffer, sequence, idx)) {
                        return idx
                    }
                    idx = find(buffer, sequence[0], idx + 1)
                }
            }
        }
        return -1
    }

    @JvmStatic
    fun lastIndexOf(buffer: ByteArray, sequence: ByteArray): @Min(-1) Int {
        if (buffer.size >= sequence.size) {
            val maxPos = buffer.size - sequence.size
            if (maxPos >= 0) {
                // @todo [06-JUN-2020:KASI]   scan from the end
                var idx = find(buffer, sequence[0])
                var lidx = -1
                while (idx != -1) {
                    if (compare(buffer, sequence, idx)) {
                        lidx = idx
                    }
                    idx = find(buffer, sequence[0], idx + 1)
                }
                return lidx
            }
        }
        return -1
    }

    @JvmStatic
    fun toList(buffer: ByteArray): MutableList<Byte> {
        val result = ArrayList<Byte>(buffer.size)
        for (element in buffer) {
            result.add(element)
        }
        return result
    }

    @JvmStatic
    fun toObjectArray(buffer: ByteArray): Array<Byte?> {
        val result = arrayOfNulls<Byte>(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun toPrimitiveArray(buffer: Array<Byte>): ByteArray {
        val result = ByteArray(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun toPrimitiveArrayByte(buffer: List<Byte>): ByteArray {
        val result = ByteArray(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun concat(vararg buffers: ByteArray): ByteArray {
        if (buffers.size == 0) {
            return Empty.NO_BYTES
        }
        if (buffers.size == 1) {
            return Arrays.copyOf(buffers[0], buffers[0].size)
        }
        var total = 0
        for (buffer in buffers) {
            total += buffer.size
        }
        val result = ByteArray(total)
        var pos = 0
        for (buffer in buffers) {
            System.arraycopy(buffer, 0, result, pos, buffer.size)
            pos += buffer.size
        }
        return result
    }

    @JvmStatic
    fun insert(buffer: ByteArray, sequence: ByteArray): ByteArray = concat(sequence, buffer)

    @JvmStatic
    fun insert(buffer: ByteArray, sequence: ByteArray, offset: Int): ByteArray {
        if (offset == 0) {
            return concat(sequence, buffer)
        }
        if (offset >= buffer.size) {
            throw KclException("Cannot insert at position %d into buffer of length %d", offset, buffer.size)
        }
        val total = buffer.size + sequence.size
        val result = ByteArray(total)
        System.arraycopy(buffer, 0, result, 0, offset)
        System.arraycopy(sequence, 0, result, offset, sequence.size)
        System.arraycopy(buffer, offset, result, offset + sequence.size, buffer.size - offset)
        return result
    }

    /* SHORT */
    @JvmStatic
    fun min(buffer: ShortArray): Short =
        if (buffer.size > 0) {
            var result = buffer[0]
            for (i in 1 until buffer.size) {
                result = Math.min(result.toInt(), buffer[i].toInt()).toShort()
            }
            result
        } else {
            throw KclException("API misuse. Buffer must have at least one element.")
        }

    @JvmStatic
    fun max(buffer: ShortArray): Short {
        return if (buffer.size > 0) {
            var result = buffer[0]
            for (i in 1 until buffer.size) {
                result = Math.max(result.toInt(), buffer[i].toInt()).toShort()
            }
            result
        } else {
            throw KclException("API misuse. Buffer must have at least one element.")
        }
    }

    @JvmStatic
    fun compare(buffer: ShortArray, sequence: ShortArray, pos: Int = 0): Boolean {
        if (sequence.size == 0) {
            return true
        }
        val maxPos = buffer.size - sequence.size
        if (maxPos >= 0 && pos <= maxPos) {
            var i = 0
            var j = pos
            while (i < sequence.size) {
                if (buffer[j] != sequence[i]) {
                    return false
                }
                i++
                j++
            }
            return true
        }
        return false
    }

    @JvmStatic
    fun find(buffer: ShortArray, value: Short, pos: Int = 0): Int {
        for (i in pos until buffer.size) {
            if (buffer[i] == value) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun indexOf(buffer: ShortArray, sequence: ShortArray, pos: @Min(0) Int = 0): @Min(-1) Int {
        if (buffer.size >= sequence.size) {
            val maxPos = buffer.size - sequence.size
            if (maxPos >= pos) {
                var idx = find(buffer, sequence[0], pos)
                while (idx != -1) {
                    if (compare(buffer, sequence, idx)) {
                        return idx
                    }
                    idx = find(buffer, sequence[0], idx + 1)
                }
            }
        }
        return -1
    }

    @JvmStatic
    fun lastIndexOf(buffer: ShortArray, sequence: ShortArray): @Min(-1) Int {
        if (buffer.size >= sequence.size) {
            val maxPos = buffer.size - sequence.size
            if (maxPos >= 0) {
                // @todo [06-JUN-2020:KASI]   scan from the end
                var idx = find(buffer, sequence[0])
                var lidx = -1
                while (idx != -1) {
                    if (compare(buffer, sequence, idx)) {
                        lidx = idx
                    }
                    idx = find(buffer, sequence[0], idx + 1)
                }
                return lidx
            }
        }
        return -1
    }

    @JvmStatic
    fun toList(buffer: ShortArray): MutableList<Short> {
        val result = ArrayList<Short>(buffer.size)
        for (element in buffer) {
            result.add(element)
        }
        return result
    }

    @JvmStatic
    fun toObjectArray(buffer: ShortArray): Array<Short?> {
        val result = arrayOfNulls<Short>(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun toPrimitiveArray(buffer: Array<Short>): ShortArray {
        val result = ShortArray(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun toPrimitiveArrayShort(buffer: List<Short>): ShortArray {
        val result = ShortArray(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun concat(vararg buffers: ShortArray): ShortArray {
        if (buffers.size == 0) {
            return Empty.NO_SHORTS
        }
        if (buffers.size == 1) {
            return Arrays.copyOf(buffers[0], buffers[0].size)
        }
        var total = 0
        for (buffer in buffers) {
            total += buffer.size
        }
        val result = ShortArray(total)
        var pos = 0
        for (buffer in buffers) {
            System.arraycopy(buffer, 0, result, pos, buffer.size)
            pos += buffer.size
        }
        return result
    }

    @JvmStatic
    fun insert(buffer: ShortArray, sequence: ShortArray): ShortArray = concat(sequence, buffer)

    @JvmStatic
    fun insert(buffer: ShortArray, sequence: ShortArray, offset: Int): ShortArray {
        if (offset == 0) {
            return concat(sequence, buffer)
        }
        if (offset >= buffer.size) {
            throw KclException("Cannot insert at position %d into buffer of length %d", offset, buffer.size)
        }
        val total = buffer.size + sequence.size
        val result = ShortArray(total)
        System.arraycopy(buffer, 0, result, 0, offset)
        System.arraycopy(sequence, 0, result, offset, sequence.size)
        System.arraycopy(buffer, offset, result, offset + sequence.size, buffer.size - offset)
        return result
    }

    /* INTEGER */
    @JvmStatic
    fun min(buffer: IntArray): Int =
        if (buffer.size > 0) {
            var result = buffer[0]
            for (i in 1 until buffer.size) {
                result = Math.min(result, buffer[i])
            }
            result
        } else {
            throw KclException("API misuse. Buffer must have at least one element.")
        }

    @JvmStatic
    fun max(buffer: IntArray): Int =
        if (buffer.size > 0) {
            var result = buffer[0]
            for (i in 1 until buffer.size) {
                result = Math.max(result, buffer[i])
            }
            result
        } else {
            throw KclException("API misuse. Buffer must have at least one element.")
        }

    @JvmStatic
    fun compare(buffer: IntArray, sequence: IntArray, pos: Int = 0): Boolean {
        if (sequence.size == 0) {
            return true
        }
        val maxPos = buffer.size - sequence.size
        if (maxPos >= 0 && pos <= maxPos) {
            var i = 0
            var j = pos
            while (i < sequence.size) {
                if (buffer[j] != sequence[i]) {
                    return false
                }
                i++
                j++
            }
            return true
        }
        return false
    }

    @JvmStatic
    fun find(buffer: IntArray, value: Int, pos: Int = 0): Int {
        for (i in pos until buffer.size) {
            if (buffer[i] == value) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun indexOf(buffer: IntArray, sequence: IntArray, pos: @Min(0) Int = 0): @Min(-1) Int {
        if (buffer.size >= sequence.size) {
            val maxPos = buffer.size - sequence.size
            if (maxPos >= pos) {
                var idx = find(buffer, sequence[0], pos)
                while (idx != -1) {
                    if (compare(buffer, sequence, idx)) {
                        return idx
                    }
                    idx = find(buffer, sequence[0], idx + 1)
                }
            }
        }
        return -1
    }

    @JvmStatic
    fun lastIndexOf(buffer: IntArray, sequence: IntArray): @Min(-1) Int {
        if (buffer.size >= sequence.size) {
            val maxPos = buffer.size - sequence.size
            if (maxPos >= 0) {
                // @todo [06-JUN-2020:KASI]   scan from the end
                var idx = find(buffer, sequence[0])
                var lidx = -1
                while (idx != -1) {
                    if (compare(buffer, sequence, idx)) {
                        lidx = idx
                    }
                    idx = find(buffer, sequence[0], idx + 1)
                }
                return lidx
            }
        }
        return -1
    }

    @JvmStatic
    fun toList(buffer: IntArray): MutableList<Int> {
        val result = ArrayList<Int>(buffer.size)
        for (element in buffer) {
            result.add(element)
        }
        return result
    }

    @JvmStatic
    fun toObjectArray(buffer: IntArray): Array<Int?> {
        val result = arrayOfNulls<Int>(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun toPrimitiveArray(buffer: Array<Int>): IntArray {
        val result = IntArray(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun toPrimitiveArrayInteger(buffer: List<Int>): IntArray {
        val result = IntArray(buffer!!.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun concat(vararg buffers: IntArray): IntArray {
        if (buffers.size == 0) {
            return Empty.NO_INTS
        }
        if (buffers.size == 1) {
            return Arrays.copyOf(buffers[0], buffers[0].size)
        }
        var total = 0
        for (buffer in buffers) {
            total += buffer.size
        }
        val result = IntArray(total)
        var pos = 0
        for (buffer in buffers) {
            System.arraycopy(buffer, 0, result, pos, buffer.size)
            pos += buffer.size
        }
        return result
    }

    @JvmStatic
    fun insert(buffer: IntArray, sequence: IntArray): IntArray = concat(sequence, buffer)

    @JvmStatic
    fun insert(buffer: IntArray, sequence: IntArray, offset: Int): IntArray {
        if (offset == 0) {
            return concat(sequence, buffer)
        }
        if (offset >= buffer.size) {
            throw KclException("Cannot insert at position %d into buffer of length %d", offset, buffer.size)
        }
        val total = buffer.size + sequence.size
        val result = IntArray(total)
        System.arraycopy(buffer, 0, result, 0, offset)
        System.arraycopy(sequence, 0, result, offset, sequence.size)
        System.arraycopy(buffer, offset, result, offset + sequence.size, buffer.size - offset)
        return result
    }

    /* LONG */
    @JvmStatic
    fun min(buffer: LongArray): Long =
        if (buffer.size > 0) {
            var result = buffer[0]
            for (i in 1 until buffer.size) {
                result = Math.min(result, buffer[i])
            }
            result
        } else {
            throw KclException("API misuse. Buffer must have at least one element.")
        }

    @JvmStatic
    fun max(buffer: LongArray): Long =
        if (buffer.size > 0) {
            var result = buffer[0]
            for (i in 1 until buffer.size) {
                result = Math.max(result, buffer[i])
            }
            result
        } else {
            throw KclException("API misuse. Buffer must have at least one element.")
        }

    @JvmStatic
    fun compare(buffer: LongArray, sequence: LongArray, pos: Int = 0): Boolean {
        if (sequence.size == 0) {
            return true
        }
        val maxPos = buffer.size - sequence.size
        if (maxPos >= 0 && pos <= maxPos) {
            var i = 0
            var j = pos
            while (i < sequence.size) {
                if (buffer[j] != sequence[i]) {
                    return false
                }
                i++
                j++
            }
            return true
        }
        return false
    }

    @JvmStatic
    fun find(buffer: LongArray, value: Long, pos: Int = 0): Int {
        for (i in pos until buffer.size) {
            if (buffer[i] == value) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun indexOf(buffer: LongArray, sequence: LongArray, pos: @Min(0) Int = 0): @Min(-1) Int {
        if (buffer.size >= sequence.size) {
            val maxPos = buffer.size - sequence.size
            if (maxPos >= pos) {
                var idx = find(buffer, sequence[0], pos)
                while (idx != -1) {
                    if (compare(buffer, sequence, idx)) {
                        return idx
                    }
                    idx = find(buffer, sequence[0], idx + 1)
                }
            }
        }
        return -1
    }

    @JvmStatic
    fun lastIndexOf(buffer: LongArray, sequence: LongArray): @Min(-1) Int {
        if (buffer.size >= sequence.size) {
            val maxPos = buffer.size - sequence.size
            if (maxPos >= 0) {
                // @todo [06-JUN-2020:KASI]   scan from the end
                var idx = find(buffer, sequence[0])
                var lidx = -1
                while (idx != -1) {
                    if (compare(buffer, sequence, idx)) {
                        lidx = idx
                    }
                    idx = find(buffer, sequence[0], idx + 1)
                }
                return lidx
            }
        }
        return -1
    }

    @JvmStatic
    fun toList(buffer: LongArray): MutableList<Long> {
        val result = ArrayList<Long>(buffer.size)
        for (element in buffer) {
            result.add(element)
        }
        return result
    }

    @JvmStatic
    fun toObjectArray(buffer: LongArray): Array<Long?> {
        val result = arrayOfNulls<Long>(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun toPrimitiveArray(buffer: Array<Long>): LongArray {
        val result = LongArray(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun toPrimitiveArrayLong(buffer: List<Long>): LongArray {
        val result = LongArray(buffer!!.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun concat(vararg buffers: LongArray): LongArray {
        if (buffers.size == 0) {
            return Empty.NO_LONGS
        }
        if (buffers.size == 1) {
            return Arrays.copyOf(buffers[0], buffers[0].size)
        }
        var total = 0
        for (buffer in buffers) {
            total += buffer.size
        }
        val result = LongArray(total)
        var pos = 0
        for (buffer in buffers) {
            System.arraycopy(buffer, 0, result, pos, buffer.size)
            pos += buffer.size
        }
        return result
    }

    @JvmStatic
    fun insert(buffer: LongArray, sequence: LongArray): LongArray = concat(sequence, buffer)

    @JvmStatic
    fun insert(buffer: LongArray, sequence: LongArray, offset: Int): LongArray {
        if (offset == 0) {
            return concat(sequence, buffer)
        }
        if (offset >= buffer.size) {
            throw KclException("Cannot insert at position %d into buffer of length %d", offset, buffer.size)
        }
        val total = buffer.size + sequence.size
        val result = LongArray(total)
        System.arraycopy(buffer, 0, result, 0, offset)
        System.arraycopy(sequence, 0, result, offset, sequence.size)
        System.arraycopy(buffer, offset, result, offset + sequence.size, buffer.size - offset)
        return result
    }

    /* FLOAT */
    @JvmStatic
    fun min(buffer: FloatArray): Float =
        if (buffer.size > 0) {
            var result = buffer[0]
            for (i in 1 until buffer.size) {
                result = Math.min(result, buffer[i])
            }
            result
        } else {
            throw KclException("API misuse. Buffer must have at least one element.")
        }

    @JvmStatic
    fun max(buffer: FloatArray): Float =
        if (buffer.size > 0) {
            var result = buffer[0]
            for (i in 1 until buffer.size) {
                result = Math.max(result, buffer[i])
            }
            result
        } else {
            throw KclException("API misuse. Buffer must have at least one element.")
        }

    @JvmStatic
    fun compare(buffer: FloatArray, sequence: FloatArray, pos: Int = 0): Boolean {
        if (sequence.size == 0) {
            return true
        }
        val maxPos = buffer.size - sequence.size
        if (maxPos >= 0 && pos <= maxPos) {
            var i = 0
            var j = pos
            while (i < sequence.size) {
                if (buffer[j] != sequence[i]) {
                    return false
                }
                i++
                j++
            }
            return true
        }
        return false
    }

    @JvmStatic
    fun find(buffer: FloatArray, value: Float, pos: Int = 0): Int {
        for (i in pos until buffer.size) {
            if (buffer[i] == value) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun indexOf(buffer: FloatArray, sequence: FloatArray, pos: @Min(0) Int = 0): @Min(-1) Int {
        if (buffer.size >= sequence.size) {
            val maxPos = buffer.size - sequence.size
            if (maxPos >= pos) {
                var idx = find(buffer, sequence[0], pos)
                while (idx != -1) {
                    if (compare(buffer, sequence, idx)) {
                        return idx
                    }
                    idx = find(buffer, sequence[0], idx + 1)
                }
            }
        }
        return -1
    }

    @JvmStatic
    fun lastIndexOf(buffer: FloatArray, sequence: FloatArray): @Min(-1) Int {
        if (buffer.size >= sequence.size) {
            val maxPos = buffer.size - sequence.size
            if (maxPos >= 0) {
                // @todo [06-JUN-2020:KASI]   scan from the end
                var idx = find(buffer, sequence[0])
                var lidx = -1
                while (idx != -1) {
                    if (compare(buffer, sequence, idx)) {
                        lidx = idx
                    }
                    idx = find(buffer, sequence[0], idx + 1)
                }
                return lidx
            }
        }
        return -1
    }

    @JvmStatic
    fun toList(buffer: FloatArray): MutableList<Float> {
        val result = ArrayList<Float>(buffer.size)
        for (element in buffer) {
            result.add(element)
        }
        return result
    }

    @JvmStatic
    fun toObjectArray(buffer: FloatArray): Array<Float?> {
        val result = arrayOfNulls<Float>(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun toPrimitiveArray(buffer: Array<Float>): FloatArray {
        val result = FloatArray(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun toPrimitiveArrayFloat(buffer: List<Float>): FloatArray {
        val result = FloatArray(buffer!!.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun concat(vararg buffers: FloatArray): FloatArray {
        if (buffers.size == 0) {
            return Empty.NO_FLOATS
        }
        if (buffers.size == 1) {
            return Arrays.copyOf(buffers[0], buffers[0].size)
        }
        var total = 0
        for (buffer in buffers) {
            total += buffer.size
        }
        val result = FloatArray(total)
        var pos = 0
        for (buffer in buffers) {
            System.arraycopy(buffer, 0, result, pos, buffer.size)
            pos += buffer.size
        }
        return result
    }

    @JvmStatic
    fun insert(buffer: FloatArray, sequence: FloatArray): FloatArray = concat(sequence, buffer)

    @JvmStatic
    fun insert(buffer: FloatArray, sequence: FloatArray, offset: Int): FloatArray {
        if (offset == 0) {
            return concat(sequence, buffer)
        }
        if (offset >= buffer.size) {
            throw KclException("Cannot insert at position %d into buffer of length %d", offset, buffer.size)
        }
        val total = buffer.size + sequence.size
        val result = FloatArray(total)
        System.arraycopy(buffer, 0, result, 0, offset)
        System.arraycopy(sequence, 0, result, offset, sequence.size)
        System.arraycopy(buffer, offset, result, offset + sequence.size, buffer.size - offset)
        return result
    }

    /* DOUBLE */
    @JvmStatic
    fun min(buffer: DoubleArray): Double =
        if (buffer.size > 0) {
            var result = buffer[0]
            for (i in 1 until buffer.size) {
                result = Math.min(result, buffer[i])
            }
            result
        } else {
            throw KclException("API misuse. Buffer must have at least one element.")
        }

    @JvmStatic
    fun max(buffer: DoubleArray): Double =
        if (buffer.size > 0) {
            var result = buffer[0]
            for (i in 1 until buffer.size) {
                result = Math.max(result, buffer[i])
            }
            result
        } else {
            throw KclException("API misuse. Buffer must have at least one element.")
        }

    @JvmStatic
    fun compare(buffer: DoubleArray, sequence: DoubleArray, pos: Int = 0): Boolean {
        if (sequence.size == 0) {
            return true
        }
        val maxPos = buffer.size - sequence.size
        if (maxPos >= 0 && pos <= maxPos) {
            var i = 0
            var j = pos
            while (i < sequence.size) {
                if (buffer[j] != sequence[i]) {
                    return false
                }
                i++
                j++
            }
            return true
        }
        return false
    }

    @JvmStatic
    fun find(buffer: DoubleArray, value: Double, pos: Int = 0): Int {
        for (i in pos until buffer.size) {
            if (buffer[i] == value) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun indexOf(buffer: DoubleArray, sequence: DoubleArray, pos: @Min(0) Int = 0): @Min(-1) Int {
        if (buffer.size >= sequence.size) {
            val maxPos = buffer.size - sequence.size
            if (maxPos >= pos) {
                var idx = find(buffer, sequence[0], pos)
                while (idx != -1) {
                    if (compare(buffer, sequence, idx)) {
                        return idx
                    }
                    idx = find(buffer, sequence[0], idx + 1)
                }
            }
        }
        return -1
    }

    @JvmStatic
    fun lastIndexOf(buffer: DoubleArray, sequence: DoubleArray): @Min(-1) Int {
        if (buffer.size >= sequence.size) {
            val maxPos = buffer.size - sequence.size
            if (maxPos >= 0) {
                // @todo [06-JUN-2020:KASI]   scan from the end
                var idx = find(buffer, sequence[0])
                var lidx = -1
                while (idx != -1) {
                    if (compare(buffer, sequence, idx)) {
                        lidx = idx
                    }
                    idx = find(buffer, sequence[0], idx + 1)
                }
                return lidx
            }
        }
        return -1
    }

    @JvmStatic
    fun toList(buffer: DoubleArray): MutableList<Double> {
        val result = ArrayList<Double>(buffer.size)
        for (element in buffer) {
            result.add(element)
        }
        return result
    }

    @JvmStatic
    fun toObjectArray(buffer: DoubleArray): Array<Double?> {
        val result = arrayOfNulls<Double>(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun toPrimitiveArray(buffer: Array<Double>): DoubleArray {
        val result = DoubleArray(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun toPrimitiveArrayDouble(buffer: List<Double>): DoubleArray {
        val result = DoubleArray(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun concat(vararg buffers: DoubleArray): DoubleArray {
        if (buffers.size == 0) {
            return Empty.NO_DOUBLES
        }
        if (buffers.size == 1) {
            return Arrays.copyOf(buffers[0], buffers[0].size)
        }
        var total = 0
        for (buffer in buffers) {
            total += buffer.size
        }
        val result = DoubleArray(total)
        var pos = 0
        for (buffer in buffers) {
            System.arraycopy(buffer, 0, result, pos, buffer.size)
            pos += buffer.size
        }
        return result
    }

    @JvmStatic
    fun insert(buffer: DoubleArray, sequence: DoubleArray): DoubleArray = concat(sequence, buffer)

    @JvmStatic
    fun insert(buffer: DoubleArray, sequence: DoubleArray, offset: Int): DoubleArray {
        if (offset == 0) {
            return concat(sequence, buffer)
        }
        if (offset >= buffer.size) {
            throw KclException("Cannot insert at position %d into buffer of length %d", offset, buffer.size)
        }
        val total = buffer.size + sequence.size
        val result = DoubleArray(total)
        System.arraycopy(buffer, 0, result, 0, offset)
        System.arraycopy(sequence, 0, result, offset, sequence.size)
        System.arraycopy(buffer, offset, result, offset + sequence.size, buffer.size - offset)
        return result
    }

    /* CHAR */
    @JvmStatic
    fun compare(buffer: CharArray, sequence: CharArray, pos: Int = 0): Boolean {
        if (sequence.size == 0) {
            return true
        }
        val maxPos = buffer.size - sequence.size
        if (maxPos >= 0 && pos <= maxPos) {
            var i = 0
            var j = pos
            while (i < sequence.size) {
                if (buffer[j] != sequence[i]) {
                    return false
                }
                i++
                j++
            }
            return true
        }
        return false
    }

    @JvmStatic
    fun find(buffer: CharArray, value: Char, pos: Int = 0): Int {
        for (i in pos until buffer.size) {
            if (buffer[i] == value) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun indexOf(buffer: CharArray, sequence: CharArray, pos: @Min(0) Int = 0): @Min(-1) Int {
        if (buffer.size >= sequence.size) {
            val maxPos = buffer.size - sequence.size
            if (maxPos >= pos) {
                var idx = find(buffer, sequence[0], pos)
                while (idx != -1) {
                    if (compare(buffer, sequence, idx)) {
                        return idx
                    }
                    idx = find(buffer, sequence[0], idx + 1)
                }
            }
        }
        return -1
    }

    @JvmStatic
    fun lastIndexOf(buffer: CharArray, sequence: CharArray): @Min(-1) Int {
        if (buffer.size >= sequence.size) {
            val maxPos = buffer.size - sequence.size
            if (maxPos >= 0) {
                // @todo [06-JUN-2020:KASI]   scan from the end
                var idx = find(buffer, sequence[0])
                var lidx = -1
                while (idx != -1) {
                    if (compare(buffer, sequence, idx)) {
                        lidx = idx
                    }
                    idx = find(buffer, sequence[0], idx + 1)
                }
                return lidx
            }
        }
        return -1
    }

    @JvmStatic
    fun toList(buffer: CharArray): MutableList<Char> {
        val result = ArrayList<Char>(buffer.size)
        for (element in buffer) {
            result.add(element)
        }
        return result
    }

    @JvmStatic
    fun toObjectArray(buffer: CharArray): Array<Char?> {
        val result = arrayOfNulls<Char>(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun toPrimitiveArray(buffer: Array<Char>): CharArray {
        val result = CharArray(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun toPrimitiveArrayCharacter(buffer: List<Char>): CharArray {
        val result = CharArray(buffer!!.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun concat(vararg buffers: CharArray): CharArray {
        if (buffers.size == 0) {
            return Empty.NO_CHARS
        }
        if (buffers.size == 1) {
            return Arrays.copyOf(buffers[0], buffers[0].size)
        }
        var total = 0
        for (buffer in buffers) {
            total += buffer.size
        }
        val result = CharArray(total)
        var pos = 0
        for (buffer in buffers) {
            System.arraycopy(buffer, 0, result, pos, buffer.size)
            pos += buffer.size
        }
        return result
    }

    @JvmStatic
    fun insert(buffer: CharArray, sequence: CharArray): CharArray = concat(sequence, buffer)

    @JvmStatic
    fun insert(buffer: CharArray, sequence: CharArray, offset: Int): CharArray {
        if (offset == 0) {
            return concat(sequence, buffer)
        }
        if (offset >= buffer.size) {
            throw KclException("Cannot insert at position %d into buffer of length %d", offset, buffer.size)
        }
        val total = buffer.size + sequence.size
        val result = CharArray(total)
        System.arraycopy(buffer, 0, result, 0, offset)
        System.arraycopy(sequence, 0, result, offset, sequence.size)
        System.arraycopy(buffer, offset, result, offset + sequence.size, buffer.size - offset)
        return result
    }

    /* BOOLEAN */
    @JvmStatic
    fun or(buffer: BooleanArray): Boolean {
        if (buffer.size > 0) {
            for (b in buffer) {
                if (b) {
                    return true
                }
            }
            return false
        } else {
            throw KclException("API misuse. Buffer must have at least one element.")
        }
    }

    @JvmStatic
    fun and(buffer: BooleanArray): Boolean {
        if (buffer.size > 0) {
            for (b in buffer) {
                if (!b) {
                    return false
                }
            }
            return true
        } else {
            throw KclException("API misuse. Buffer must have at least one element.")
        }
    }

    @JvmStatic
    fun compare(buffer: BooleanArray, sequence: BooleanArray, pos: Int = 0): Boolean {
        if (sequence.size == 0) {
            return true
        }
        val maxPos = buffer.size - sequence.size
        if (maxPos >= 0 && pos <= maxPos) {
            var i = 0
            var j = pos
            while (i < sequence.size) {
                if (buffer[j] != sequence[i]) {
                    return false
                }
                i++
                j++
            }
            return true
        }
        return false
    }

    @JvmStatic
    fun find(buffer: BooleanArray, value: Boolean, pos: Int = 0): Int {
        for (i in pos until buffer.size) {
            if (buffer[i] == value) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun indexOf(buffer: BooleanArray, sequence: BooleanArray, pos: @Min(0) Int = 0): @Min(-1) Int {
        if (buffer.size >= sequence.size) {
            val maxPos = buffer.size - sequence.size
            if (maxPos >= pos) {
                var idx = find(buffer, sequence[0], pos)
                while (idx != -1) {
                    if (compare(buffer, sequence, idx)) {
                        return idx
                    }
                    idx = find(buffer, sequence[0], idx + 1)
                }
            }
        }
        return -1
    }

    @JvmStatic
    fun lastIndexOf(buffer: BooleanArray, sequence: BooleanArray): @Min(-1) Int {
        if (buffer.size >= sequence.size) {
            val maxPos = buffer.size - sequence.size
            if (maxPos >= 0) {
                // @todo [06-JUN-2020:KASI]   scan from the end
                var idx = find(buffer, sequence[0])
                var lidx = -1
                while (idx != -1) {
                    if (compare(buffer, sequence, idx)) {
                        lidx = idx
                    }
                    idx = find(buffer, sequence[0], idx + 1)
                }
                return lidx
            }
        }
        return -1
    }

    @JvmStatic
    fun toList(buffer: BooleanArray): MutableList<Boolean> {
        val result = ArrayList<Boolean>(buffer.size)
        for (element in buffer) {
            result.add(element)
        }
        return result
    }

    @JvmStatic
    fun toObjectArray(buffer: BooleanArray): Array<Boolean?> {
        val result = arrayOfNulls<Boolean>(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun toPrimitiveArray(buffer: Array<Boolean>): BooleanArray {
        val result = BooleanArray(buffer.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun toPrimitiveArrayBoolean(buffer: List<Boolean>): BooleanArray {
        val result = BooleanArray(buffer!!.size)
        for (i in buffer.indices) {
            result[i] = buffer[i]
        }
        return result
    }

    @JvmStatic
    fun concat(vararg buffers: BooleanArray): BooleanArray {
        if (buffers.size == 0) {
            return Empty.NO_BOOLEANS
        }
        if (buffers.size == 1) {
            return Arrays.copyOf(buffers[0], buffers[0].size)
        }
        var total = 0
        for (buffer in buffers) {
            total += buffer.size
        }
        val result = BooleanArray(total)
        var pos = 0
        for (buffer in buffers) {
            System.arraycopy(buffer, 0, result, pos, buffer.size)
            pos += buffer.size
        }
        return result
    }

    @JvmStatic
    fun insert(buffer: BooleanArray, sequence: BooleanArray): BooleanArray = concat(sequence, buffer)

    @JvmStatic
    fun insert(buffer: BooleanArray, sequence: BooleanArray, offset: Int): BooleanArray {
        if (offset == 0) {
            return concat(sequence, buffer)
        }
        if (offset >= buffer.size) {
            throw KclException("Cannot insert at position %d into buffer of length %d", offset, buffer.size)
        }
        val total = buffer.size + sequence.size
        val result = BooleanArray(total)
        System.arraycopy(buffer, 0, result, 0, offset)
        System.arraycopy(sequence, 0, result, offset, sequence.size)
        System.arraycopy(buffer, offset, result, offset + sequence.size, buffer.size - offset)
        return result
    }

    /* PARSING PRIMITIVE TYPES */
    /**
     * Interpretes a value as a boolean.
     *
     * @param value   The value which has to be parsed.
     *
     * @return   `true`  <=> If the supplied literal has one of the values [.TRUEVALUES] (case insensitive).
     * `false` <=> All other cases.
     */
    @JvmStatic
    fun parseBoolean(value: String): Boolean {
        val lower = value.lowercase(Locale.getDefault())
        if (BOOLEAN_TRUES.contains(lower)) {
            return true
        }
        if (BOOLEAN_FALSES.contains(lower)) {
            return false
        }
        throw KclException("Invalid boolean value: '%s'", value!!)
    }

    /* SUPPLIERS */
    @JvmStatic
    fun getBoolean(inv: KSupplierBoolean): Boolean =
        try {
            inv.get()
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }

    @JvmStatic
    fun getChar(inv: KSupplierChar): Char =
        try {
            inv.get()
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }

    @JvmStatic
    fun getByte(inv: KSupplierByte): Byte =
        try {
            inv.get()
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }

    @JvmStatic
    fun getShort(inv: KSupplierShort): Short =
        try {
            inv.get()
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }

    @JvmStatic
    fun getInt(inv: KSupplierInt): Int =
        try {
            inv.get()
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }

    @JvmStatic
    fun getLong(inv: KSupplierLong): Long =
        try {
            inv.get()
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }

    @JvmStatic
    fun getFloat(inv: KSupplierFloat): Float =
        try {
            inv.get()
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }

    @JvmStatic
    fun getDouble(inv: KSupplierDouble): Double =
        try {
            inv.get()
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }

    /* CONSUMERS */
    @JvmStatic
    fun acceptBoolean(inv: KConsumerBoolean, value: Boolean) {
        try {
            inv.accept(value)
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }
    }

    @JvmStatic
    fun acceptChar(inv: KConsumerChar, value: Char) {
        try {
            inv.accept(value)
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }
    }

    @JvmStatic
    fun acceptByte(inv: KConsumerByte, value: Byte) {
        try {
            inv.accept(value)
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }
    }

    @JvmStatic
    fun acceptShort(inv: KConsumerShort, value: Short) {
        try {
            inv.accept(value)
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }
    }

    @JvmStatic
    fun acceptInt(inv: KConsumerInt, value: Int) {
        try {
            inv.accept(value)
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }
    }

    @JvmStatic
    fun acceptLong(inv: KConsumerLong, value: Long) {
        try {
            inv.accept(value)
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }
    }

    @JvmStatic
    fun acceptFloat(inv: KConsumerFloat, value: Float) {
        try {
            inv.accept(value)
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }
    }

    @JvmStatic
    fun acceptDouble(inv: KConsumerDouble, value: Double) {
        try {
            inv.accept(value)
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }
    }

    /* FUNCTIONS */
    @JvmStatic
    fun <I> applyBoolean(inv: KFunctionBoolean<I>, input: I): Boolean =
        try {
            inv.apply(input)
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }

    @JvmStatic
    fun <I> applyChar(inv: KFunctionChar<I>, input: I): Char =
        try {
            inv.apply(input)
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }

    @JvmStatic
    fun <I> applyByte(inv: KFunctionByte<I>, input: I): Byte =
        try {
            inv.apply(input)
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }

    @JvmStatic
    fun <I> applyShort(inv: KFunctionShort<I>, input: I): Short =
        try {
            inv.apply(input)
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }

    @JvmStatic
    fun <I> applyInt(inv: KFunctionInt<I>, input: I): Int =
        try {
            inv.apply(input)
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }

    @JvmStatic
    fun <I> applyLong(inv: KFunctionLong<I>, input: I): Long =
        try {
            inv.apply(input)
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }

    @JvmStatic
    fun <I> applyFloat(inv: KFunctionFloat<I>, input: I): Float =
        try {
            inv.apply(input)
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }

    @JvmStatic
    fun <I> applyDouble(inv: KFunctionDouble<I>, input: I): Double =
        try {
            inv.apply(input)
        } catch (ex: Exception) {
            throw KclException.wrap(ex)
        }

} /* ENDOBJECT */
