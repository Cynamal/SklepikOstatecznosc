package common.handlers;

import hla.rti.*;

/**
 * Created by Karolina on 22.06.2017.
 */
public class RozpoczecieObslugiHandler {
    public int RozpoczecieObslugiHandler;
    public int NumerKasyHandler;
    public int CzasOczekiwaniaHandler;
    public int IDKlientaHandler;

    public RozpoczecieObslugiHandler(RTIambassador rtiamb) throws NameNotFound, FederateNotExecutionMember, RTIinternalError, InteractionClassNotDefined {
        RozpoczecieObslugiHandler = rtiamb.getInteractionClassHandle("HLAinteractionRoot.RozpoczecieObslugi");
        NumerKasyHandler = rtiamb.getParameterHandle("NumerKasy", RozpoczecieObslugiHandler );
        CzasOczekiwaniaHandler = rtiamb.getParameterHandle("CzasOczekiwania", RozpoczecieObslugiHandler );
        IDKlientaHandler = rtiamb.getParameterHandle("IDKlienta", RozpoczecieObslugiHandler);
    }

    public int getRozpoczecieObslugiHandler(){
        return RozpoczecieObslugiHandler;
    }
}
