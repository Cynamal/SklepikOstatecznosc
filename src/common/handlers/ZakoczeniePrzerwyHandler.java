package common.handlers;

import hla.rti.FederateNotExecutionMember;
import hla.rti.NameNotFound;
import hla.rti.RTIambassador;
import hla.rti.RTIinternalError;

/**
 * Created by Karolina on 22.06.2017.
 */
public class ZakoczeniePrzerwyHandler {
    public int ZakoczeniePrzerwyHandler;

    public ZakoczeniePrzerwyHandler(RTIambassador rtiamb) throws NameNotFound, FederateNotExecutionMember, RTIinternalError {
        ZakoczeniePrzerwyHandler = rtiamb.getInteractionClassHandle("HLAinteractionRoot.ZakoczeniePrzerwy");
    }

    public int getZakoczeniePrzerwyHandler(){
        return ZakoczeniePrzerwyHandler;
    }
}
