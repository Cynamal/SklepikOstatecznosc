package federacje.GUI;

import common.AmbasadorAbstract;
import common.ExternalEventAbstract;
import common.FederateAbstract;
import hla.rti.*;
import hla.rti.jlc.RtiFactoryFactory;
import objects.Kasa;
import objects.Klient;

import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Marcin on 22.06.2017.
 */
public class GUIFederate extends FederateAbstract {
    public static final String federateName = "GUIFederat";
    public AmbasadorAbstract fedamb;
    public GUIapp GUI;
    public Lock CheckStartedLock = new ReentrantLock();

    public LinkedList<Kasa> kasy = new LinkedList<>();
    private int IteratorKasy = 1;
    public GUIFederate(GUIapp GUI)
    {
        this.GUI=GUI;
    }
    public void runFederate(){
        fedamb = new AmbasadorAbstract();
        CommonrunFederate(federateName,fedamb);
        publishAndSubscribe();
        CheckStartedLock.lock();
        boolean StartedForThread=fedamb.Started;
        CheckStartedLock.unlock();
        System.out.println("Czekanie na rozpoczecie symulacji");
        while(!StartedForThread)
        {



            CheckStartedLock.lock();
            StartedForThread=fedamb.Started;
            CheckStartedLock.unlock();
        }

        try {

            wyslijZadanieRozpoczeciaSymulacji(1.0);
            System.out.println("Wystartowano");
        } catch (Exception rtIexception) {
            rtIexception.printStackTrace();
        }




        while(this.isRunning) {
            if (fedamb.externalEvents.size() > 0) {
                Collections.sort(fedamb.externalEvents, new ExternalEventAbstract.ExternalEventComparator());
                for (ExternalEventAbstract event : fedamb.externalEvents) {
                    try {


                        // System.out.println("w for");
                        switch (event.getEventType()) {
                            case Klient:
                                System.out.println("Dodano klienta: " + event.getKlient());
                                break;
                            case Kasa:
                                System.out.println("Dodano kase: " + event.getKasa());
                                break;
                            case ZakonczanieObslugiKlienta:
                                System.out.println("Zakonczenie obslugi klienta: " + event.getZakonczanieObslugiKlienta());
                                break;
                        }
                    } catch (Exception e) {

                    }
                }

            }
        }
    }
    private void wyslijZadanieRozpoczeciaSymulacji(double timeStep) throws Exception{
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();
        LogicalTime time = convertTime( timeStep );
        rtiamb.sendInteraction(fedamb.publikacje.rozpoczecieSymulacjiHandler.getRozpoczecieSymulacjiHandler(), parameters, "tag".getBytes(), time );


    }
    private void publishAndSubscribe() {
        //obj

        //interactions
        fedamb.publikacje.publishRozpoczecieSymulacji(super.rtiamb);
        fedamb.publikacje.publishZakonczenieSymulacji(super.rtiamb);

        //obj
        fedamb.subskrypcje.subscribeKasa(super.rtiamb);
        fedamb.subskrypcje.subscribeKlient(super.rtiamb);
        // interactions
        fedamb.subskrypcje.subscribeUruchomNowaKase(super.rtiamb);
        fedamb.subskrypcje.subscribeRozpocznijPrzerwe(super.rtiamb);
        fedamb.subskrypcje.subscribeRozpoczecieObslugi(super.rtiamb);
        fedamb.subskrypcje.subscribeZakonczanieObslugiKlienta(super.rtiamb);
        fedamb.subskrypcje.subscribeZakoczeniePrzerwy(super.rtiamb);
    }

}
