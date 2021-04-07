package com.chenchen.android.pjsipdemo.Domain;

import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.pjsip_transport_type_e;

public class SipEndPoint extends Endpoint {
    public static SipEndPoint sipEndPoint = null;

    public static SipEndPoint getInstance() {
        if(null == sipEndPoint) sipEndPoint = new SipEndPoint();
        return sipEndPoint;
    }

    public void init(){
        try {
            // Create endpoint
            if (null == sipEndPoint) sipEndPoint = new SipEndPoint();

            sipEndPoint.libCreate();
            // Initialize endpoint
            EpConfig epConfig = new EpConfig();
            sipEndPoint.libInit(epConfig);
            // Create SIP transport. Error handling sample is shown
            TransportConfig sipTpConfig = new TransportConfig();
            sipTpConfig.setPort(5060);
            sipEndPoint.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP, sipTpConfig);
            // Start the library
            sipEndPoint.libStart();
        }catch (Exception e){

        }

    }

    public void destroy(){
        if(null != sipEndPoint) {
            try {
                sipEndPoint.libDestroy();
                sipEndPoint.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
