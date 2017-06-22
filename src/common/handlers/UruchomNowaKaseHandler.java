package common.handlers;

import hla.rti.FederateNotExecutionMember;
import hla.rti.NameNotFound;
import hla.rti.RTIambassador;
import hla.rti.RTIinternalError;

/**
 * Created by Karolina on 22.06.2017.
 */
public class UruchomNowaKaseHandler {
    public int UruchomNowaKaseHandler;

    public UruchomNowaKaseHandler(RTIambassador rtiamb) throws NameNotFound, FederateNotExecutionMember, RTIinternalError {
        UruchomNowaKaseHandler = rtiamb.getInteractionClassHandle("HLAinteractionRoot.UruchomNowaKase");
    }

    public int getUruchomNowaKaseHandler(){
        return UruchomNowaKaseHandler;
    }
}
