package federacje.statystyka;

import Interactions.RozpoczecieObslugi;
import common.AmbasadorAbstract;
import common.ExternalEventAbstract;
import common.FederateAbstract;
import federacje.kasa.KasaFederate;
import hla.rti.RTIexception;
import statistic.Colections.*;

import statistic.Obj.*;

import java.util.Collections;

/**
 * Created by Marcin on 22.06.2017.
 */
public class StatystykaFederate extends FederateAbstract {
    //Statystyki----------
    ZadaniaUruchomieniaKasy zadaniaUruchomieniaKasy=new ZadaniaUruchomieniaKasy();
    ZakonczeniaObslugiKliena zakonczeniaObslugiKliena=new ZakonczeniaObslugiKliena();
    UpdateKasaList updateKasaList=new UpdateKasaList();
    UpdateKlientList updateKlientList=new UpdateKlientList();
    ZakoczeniaPrzerwy zakoczeniaPrzerwy=new ZakoczeniaPrzerwy();
    WejsciaDoKolejki wejsciaDoKolejki=new WejsciaDoKolejki();
    RozpoczeciaPrzerwy rozpoczeciaPrzerwy=new RozpoczeciaPrzerwy();
    RozpoczeciaOblugi rozpoczeciaOblugi=new RozpoczeciaOblugi();
    //--------------------
    public static final String federateName = "StatystykaFederate";
    public AmbasadorAbstract fedamb;

    public void runFederate(){
        fedamb = new AmbasadorAbstract();
        CommonrunFederate(federateName,fedamb);
        publishAndSubscribe();
        czekajNAGUI(fedamb);
        while(this.isRunning) {
            if (fedamb.externalEvents.size() > 0) {
                Collections.sort(fedamb.externalEvents, new ExternalEventAbstract.ExternalEventComparator());
                for (ExternalEventAbstract event : fedamb.externalEvents) {
                    try {
                        // System.out.println("w for");
                        switch (event.getEventType()) {
                            case Klient:
                                log("Dodano klienta: " + event.getKlient());
                                updateKlientList.add(new UpdateKlient(event.getTime(), event.getKlient()));
                                break;
                            case Kasa:
                                log("Dodano kase: " + event.getKasa());
                                updateKasaList.add(new UpdateKasa(event.getTime(),event.getKasa()));
                                break;
                            case ZakonczanieObslugiKlienta:
                                log("Zakonczenie obslugi klienta: " + event.getZakonczanieObslugiKlienta());
                                zakonczeniaObslugiKliena.add(new EvZakonczenieObslugiKlienta(event.getTime(),event.getZakonczanieObslugiKlienta()));
                                break;
                            case UruchomNowaKase:
                                log("Odebrano zadanie uruchomienia kasy");
                                zadaniaUruchomieniaKasy.add(new ZadanieUruchomieniaKasy(event.getTime()));
                                break;
                            case ZakoczeniePrzerwy:
                                log("Zakoczenie przerwy: "+event.getZakoczeniePrzerwy());
                                zakoczeniaPrzerwy.add(new EvZakoczeniePrzerwy(event.getTime(), event.getZakoczeniePrzerwy()));
                                break;
                            case WejscieDoKolejki:
                                log("Wejscie do kolejki klienta: " + event.getWejscieDoKolejki());
                                wejsciaDoKolejki.add(new EvWejscieDoKolejki(event.getTime(), event.getWejscieDoKolejki()));
                                break;
                            case RozpocznijPrzerwe:
                                log("Rozpocznij przerwe: "+event.getRozpocznijPrzerwe());
                                rozpoczeciaPrzerwy.add(new EvRozpocznijPrzerwe(event.getTime(), event.getRozpocznijPrzerwe()));
                                break;
                            case RozpoczecieObslugi:
                                log("Rozpoczecie obslugi: "+event.getRozpoczecieObslugi());
                                rozpoczeciaOblugi.add(new EvRozpoczecieObslugi(event.getTime(), event.getRozpoczecieObslugi()));
                                break;
                            case ZakoczenieSymulacji:
                                log("Odebrano zakonczenie symulacji");
                                this.isRunning=false;
                                break;
                            case RozpoczecieSymulacji:
                                log("Odebrano rozpoczecie symulacji");
                                break;
                        }} catch (Exception e) {}
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
    public static void main(String[] args) {
        new StatystykaFederate().runFederate();
    }

    private void publishAndSubscribe() {
        //obiekty

        //interakcje

        //obiekty
        fedamb.subskrypcje.subscribeKasa(rtiamb);
        fedamb.subskrypcje.subscribeKlient(rtiamb);
        //interakcje
        fedamb.subskrypcje.subscribeRozpoczecieSymulacji(rtiamb);
        fedamb.subskrypcje.subscribeZakoczenieSymulacji(rtiamb);
        fedamb.subskrypcje.subscribeUruchomNowaKase(rtiamb);
        fedamb.subskrypcje.subscribeRozpocznijPrzerwe(rtiamb);
        fedamb.subskrypcje.subscribeRozpoczecieObslugi(rtiamb);
        fedamb.subskrypcje.subscribeZakonczanieObslugiKlienta(rtiamb);
        fedamb.subskrypcje.subscribeZakoczeniePrzerwy(rtiamb);
    }
}