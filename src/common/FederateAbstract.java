package common;

import hla.rti.*;
import hla.rti.jlc.RtiFactoryFactory;
import org.portico.impl.hla13.types.DoubleTime;
import org.portico.impl.hla13.types.DoubleTimeInterval;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

/**
 * Created by Marcin on 22.06.2017.
 */
public class FederateAbstract  {
    public static final String NAZWA_FEDERACJI = "FederacjaSklepowa";
    public static final String MODEL_FOM = "src/xml/ModelFom.xml";
    public static final String READY_TO_RUN = "ReadyToRun";
    public static final String READY_TO_STOP = "ReadyToStop";
    public double federateLookahead = 1.0;
    public double federateTime = 0.0;
    public RTIambassador rtiamb;
    public static String logS="";
    public boolean isAnnounced = false;
    public boolean isReadyToRun = false;
    public boolean isRegulating = false;
    public boolean isConstrained = false;
    public boolean isAdvancing = false;
    public boolean isRunning=true;
    protected static void log(String x) {
        System.out.println(x);
        logS+=x+"/n";
    }



    public void advanceTime(double timestep, AmbasadorAbstract fedamb) throws RTIexception {
        fedamb.isAdvancing = true;
        LogicalTime newTime = convertTime(fedamb.federateTime + timestep);
        rtiamb.timeAdvanceRequest(newTime);

        while (fedamb.isAdvancing)
            rtiamb.tick();
    }


    public LogicalTime convertTime(double time) {
        return new DoubleTime(time);
    }
    protected LogicalTimeInterval convertInterval(double time) {
        return new DoubleTimeInterval(time);
    }
    protected void ustawianiePolitykiCzasowej(AmbasadorAbstract fedamb) {
        LogicalTime currentTime = convertTime(fedamb.federateTime);
        LogicalTimeInterval lookahead = convertInterval(fedamb.federateLookahead);

        try {
            this.rtiamb.enableTimeRegulation(currentTime, lookahead);
            while (!fedamb.isRegulating) {
                rtiamb.tick();
            }
        } catch (TimeRegulationAlreadyEnabled | EnableTimeRegulationPending | TimeAdvanceAlreadyInProgress | InvalidLookahead | InvalidFederationTime | FederateNotExecutionMember | SaveInProgress | RTIinternalError | RestoreInProgress | ConcurrentAccessAttempted timeRegulationAlreadyEnabled) {
            timeRegulationAlreadyEnabled.printStackTrace();
        }

        try {
            this.rtiamb.enableTimeConstrained();
            while (!fedamb.isConstrained) {
                rtiamb.tick();
            }
        } catch (TimeConstrainedAlreadyEnabled | EnableTimeConstrainedPending | FederateNotExecutionMember | TimeAdvanceAlreadyInProgress | RestoreInProgress | SaveInProgress | ConcurrentAccessAttempted | RTIinternalError timeConstrainedAlreadyEnabled) {
            timeConstrainedAlreadyEnabled.printStackTrace();
        }
    }

