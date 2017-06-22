package common.handlers;

import hla.rti.FederateNotExecutionMember;
import hla.rti.NameNotFound;
import hla.rti.RTIambassador;
import hla.rti.RTIinternalError;

/**
 * Created by Karolina on 22.06.2017.
 */
public class ZakonczenieSymulacjiHandler {
    public int ZakonczenieSymulacjiHandler;

    public ZakonczenieSymulacjiHandler(RTIambassador rtiamb) throws NameNotFound, FederateNotExecutionMember, RTIinternalError {
        ZakonczenieSymulacjiHandler = rtiamb.getInteractionClassHandle("HLAinteractionRoot.ZakoczenieSymulacji");
    }

    public int getZakonczenieSymulacjiHandler(){
        return ZakonczenieSymulacjiHandler;
    }
}
