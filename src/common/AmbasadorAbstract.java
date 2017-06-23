package common;

import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.NullFederateAmbassador;
import objects.Kasa;
import objects.Klient;
import org.portico.impl.hla13.types.DoubleTime;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Marcin on 22.06.2017.
 */
public class AmbasadorAbstract extends NullFederateAmbassador {
    public static final String federationName = "FederacjaSklepowa";
    public static final String READY_TO_RUN = "ReadyToRun";
    public Publikacje publikacje= new Publikacje();
    public Subskrypcje subskrypcje= new Subskrypcje();
    public double federateLookahead = 1.0;
    public double federateTime = 0.0;
    public boolean Started=false;
    public boolean isAnnounced = false;
    public boolean isReadyToRun = false;
    public boolean isRegulating = false;
    public boolean isConstrained = false;
    public boolean isAdvancing = false;
    public boolean isRunning=true;

    protected ArrayList<ExternalEventAbstract> externalEvents = new ArrayList<>();
    private java.util.List<Map.Entry<String, Integer>> pairList = new java.util.ArrayList<>();
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
    public static boolean intequalifnullfalse(int one,int two)
    {
     //   if(one==null) return false;
      //  if(two==null) return false;
        //#TODO dokonczyc implementacje
        return false;
    }
    public void receiveInteraction(int interactionClass,
                                   ReceivedInteraction theInteraction,
                                   byte[] tag,
                                   LogicalTime theTime,
                                   EventRetractionHandle eventRetractionHandle) {
        StringBuilder builder = new StringBuilder("Interaction Received:");


        if(interactionClass ==subskrypcje.rozpoczecieSymulacjiHandler.getRozpoczecieSymulacjiHandler()) {

            builder.append("Start Symulacji z gui\n");
            isRunning=true;

        }
        if(interactionClass ==subskrypcje.zakonczenieSymulacjiHandler.getZakonczenieSymulacjiHandler()) {

            builder.append("Zakonczenie symulacji z gui\n");
            isRunning=false;
        }
        log(builder.toString());
        //#TODO
    }
    protected double convertTime(LogicalTime logicalTime) {
        return ((DoubleTime) logicalTime).getTime();
    }
    public void reflectAttributeValues(int theObject,
                                       ReflectedAttributes theAttributes,
                                       byte[] tag,
                                       LogicalTime theTime,
                                       EventRetractionHandle retractionHandle) {
        StringBuilder builder = new StringBuilder("Reflection for object: ");
        builder.append(" handle=" + theObject);
        builder.append(", tag=" + EncodingHelpers.decodeString(tag));

        if (theTime != null)
            builder.append(", time=" + convertTime(theTime));
        builder.append(", attributeCount=" + theAttributes.size());
        builder.append("\n");
        for (Map.Entry<String, Integer> m : pairList) {
            if (m.getValue() == theObject) {
                double time = convertTime(theTime);
                ExternalEventAbstract externalEvent = new ExternalEventAbstract();
                if (m.getKey().contains("Kasa")) {
                    try {
                        Kasa kasa = new Kasa();
                        int size = theAttributes.size();
                        kasa.NumerKasy = EncodingHelpers.decodeInt(theAttributes.getValue(1));
                        kasa.Dlugosc = EncodingHelpers.decodeInt(theAttributes.getValue(2));
                        kasa.CzyOtwarta = EncodingHelpers.decodeBoolean(theAttributes.getValue(0));
                        kasa.CzyPelna = EncodingHelpers.decodeBoolean(theAttributes.getValue(3));

                        externalEvent.KasaEvent(kasa, time);
                        this.externalEvents.add(externalEvent);
                    } catch (RTIexception aioob) {
                        //
                    }

                } else if (m.getKey().contains("Klient")) {
                    try {
                        Klient klient = new Klient();

                        int size = theAttributes.size();
                        klient.IDKlienta = EncodingHelpers.decodeInt(theAttributes.getValue(0));
                        klient.uprzywilejowany = EncodingHelpers.decodeBoolean(theAttributes.getValue(1));
                        klient.NumerKolejki = EncodingHelpers.decodeInt(theAttributes.getValue(2));
                        klient.NumerWKolejce = EncodingHelpers.decodeInt(theAttributes.getValue(3));
                        klient.Gotowka = EncodingHelpers.decodeInt(theAttributes.getValue(4));

                        // System.out.println("jest: "+klient);
                        externalEvent.KlientEvent(klient, time);
                        this.externalEvents.add(externalEvent);
                    } catch (RTIexception aioob) {
                        //
                    }
                }
            }
            log("" + builder);
        }


    }
}
