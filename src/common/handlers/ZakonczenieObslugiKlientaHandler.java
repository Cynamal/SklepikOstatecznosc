package common.handlers;

import hla.rti.*;

/**
 * Created by Karolina on 22.06.2017.
 */
public class ZakonczenieObslugiKlientaHandler {

    public int ZakonczenieObslugiKlientaHandler;
    public int IDKlientaHandler;
    public int CzasObslugiHandler;
    public ZakonczenieObslugiKlientaHandler(RTIambassador rtiamb) throws NameNotFound, FederateNotExecutionMember, RTIinternalError, InteractionClassNotDefined {
        ZakonczenieObslugiKlientaHandler = rtiamb.getInteractionClassHandle("HLAinteractionRoot.ZakonczanieObslugiKlienta");
        IDKlientaHandler 			= rtiamb.getParameterHandle("IDKlienta", ZakonczenieObslugiKlientaHandler );
        CzasObslugiHandler 			= rtiamb.getParameterHandle("CzasObslugi", ZakonczenieObslugiKlientaHandler );
    }

    public int getZakonczenieObslugiKlientaHandler(){
        return ZakonczenieObslugiKlientaHandler;
    }
}
