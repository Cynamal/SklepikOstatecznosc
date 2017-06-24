package federacje.GUI;

import Interactions.*;
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

    public static void logowanieDzialania(String x){
        dzialanieLog=x+"\n"+dzialanieLog;
    }

    public void liczenieKlientowWSklepie(Klient klient){
        if(klient.NumerKolejki == -1){
            liczbaKlientowWSklepie++;
            liczbaKlientowRobiacychZakupy++;
            logowanieDzialania("Do sklepu wszedl klient o ID: " + klient.IDKlienta + " i rozpoczal zakupy");
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

        while(fedamb.Started) {
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
                                Kasa kasa = event.getKasa();
                                if(Kasa.addorChangeIfExist(kasa,kasy)){
                                    logowanieDzialania("Otwarto nowa kase o numerze: " + kasa.NumerKasy);
                                    log("Odebrano nowa kase:"+kasa);
                                }
                                else

                                    log("Zaktualizowano kase:"+kasa);
                                break;
                            case ZakonczanieObslugiKlienta:
                                ZakonczanieObslugiKlienta zak = event.getZakonczanieObslugiKlienta();
                                logowanieDzialania("Klient o ID " + zak.IDKlienta + " zostal obsluzony i opuscil sklep. Czas obslugi trwal " + zak.CzasObslugi);
                                log("Zakonczenie obslugi klienta: " + zak);
                                klientOpuscilSklep();
                                break;
                            case UruchomNowaKase:
                                log("Odebrano zadanie uruchomienia kasy");
                                logowanieDzialania("Wlasciciel sklepu podjal decyzje o uruchomieniu nowej kasy");
                                break;
                            case WejscieDoKolejki:
                                WejscieDoKolejki wej = event.getWejscieDoKolejki();
                                log("Wejscie do kolejki: " + wej);
                                klientPrzestalRobicZakupy();
                                logowanieDzialania("Klient o ID "+wej.IDKlienta+" zakonczyl zakupy po czasie "+wej.CzasZakupow+". Stanal w kolejce do kasy o numerze "+wej.NumerKasy);
                                break;
                            case RozpoczecieObslugi:
                                RozpoczecieObslugi roz = event.getRozpoczecieObslugi();
                                logowanieDzialania("Kasa o numerze "+roz.NumerKasy+" rozpoczela obsluge klienta o ID "+roz.IDKlienta+". Czas oczekiwania klienta w kolejce wynosil "+roz.CzasOczekiwania);
                                break;
                            case RozpocznijPrzerwe:
                                RozpocznijPrzerwe rop = event.getRozpocznijPrzerwe();
                                logowanieDzialania("Kasa o numerze "+rop.NumerKasy +" zostala oznaczona do zamkniecia");
                                break;
                            case ZakoczeniePrzerwy:
                                ZakoczeniePrzerwy zap = event.getZakoczeniePrzerwy();
                                logowanieDzialania("Kasa o numerze "+zap.NumerKasy+" zakonczyla przerwe. Czas przerwy wynosil "+zap.CzasPrzerwy);
                                break;

                        }
                    } catch (Exception e) {

                    }
                }
                czekajAzWszyscySieUruchomia();
                fedamb.externalEvents.clear();
            }
            try {
                advanceTime(1.0,fedamb);
            } catch (RTIexception rtIexception) {
                rtIexception.printStackTrace();
            }
        }
        try {
            wyslijZadanieZakonczeniaSymulacji(fedamb.federateTime+1.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            advanceTime(1.0,fedamb);
        } catch (RTIexception rtIexception) {
            rtIexception.printStackTrace();
        }
        System.out.print("Zamykanie");


    }
    private void wyslijZadanieZakonczeniaSymulacji(double timeStep) throws Exception {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();
        LogicalTime time = convertTime( timeStep );
        rtiamb.sendInteraction(fedamb.publikacje.zakonczenieSymulacjiHandler.getZakonczenieSymulacjiHandler(), parameters, "tag".getBytes(), time );
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
        fedamb.subskrypcje.subscribeWejscieDoKolejki(super.rtiamb);
    }

}
