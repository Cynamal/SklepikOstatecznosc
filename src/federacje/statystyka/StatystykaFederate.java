package federacje.statystyka;

import Interactions.RozpoczecieObslugi;
import Interactions.WejscieDoKolejki;
import Interactions.ZakoczeniePrzerwy;
import Interactions.ZakonczanieObslugiKlienta;
import common.AmbasadorAbstract;
import common.ExternalEventAbstract;
import common.FederateAbstract;
import federacje.kasa.KasaFederate;
import hla.rti.RTIexception;
import objects.Klient;
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
    //Obliczenia----------
    int iloscGotowki=0;
    int liczbaKlientowWSklepie=0;
    int czasObslugi=0;
    int liczbaObsluzonych=0;
    int czasPrzerw=0;
    int liczbaPrzerw=0;
    int czasZakupow=0;
    int liczbaKlientowWKolejce=0;
    int liczbaKas=0;
    //--------------------
    public static final String federateName = "StatystykaFederate";
    public AmbasadorAbstract fedamb;

    public void liczenieKlientowWSklepie(Klient klient){
        if(klient.NumerKolejki == -1){
            liczbaKlientowWSklepie++;
            iloscGotowki+=klient.Gotowka;
        }
    }

    public void wypisanieStatystyk(){
        System.out.println("*********************************************************");
        System.out.println("************************STATYSTYKA***********************");
        System.out.println("*********************************************************");
        System.out.println("Liczba otwartych kas: " + liczbaKas);
        System.out.println("Laczna liczba klientow w sklepie: " + liczbaKlientowWSklepie);
        if(liczbaKlientowWSklepie!=0)
            System.out.println("Srednia ilosc gotowki: " + Math.round(iloscGotowki/liczbaKlientowWSklepie));
        if(liczbaKlientowWKolejce!=0)
            System.out.println("Sredni czas zakupow: " + Math.round(czasZakupow/liczbaKlientowWKolejce));
        System.out.println("Sredni czas oczekiwania: " );
        if(liczbaObsluzonych!=0)
            System.out.println("Sredni czas obslugi: " + Math.round(czasObslugi/liczbaObsluzonych));
        if(liczbaPrzerw!=0)
        System.out.println("Sredni czas przerwy: " + Math.round(czasPrzerw/liczbaPrzerw));
        System.out.println("*********************************************************");
    }

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
                                Klient klient = event.getKlient();
                                log("Dodano klienta: " + klient);
                                updateKlientList.add(new UpdateKlient(event.getTime(), klient));
                                liczenieKlientowWSklepie(klient);
                                break;
                            case Kasa:
                                log("Dodano kase: " + event.getKasa());
                                updateKasaList.add(new UpdateKasa(event.getTime(),event.getKasa()));
                                break;
                            case ZakonczanieObslugiKlienta:
                                ZakonczanieObslugiKlienta zak = event.getZakonczanieObslugiKlienta();
                                log("Zakonczenie obslugi klienta: " + zak);
                                zakonczeniaObslugiKliena.add(new EvZakonczenieObslugiKlienta(event.getTime(),zak));
                                czasObslugi+=zak.CzasObslugi;
                                liczbaObsluzonych++;
                                break;
                            case UruchomNowaKase:
                                log("Odebrano zadanie uruchomienia kasy");
                                zadaniaUruchomieniaKasy.add(new ZadanieUruchomieniaKasy(event.getTime()));
                                break;
                            case ZakoczeniePrzerwy:
                                ZakoczeniePrzerwy zap = event.getZakoczeniePrzerwy();
                                log("Zakoczenie przerwy: "+zap);
                                zakoczeniaPrzerwy.add(new EvZakoczeniePrzerwy(event.getTime(), zap));
                                liczbaPrzerw++;
                                czasPrzerw+=zap.CzasPrzerwy;
                                break;
                            case WejscieDoKolejki:
                                WejscieDoKolejki wej = event.getWejscieDoKolejki();
                                log("Wejscie do kolejki klienta: " + wej);
                                wejsciaDoKolejki.add(new EvWejscieDoKolejki(event.getTime(), wej));
                                liczbaKlientowWKolejce++;
                                czasZakupow+=wej.CzasZakupow;
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
                if(this.isRunning)
                advanceTime(1.0,fedamb);
            } catch (RTIexception rtIexception) {
                rtIexception.printStackTrace();
            }


        }
        System.out.println("Konczenie symulacji");
        try
        {
            if(!this.isRunning)
                wypisanieStatystyk();
        }catch (Exception e)
        {
            e.printStackTrace();
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