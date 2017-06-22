package common;

import hla.rti.LogicalTime;
import hla.rti.RTIambassador;
import hla.rti.jlc.NullFederateAmbassador;
import org.portico.impl.hla13.types.DoubleTime;

/**
 * Created by Marcin on 22.06.2017.
 */
public class AmbasadorAbstract extends NullFederateAmbassador {
    public static final String federationName = "FederacjaSklepowa";
    public static final String READY_TO_RUN = "ReadyToRun";
    public Publikacje publikacje;
    public Subskrypcje subskrypcje;
    public double federateLookahead = 1.0;
    public double federateTime = 0.0;

    public boolean isAnnounced = false;
    public boolean isReadyToRun = false;
    public boolean isRegulating = false;
    public boolean isConstrained = false;
    public boolean isAdvancing = false;
    public boolean isRunning=true;


    private static void log (String x){
        System.out.println(x);

    }

    public void synchronizationPointRegistrationFailed(String label) {
        log("Failed to register sync point: " + label);
    }

    public void synchronizationPointRegistrationSucceeded(String label) {
        log("Successfully registered sync point: " + label);
    }

    public void announceSynchronizationPoint(String label, byte[] tag) {
        log("Synchronization point announced: " + label);
        if (label.equals(READY_TO_RUN))
            this.isAnnounced = true;
    }

    public void federationSynchronized(String label) {
        log("Federation Synchronized: " + label);
        if (label.equals(READY_TO_RUN))
            this.isReadyToRun = true;
    }

    public double getFederateTime() {
        return federateTime;
    }

    public double getFederateLookahead() {
        return federateLookahead;
    }

    public boolean isRegulating() {
        return isRegulating;
    }

    public boolean isConstrained() {
        return isConstrained;
    }

    public boolean isAdvancing() {
        return isAdvancing;
    }

    public boolean isAnnounced() {
        return isAnnounced;
    }

    public boolean isReadyToRun() {
        return isReadyToRun;
    }

    @Override
    public void timeRegulationEnabled(LogicalTime theFederateTime) {
        this.federateTime = convertTime(theFederateTime);
        this.isRegulating = true;
    }

    @Override
    public void timeConstrainedEnabled(LogicalTime theFederateTime) {
        this.federateTime = convertTime(theFederateTime);
        this.isConstrained = true;
    }

    @Override
    public void timeAdvanceGrant(LogicalTime theTime) {
        this.federateTime = convertTime(theTime);
        this.isAdvancing = false;
    }

    protected double convertTime(LogicalTime logicalTime) {
        return ((DoubleTime) logicalTime).getTime();
    }

}
