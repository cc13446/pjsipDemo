/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.pjsip.pjsua2;

public class ToneDigit extends pjmedia_tone_digit {
  private long swigCPtr;

  protected ToneDigit(long cPtr, boolean cMemoryOwn) {
    super(pjsua2JNI.ToneDigit_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ToneDigit obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        pjsua2JNI.delete_ToneDigit(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public ToneDigit() {
    this(pjsua2JNI.new_ToneDigit(), true);
  }

}
