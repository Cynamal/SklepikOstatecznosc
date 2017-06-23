package common.handlers;

import hla.rti.*;

/**
 * Created by Karolina on 22.06.2017.
 */
public class ZakoczeniePrzerwyHandler {
    public int ZakoczeniePrzerwyHandler;
    public int CzasPrzerwyHandler;

    public ZakoczeniePrzerwyHandler(RTIambassador rtiamb) throws NameNotFound, FederateNotExecutionMember, RTIinternalError, InteractionClassNotDefined {
        ZakoczeniePrzerwyHandler = rtiamb.getInteractionClassHandle("HLAinteractionRoot.ZakoczeniePrzerwy");
        CzasPrzerwyHandler = rtiamb.getParameterHandle("CzasPrzerwy", ZakoczeniePrzerwyHandler);
    }

    public int getZakoczeniePrzerwyHandler(){
        return ZakoczeniePrzerwyHandler;
    }
}
