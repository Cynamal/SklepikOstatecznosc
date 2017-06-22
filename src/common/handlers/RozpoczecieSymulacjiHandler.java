package common.handlers;

import hla.rti.FederateNotExecutionMember;
import hla.rti.NameNotFound;
import hla.rti.RTIambassador;
import hla.rti.RTIinternalError;

/**
 * Created by Karolina on 22.06.2017.
 */
public class RozpoczecieSymulacjiHandler {

    public int RozpoczecieSymulacjiHandler;

    public RozpoczecieSymulacjiHandler(RTIambassador rtiamb) throws NameNotFound, FederateNotExecutionMember, RTIinternalError {
        RozpoczecieSymulacjiHandler = rtiamb.getInteractionClassHandle("HLAinteractionRoot.RozpoczecieSymulacji");
    }

    public int getRozpoczecieSymulacjiHandler(){
        return RozpoczecieSymulacjiHandler;
    }
}
