package common;
import common.handlers.*;
import hla.rti.*;

/**
 * Created by Marcin on 22.06.2017.
 */
public class Publikacje {

    public KlientHandler klientHandler;
    public RozpoczecieObslugiHandler rozpoczecieObslugiHandler;
    public WejscieDoKolejkiHandler wejscieDoKolejkiHandler;

    private void publishKlient(RTIambassador rtiamb) {
        try {
            klientHandler = new KlientHandler(rtiamb);
            rtiamb.publishObjectClass(klientHandler.getKlientHandler(), klientHandler.getAttributeHandleSet());
        } catch (RTIexception nameNotFound) {
            nameNotFound.printStackTrace();
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

}
