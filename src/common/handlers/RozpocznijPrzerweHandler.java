package common.handlers;

import hla.rti.FederateNotExecutionMember;
import hla.rti.NameNotFound;
import hla.rti.RTIambassador;
import hla.rti.RTIinternalError;

/**
 * Created by Karolina on 22.06.2017.
 */
public class RozpocznijPrzerweHandler {
    public int RozpocznijPrzerweHandler;

    public RozpocznijPrzerweHandler(RTIambassador rtiamb) throws NameNotFound, FederateNotExecutionMember, RTIinternalError {
        RozpocznijPrzerweHandler = rtiamb.getInteractionClassHandle("HLAinteractionRoot.RozpocznijPrzerwe");
    }

    public int getRozpocznijPrzerweHandler(){
        return RozpocznijPrzerweHandler;
    }
}
