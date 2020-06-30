package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.functional.Functions;
import com.kasisoft.libs.common.utils.PrimitiveFunctions;

import java.nio.CharBuffer;

import java.io.Reader;
import java.io.Writer;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KReader extends Reader {

  Reader    impl;

  @Override
  public int read(CharBuffer target) {
    return PrimitiveFunctions.applyInt(impl::read, target);
  }

  @Override
  public int read() {
    return PrimitiveFunctions.getInt(impl::read);
  }

  @Override
  public int read(char cbuf[]) {
    return PrimitiveFunctions.applyInt(impl::read, cbuf);
  }

  @Override
  public int read(char cbuf[], int off, int len) {
    return PrimitiveFunctions.getInt(() -> impl.read(cbuf, off, len));
  }

  @Override
  public long skip(long n)  {
    return PrimitiveFunctions.applyLong(impl::skip, n);
  }

  @Override
  public boolean ready() {
    return PrimitiveFunctions.getBoolean(impl::ready);
  }

  @Override
  public boolean markSupported() {
    return PrimitiveFunctions.getBoolean(impl::markSupported);
  }

  @Override
  public void mark(int readAheadLimit) {
    PrimitiveFunctions.acceptInt(impl::mark, readAheadLimit);
  }

  @Override
  public void reset() {
    Functions.run(impl::reset);
  }

  @Override
  public void close() {
    Functions.run(impl::close);
  }

  @Override
  public long transferTo(Writer out) {
    return PrimitiveFunctions.applyLong(impl::transferTo, out);
  }

} /* ENDCLASS */
