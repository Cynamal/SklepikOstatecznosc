package common.handlers;

import hla.rti.FederateNotExecutionMember;
import hla.rti.NameNotFound;
import hla.rti.RTIambassador;
import hla.rti.RTIinternalError;

/**
 * Created by Karolina on 22.06.2017.
 */
public class WejscieDoKolejkiHandler {

    public int WejscieDoKolejkiHandler;
    public int IDKlientaHandler;
    public int CzasObslugiHandler;

    public WejscieDoKolejkiHandler(RTIambassador rtiamb) throws NameNotFound, FederateNotExecutionMember, RTIinternalError {
        WejscieDoKolejkiHandler = rtiamb.getInteractionClassHandle("HLAinteractionRoot.WejscieDoKolejki");
        //    IDKlientaHandler 			= rtiamb.getParameterHandle("IDKlienta", ZakonczanieObslugiKlientaHandle );
        //     CzasObslugiHandler			= rtiamb.getParameterHandle("CzasObslugi", ZakonczanieObslugiKlientaHandle );
    }

    public int getWejscieDoKolejkiHandler(){
        return WejscieDoKolejkiHandler;
    }
}
