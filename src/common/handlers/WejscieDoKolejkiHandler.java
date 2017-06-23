package common.handlers;

import hla.rti.*;

/**
 * Created by Karolina on 22.06.2017.
 */
public class WejscieDoKolejkiHandler {

    public int WejscieDoKolejkiHandler;
    public int CzasZakupowHandler;
    public int NumerKasyHandler;
    public int IDKlientaHandler;

    public WejscieDoKolejkiHandler(RTIambassador rtiamb) throws NameNotFound, FederateNotExecutionMember, RTIinternalError, InteractionClassNotDefined {
        WejscieDoKolejkiHandler = rtiamb.getInteractionClassHandle("HLAinteractionRoot.WejscieDoKolejki");
        CzasZakupowHandler = rtiamb.getParameterHandle("CzasZakupow", WejscieDoKolejkiHandler);
        NumerKasyHandler = rtiamb.getParameterHandle("NumerKasy", WejscieDoKolejkiHandler);
        IDKlientaHandler = rtiamb.getParameterHandle("IDKlienta", WejscieDoKolejkiHandler);
    }

    public int getWejscieDoKolejkiHandler(){
        return WejscieDoKolejkiHandler;
    }
}
