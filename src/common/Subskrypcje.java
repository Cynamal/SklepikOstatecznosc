package common;
import common.handlers.*;
import hla.rti.*;
import hla.rti.jlc.*;

/**
 * Created by Marcin on 22.06.2017.
 */
public class Subskrypcje {

    public KlientHandler klientHandler;
    public KasaHandler kasaHandler;
    public ZakonczenieSymulacjiHandler zakonczenieSymulacjiHandler;
    public RozpoczecieSymulacjiHandler rozpoczecieSymulacjiHandler;
    public ZakonczenieObslugiKlientaHandler zakonczenieObslugiKlientaHandler;
    public RozpoczecieObslugiHandler rozpoczecieObslugiHandler;
    public WejscieDoKolejkiHandler wejscieDoKolejkiHandler;
    public RozpocznijPrzerweHandler rozpocznijPrzerweHandler;
    public ZakoczeniePrzerwyHandler zakoczeniePrzerwyHandler;
    public UruchomNowaKaseHandler uruchomNowaKaseHandler;

    private void subscribeKasa(RTIambassador rtiamb) {
        try {
            kasaHandler = new KasaHandler(rtiamb);
            rtiamb.subscribeObjectClassAttributes(kasaHandler.getKasaHandler(), kasaHandler.getAttributeHandleSet());
        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (ObjectClassNotDefined objectClassNotDefined) {
            objectClassNotDefined.printStackTrace();
        } catch (AttributeNotDefined attributeNotDefined) {
            attributeNotDefined.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        }
    }

    private void subscribeKlient(RTIambassador rtiamb) {
        try {
            klientHandler = new KlientHandler(rtiamb);
            rtiamb.subscribeObjectClassAttributes(klientHandler.getKlientHandler(), klientHandler.getAttributeHandleSet());
        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (ObjectClassNotDefined objectClassNotDefined) {
            objectClassNotDefined.printStackTrace();
        } catch (AttributeNotDefined attributeNotDefined) {
            attributeNotDefined.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        }
    }

    private void subscribeZakoczenieSymulacji(RTIambassador rtiamb) {
        try {
            zakonczenieSymulacjiHandler = new ZakonczenieSymulacjiHandler(rtiamb);
            rtiamb.subscribeInteractionClass(zakonczenieSymulacjiHandler.getZakonczenieSymulacjiHandler());
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (FederateLoggingServiceCalls federateLoggingServiceCalls) {
            federateLoggingServiceCalls.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        }
    }

    private void subscribeRozpoczecieSymulacji(RTIambassador rtiamb) {
        try {
            rozpoczecieSymulacjiHandler = new RozpoczecieSymulacjiHandler(rtiamb);
            rtiamb.subscribeInteractionClass(rozpoczecieSymulacjiHandler.getRozpoczecieSymulacjiHandler());
        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (FederateLoggingServiceCalls federateLoggingServiceCalls) {
            federateLoggingServiceCalls.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        }
    }

    private void subscribeZakonczanieObslugiKlienta(RTIambassador rtiamb) {
        try {
            zakonczenieObslugiKlientaHandler = new ZakonczenieObslugiKlientaHandler(rtiamb);
            rtiamb.subscribeInteractionClass(zakonczenieObslugiKlientaHandler.getZakonczenieObslugiKlientaHandler());
        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (FederateLoggingServiceCalls federateLoggingServiceCalls) {
            federateLoggingServiceCalls.printStackTrace();
        }
    }

    private void subscribeRozpoczecieObslugi(RTIambassador rtiamb) {
        try {
            rozpoczecieObslugiHandler = new RozpoczecieObslugiHandler(rtiamb);
            rtiamb.subscribeInteractionClass(rozpoczecieObslugiHandler.getRozpoczecieObslugiHandler());
        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (FederateLoggingServiceCalls federateLoggingServiceCalls) {
            federateLoggingServiceCalls.printStackTrace();
        }
    }

    private void subscribeWejscieDoKolejki(RTIambassador rtiamb) {
        try {
            wejscieDoKolejkiHandler = new WejscieDoKolejkiHandler(rtiamb);
            rtiamb.subscribeInteractionClass(wejscieDoKolejkiHandler.getWejscieDoKolejkiHandler());
        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (FederateLoggingServiceCalls federateLoggingServiceCalls) {
            federateLoggingServiceCalls.printStackTrace();
        }
    }

    private void subscribeRozpocznijPrzerwe(RTIambassador rtiamb) {
        try {
            rozpocznijPrzerweHandler = new RozpocznijPrzerweHandler(rtiamb);
            rtiamb.subscribeInteractionClass(rozpocznijPrzerweHandler.getRozpocznijPrzerweHandler());
        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (FederateLoggingServiceCalls federateLoggingServiceCalls) {
            federateLoggingServiceCalls.printStackTrace();
        }
    }

    private void subscribeZakoczeniePrzerwy(RTIambassador rtiamb) {
        try {
            zakoczeniePrzerwyHandler = new ZakoczeniePrzerwyHandler(rtiamb);
            rtiamb.subscribeInteractionClass(zakoczeniePrzerwyHandler.getZakoczeniePrzerwyHandler());
        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (FederateLoggingServiceCalls federateLoggingServiceCalls) {
            federateLoggingServiceCalls.printStackTrace();
        }
    }

    private void subscribeUruchomNowaKase(RTIambassador rtiamb) {
        try {
            uruchomNowaKaseHandler = new UruchomNowaKaseHandler(rtiamb);
            rtiamb.subscribeInteractionClass(uruchomNowaKaseHandler.getUruchomNowaKaseHandler());
        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (FederateLoggingServiceCalls federateLoggingServiceCalls) {
            federateLoggingServiceCalls.printStackTrace();
        }
    }
}
