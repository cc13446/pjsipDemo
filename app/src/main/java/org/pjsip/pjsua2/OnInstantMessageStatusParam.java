/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.pjsip.pjsua2;

public class OnInstantMessageStatusParam {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected OnInstantMessageStatusParam(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(OnInstantMessageStatusParam obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        pjsua2JNI.delete_OnInstantMessageStatusParam(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setUserData(SWIGTYPE_p_void value) {
    pjsua2JNI.OnInstantMessageStatusParam_userData_set(swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
  }

  public SWIGTYPE_p_void getUserData() {
    long cPtr = pjsua2JNI.OnInstantMessageStatusParam_userData_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_void(cPtr, false);
  }

  public void setToUri(String value) {
    pjsua2JNI.OnInstantMessageStatusParam_toUri_set(swigCPtr, this, value);
  }

  public String getToUri() {
    return pjsua2JNI.OnInstantMessageStatusParam_toUri_get(swigCPtr, this);
  }

  public void setMsgBody(String value) {
    pjsua2JNI.OnInstantMessageStatusParam_msgBody_set(swigCPtr, this, value);
  }

  public String getMsgBody() {
    return pjsua2JNI.OnInstantMessageStatusParam_msgBody_get(swigCPtr, this);
  }

  public void setCode(pjsip_status_code value) {
    pjsua2JNI.OnInstantMessageStatusParam_code_set(swigCPtr, this, value.swigValue());
  }

  public pjsip_status_code getCode() {
    return pjsip_status_code.swigToEnum(pjsua2JNI.OnInstantMessageStatusParam_code_get(swigCPtr, this));
  }

  public void setReason(String value) {
    pjsua2JNI.OnInstantMessageStatusParam_reason_set(swigCPtr, this, value);
  }

  public String getReason() {
    return pjsua2JNI.OnInstantMessageStatusParam_reason_get(swigCPtr, this);
  }

  public void setRdata(SipRxData value) {
    pjsua2JNI.OnInstantMessageStatusParam_rdata_set(swigCPtr, this, SipRxData.getCPtr(value), value);
  }

  public SipRxData getRdata() {
    long cPtr = pjsua2JNI.OnInstantMessageStatusParam_rdata_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SipRxData(cPtr, false);
  }

  public OnInstantMessageStatusParam() {
    this(pjsua2JNI.new_OnInstantMessageStatusParam(), true);
  }

}