    /**
     * Czesc wspolna dla uruchamiania federata
     */
    public void CommonrunFederate(String federateName,AmbasadorAbstract fedamb)
    {
        tworzenieAmbasadoraOrazTworzenieFederata();
        dolaczenieDoFederacji(federateName,fedamb);
        ogloszeniePunktuSynchronizacji(fedamb);
        czekajAzWszyscySieUruchomia();
        osiagnieciePunktuSynchronizacji(fedamb);
        ustawianiePolitykiCzasowej(fedamb);
    }
    private void dolaczenieDoFederacji(String federateName,AmbasadorAbstract fedamb){
        try {
            rtiamb.joinFederationExecution(federateName, NAZWA_FEDERACJI,fedamb);

        } catch (FederateAlreadyExecutionMember | FederationExecutionDoesNotExist | SaveInProgress | RTIinternalError | RestoreInProgress | ConcurrentAccessAttempted federateAlreadyExecutionMember) {
            federateAlreadyExecutionMember.printStackTrace();
        }
        log("Dołączył do federacji jako: " + federateName);
    }
    protected void czekajAzWszyscySieUruchomia() {
        log(" >>>>>>>>>> Press Enter to Continue <<<<<<<<<<");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            reader.readLine();
        } catch (Exception e) {
            log("Error while waiting for user input: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void osiagnieciePunktuSynchronizacji(AmbasadorAbstract fedamb) {
        try {
            rtiamb.synchronizationPointAchieved(READY_TO_RUN);
        } catch (SynchronizationLabelNotAnnounced | ConcurrentAccessAttempted | RTIinternalError | RestoreInProgress | SaveInProgress | FederateNotExecutionMember synchronizationLabelNotAnnounced) {
            synchronizationLabelNotAnnounced.printStackTrace();
        }
        log("Osiągnięto punkt synchronizacji: " + READY_TO_RUN + ", czekanie na federacje...");
        while (!fedamb.isReadyToRun) {
            try {
                rtiamb.tick();
            } catch (RTIinternalError | ConcurrentAccessAttempted rtIinternalError) {
                rtIinternalError.printStackTrace();
            }
        }
    }
    protected void czekajAzWszyscySieSynchonizuja() {
        log(" >>>>>>>>>> Press Enter to Continue <<<<<<<<<<");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            reader.readLine();
        } catch (Exception e) {
            log("Error while waiting for user input: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void osiagnieciePunktuSynchronizacjiWyjscia(AmbasadorAbstract fedamb) {
        try {
            rtiamb.synchronizationPointAchieved(READY_TO_STOP);
        } catch (SynchronizationLabelNotAnnounced | ConcurrentAccessAttempted | RTIinternalError | RestoreInProgress | SaveInProgress | FederateNotExecutionMember synchronizationLabelNotAnnounced) {
            synchronizationLabelNotAnnounced.printStackTrace();
        }
        log("Osiągnięto punkt synchronizacji: " + READY_TO_STOP + ", czekanie na federacje...");
        while (!fedamb.isReadyToRun) {
            try {
                rtiamb.tick();
            } catch (RTIinternalError | ConcurrentAccessAttempted rtIinternalError) {
                rtIinternalError.printStackTrace();
            }
        }
    }

    private void ogloszeniePunktuSynchronizacjiWyjscia(AmbasadorAbstract fedamb) {
        try {
            rtiamb.registerFederationSynchronizationPoint(READY_TO_STOP, null);
        } catch (FederateNotExecutionMember | SaveInProgress | RTIinternalError | RestoreInProgress | ConcurrentAccessAttempted federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        }

        while (!fedamb.isAnnounced) {
            try {
                rtiamb.tick();
            } catch (RTIinternalError | ConcurrentAccessAttempted rtIinternalError) {
                rtIinternalError.printStackTrace();
            }
        }
    }
    private void ogloszeniePunktuSynchronizacji(AmbasadorAbstract fedamb) {
        try {
            rtiamb.registerFederationSynchronizationPoint(READY_TO_RUN, null);
        } catch (FederateNotExecutionMember | SaveInProgress | RTIinternalError | RestoreInProgress | ConcurrentAccessAttempted federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        }

        while (!fedamb.isAnnounced) {
            try {
                rtiamb.tick();
            } catch (RTIinternalError | ConcurrentAccessAttempted rtIinternalError) {
                rtIinternalError.printStackTrace();
            }
        }
    }
    protected void tworzenieAmbasadoraOrazTworzenieFederata() {
        try {
            rtiamb = RtiFactoryFactory.getRtiFactory().createRtiAmbassador();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        }
        try {
            File fom = new File(MODEL_FOM);
            rtiamb.createFederationExecution(NAZWA_FEDERACJI, fom.toURI().toURL());
            log("Created Federation");
        } catch (FederationExecutionAlreadyExists exists) {
            log("Didn't create federation, it already existed");
        } catch (MalformedURLException urle) {
            log("Exception processing fom: " + urle.getMessage());
            urle.printStackTrace();
        } catch (ConcurrentAccessAttempted | RTIinternalError | CouldNotOpenFED | ErrorReadingFED concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        }
    }
}
