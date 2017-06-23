package common.handlers;

import hla.rti.*;

/**
 * Created by Karolina on 22.06.2017.
 */
public class RozpocznijPrzerweHandler {
    public int RozpocznijPrzerweHandler;
    public int NumerKasyHandler;

    public RozpocznijPrzerweHandler(RTIambassador rtiamb) throws NameNotFound, FederateNotExecutionMember, RTIinternalError, InteractionClassNotDefined {
        RozpocznijPrzerweHandler = rtiamb.getInteractionClassHandle("HLAinteractionRoot.RozpocznijPrzerwe");
        NumerKasyHandler = rtiamb.getParameterHandle("NumerKasy", RozpocznijPrzerweHandler);
    }

    public int getRozpocznijPrzerweHandler(){
        return RozpocznijPrzerweHandler;
    }
}
