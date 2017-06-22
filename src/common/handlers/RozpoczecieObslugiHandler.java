package common.handlers;

import hla.rti.FederateNotExecutionMember;
import hla.rti.NameNotFound;
import hla.rti.RTIambassador;
import hla.rti.RTIinternalError;

/**
 * Created by Karolina on 22.06.2017.
 */
public class RozpoczecieObslugiHandler {
    public int RozpoczecieObslugiHandler;
    public int IDKlientaHandler;
    public int CzasObslugiHandler;

    public RozpoczecieObslugiHandler(RTIambassador rtiamb) throws NameNotFound, FederateNotExecutionMember, RTIinternalError {
        RozpoczecieObslugiHandler = rtiamb.getInteractionClassHandle("HLAinteractionRoot.RozpoczecieObslugi");
        //    IDKlientaHandler 			= rtiamb.getParameterHandle("IDKlienta", ZakonczanieObslugiKlientaHandle );
        //     CzasObslugiHandler 			= rtiamb.getParameterHandle("CzasObslugi", ZakonczanieObslugiKlientaHandle );
    }

    public int getRozpoczecieObslugiHandler(){
        return RozpoczecieObslugiHandler;
    }
}
