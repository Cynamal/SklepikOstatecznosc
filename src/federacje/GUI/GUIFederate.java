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
    public int liczbaKlientowWSklepie=0;
    public int liczbaKlientowRobiacychZakupy=0;
    public static String dzialanieLog="";
    public LinkedList<Kasa> kasy = new LinkedList<>();
    private int IteratorKasy = 1;
    public GUIFederate(GUIapp GUI)
    {
        this.GUI=GUI;
    }

    public void liczenieKlientowWSklepie(Klient klient){
        if(klient.NumerKolejki == -1){
            liczbaKlientowWSklepie++;
            liczbaKlientowRobiacychZakupy++;
        }
    }

    public void klientPrzestalRobicZakupy(){
        liczbaKlientowRobiacychZakupy--;
    }

    public void klientOpuscilSklep(){
        liczbaKlientowWSklepie--;
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
                                Klient klient = event.getKlient();
                                log("Dodano klienta: " + klient);
                                liczenieKlientowWSklepie(klient);
                                break;
                            case Kasa:
                                if(Kasa.addorChangeIfExist(event.getKasa(),kasy))
                                    log("Odebrano nowa kase:"+event.getKasa());
                                else
                                    log("Zaktualizowano kase:"+event.getKasa());
                                break;
                            case ZakonczanieObslugiKlienta:
                                log("Zakonczenie obslugi klienta: " + event.getZakonczanieObslugiKlienta());
                                klientOpuscilSklep();
                                break;
                            case UruchomNowaKase:
                                log("Odebrano zadanie uruchomienia kasy");
                                break;
                            case WejscieDoKolejki:
                                log("Wejscie do kolejki: " + event.getWejscieDoKolejki());
                                klientPrzestalRobicZakupy();
                                break;
                        }
                    } catch (Exception e) {

                    }
                }
                fedamb.externalEvents.clear();
            }
            try {
                advanceTime(1.0,fedamb);
            } catch (RTIexception rtIexception) {
                rtIexception.printStackTrace();
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
