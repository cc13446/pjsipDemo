/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.pjsip.pjsua2;

public class TsxStateEvent {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected TsxStateEvent(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(TsxStateEvent obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        pjsua2JNI.delete_TsxStateEvent(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setSrc(TsxStateEventSrc value) {
    pjsua2JNI.TsxStateEvent_src_set(swigCPtr, this, TsxStateEventSrc.getCPtr(value), value);
  }

  public TsxStateEventSrc getSrc() {
    long cPtr = pjsua2JNI.TsxStateEvent_src_get(swigCPtr, this);
    return (cPtr == 0) ? null : new TsxStateEventSrc(cPtr, false);
  }

  public void setTsx(SipTransaction value) {
    pjsua2JNI.TsxStateEvent_tsx_set(swigCPtr, this, SipTransaction.getCPtr(value), value);
  }

  public SipTransaction getTsx() {
    long cPtr = pjsua2JNI.TsxStateEvent_tsx_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SipTransaction(cPtr, false);
  }

  public void setPrevState(pjsip_tsx_state_e value) {
    pjsua2JNI.TsxStateEvent_prevState_set(swigCPtr, this, value.swigValue());
  }

  public pjsip_tsx_state_e getPrevState() {
    return pjsip_tsx_state_e.swigToEnum(pjsua2JNI.TsxStateEvent_prevState_get(swigCPtr, this));
  }

  public void setType(pjsip_event_id_e value) {
    pjsua2JNI.TsxStateEvent_type_set(swigCPtr, this, value.swigValue());
  }

  public pjsip_event_id_e getType() {
    return pjsip_event_id_e.swigToEnum(pjsua2JNI.TsxStateEvent_type_get(swigCPtr, this));
  }

  public TsxStateEvent() {
    this(pjsua2JNI.new_TsxStateEvent(), true);
  }

}
