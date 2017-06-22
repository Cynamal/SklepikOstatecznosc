package common.handlers;

import hla.rti.FederateNotExecutionMember;
import hla.rti.NameNotFound;
import hla.rti.RTIambassador;
import hla.rti.RTIinternalError;

/**
 * Created by Karolina on 22.06.2017.
 */
public class ZakonczenieObslugiKlientaHandler {

    public int ZakonczenieObslugiKlientaHandler;

    public ZakonczenieObslugiKlientaHandler(RTIambassador rtiamb) throws NameNotFound, FederateNotExecutionMember, RTIinternalError {
        ZakonczenieObslugiKlientaHandler = rtiamb.getInteractionClassHandle("HLAinteractionRoot.ZakonczanieObslugiKlienta");
    }

    public int getZakonczenieObslugiKlientaHandler(){
        return ZakonczenieObslugiKlientaHandler;
    }
}
