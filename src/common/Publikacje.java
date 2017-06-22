package common;
import common.handlers.*;
import hla.rti.*;

/**
 * Created by Marcin on 22.06.2017.
 */
public class Publikacje {

    public KlientHandler klientHandler;
    public KasaHandler kasaHandler;
    public RozpoczecieObslugiHandler rozpoczecieObslugiHandler;
    public WejscieDoKolejkiHandler wejscieDoKolejkiHandler;
    public ZakonczenieSymulacjiHandler zakonczenieSymulacjiHandler;
    public RozpoczecieSymulacjiHandler rozpoczecieSymulacjiHandler;
    public ZakonczenieObslugiKlientaHandler zakonczenieObslugiKlientaHandler;
    public RozpocznijPrzerweHandler rozpocznijPrzerweHandler;
    public ZakoczeniePrzerwyHandler zakoczeniePrzerwyHandler;
    public UruchomNowaKaseHandler uruchomNowaKaseHandler;

    private void publishKlient(RTIambassador rtiamb) {
        try {
            klientHandler = new KlientHandler(rtiamb);
            rtiamb.publishObjectClass(klientHandler.getKlientHandler(), klientHandler.getAttributeHandleSet());
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
        } catch (OwnershipAcquisitionPending ownershipAcquisitionPending) {
            ownershipAcquisitionPending.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        }
    }

    private void publishKasa(RTIambassador rtiamb) {
        try {
            kasaHandler = new KasaHandler(rtiamb);
            rtiamb.publishObjectClass(kasaHandler.getKasaHandler(), kasaHandler.getAttributeHandleSet());
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
        } catch (OwnershipAcquisitionPending ownershipAcquisitionPending) {
            ownershipAcquisitionPending.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        }
    }

    private void publishRozpoczecieObslugi(RTIambassador rtiamb) {

        try {
            rozpoczecieObslugiHandler = new RozpoczecieObslugiHandler(rtiamb);
            rtiamb.publishInteractionClass(rozpoczecieObslugiHandler.getRozpoczecieObslugiHandler());

        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        }
    }

    private void publishWejscieDoKolejki(RTIambassador rtiamb) {
        try {
            wejscieDoKolejkiHandler = new WejscieDoKolejkiHandler(rtiamb);
            rtiamb.publishInteractionClass(wejscieDoKolejkiHandler.getWejscieDoKolejkiHandler());
        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        }
    }

    private void publishZakonczenieSymulacji(RTIambassador rtiamb) {
        try {
            zakonczenieSymulacjiHandler = new ZakonczenieSymulacjiHandler(rtiamb);
            rtiamb.publishInteractionClass(zakonczenieSymulacjiHandler.getZakonczenieSymulacjiHandler());
        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        }
    }

    private void publishRozpoczecieSymulacji(RTIambassador rtiamb) {
        try {
            rozpoczecieSymulacjiHandler = new RozpoczecieSymulacjiHandler(rtiamb);
            rtiamb.publishInteractionClass(rozpoczecieSymulacjiHandler.getRozpoczecieSymulacjiHandler());
        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        }
    }

    private void publishZakonczenieObslugiKlienta(RTIambassador rtiamb) {
        try {
            zakonczenieObslugiKlientaHandler = new ZakonczenieObslugiKlientaHandler(rtiamb);
            rtiamb.publishInteractionClass(zakonczenieObslugiKlientaHandler.getZakonczenieObslugiKlientaHandler());
        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        }
    }

    private void publishRozpocznijPrzerwe(RTIambassador rtiamb) {
        try {
            rozpocznijPrzerweHandler = new RozpocznijPrzerweHandler(rtiamb);
            rtiamb.publishInteractionClass(rozpocznijPrzerweHandler.getRozpocznijPrzerweHandler());
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
        }
    }

    private void publishZakoczeniePrzerwy(RTIambassador rtiamb) {
        try {
            zakoczeniePrzerwyHandler = new ZakoczeniePrzerwyHandler(rtiamb);
            rtiamb.publishInteractionClass(zakoczeniePrzerwyHandler.getZakoczeniePrzerwyHandler());
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
        }
    }

    private void publishUruchomNowaKase(RTIambassador rtiamb) {
        try {
            uruchomNowaKaseHandler = new UruchomNowaKaseHandler(rtiamb);
            rtiamb.publishInteractionClass(uruchomNowaKaseHandler.getUruchomNowaKaseHandler());
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
        }
    }
}
