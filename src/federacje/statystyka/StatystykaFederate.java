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
import objects.Kasa;
import objects.Klient;
import statistic.Colections.*;

import statistic.Obj.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by Marcin on 22.06.2017.
 */
public class StatystykaFederate extends FederateAbstract {
    //Statystyki----------
    ListSredniCzasOczekiwania listSredniCzasOczekiwania=new ListSredniCzasOczekiwania();
    ZadaniaUruchomieniaKasy zadaniaUruchomieniaKasy=new ZadaniaUruchomieniaKasy();
    ZakonczeniaObslugiKliena zakonczeniaObslugiKliena=new ZakonczeniaObslugiKliena();
    UpdateKasaList updateKasaList=new UpdateKasaList();
    UpdateKlientList updateKlientList=new UpdateKlientList();
    ZakoczeniaPrzerwy zakoczeniaPrzerwy=new ZakoczeniaPrzerwy();
    WejsciaDoKolejki wejsciaDoKolejki=new WejsciaDoKolejki();
    RozpoczeciaPrzerwy rozpoczeciaPrzerwy=new RozpoczeciaPrzerwy();
    RozpoczeciaOblugi rozpoczeciaOblugi=new RozpoczeciaOblugi();
    LinkedList<Kasa> listaKas=new LinkedList<Kasa>();
    LinkedList<Klient> listaKlientow=new LinkedList<Klient>();
    //Obliczenia----------
    int iloscGotowki=0;
    int liczbaKlientowWSklepie=0;
    int liczbaPriorytetowych=0;
    int czasObslugi=0;
    int liczbaObsluzonych=0;
    int czasPrzerw=0;
    int liczbaPrzerw=0;
    int czasZakupow=0;
    int liczbaKlientowWKolejce=0;
    //--------------------
    public static final String federateName = "StatystykaFederate";
    public AmbasadorAbstract fedamb;

    public void liczenieKas(Kasa kasa){
        if(listaKas.size()==0)
            listaKas.add(kasa);
        else{
            boolean temp = true;
            for (Kasa ks: listaKas) {
                if(ks.NumerKasy==kasa.NumerKasy) temp = false;
            }
            if(temp) listaKas.add(kasa);
        }
    }

    public void liczenieKlientowWSklepie(Klient klient){
        if(klient.NumerKolejki == -1){
            if(listaKlientow.size()==0){
                liczbaKlientowWSklepie++;
                iloscGotowki+=klient.Gotowka;
                listaKlientow.add(klient);
                if(klient.uprzywilejowany==true){
                    liczbaPriorytetowych++;
                }
            }
            else{
                boolean temp = true;
                for (Klient kl: listaKlientow) {
                    if(kl.IDKlienta==klient.IDKlienta) temp = false;
                }
                if(temp){
                    listaKlientow.add(klient);
                    liczbaKlientowWSklepie++;
                    iloscGotowki+=klient.Gotowka;
                    if(klient.uprzywilejowany==true){
                        liczbaPriorytetowych++;
                    }
                }
            }
        }
    }

    public void wypisanieStatystyk(){
        System.out.println("*********************************************************");
        System.out.println("************************STATYSTYKA***********************");
        System.out.println("*********************************************************");
        System.out.println("Liczba kas: " + listaKas.size());
        System.out.println("Laczna liczba klientow: " + liczbaKlientowWSklepie);
        System.out.println("Laczna liczba klientow uprzywilejowanych: " + liczbaPriorytetowych);
        if(liczbaKlientowWSklepie!=0)
            System.out.println("Srednia ilosc gotowki: " + Math.round(iloscGotowki/liczbaKlientowWSklepie));
        if(liczbaKlientowWKolejce!=0)
            System.out.println("Sredni czas zakupow: " + Math.round(czasZakupow/liczbaKlientowWKolejce));
        System.out.printf("Sredni czas oczekiwania: %.2f\n",listSredniCzasOczekiwania.ObliczSredni());
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
                                Kasa kasa = event.getKasa();
                                log("Dodano kase: " + kasa);
                                liczenieKas(kasa);
                                updateKasaList.add(new UpdateKasa(event.getTime(),kasa));
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
                                listSredniCzasOczekiwania.add(new SredniCzasOczekiwania(wej.IDKlienta,fedamb.federateTime));
                                break;
                            case RozpocznijPrzerwe:
                                log("Rozpocznij przerwe: "+event.getRozpocznijPrzerwe());
                                rozpoczeciaPrzerwy.add(new EvRozpocznijPrzerwe(event.getTime(), event.getRozpocznijPrzerwe()));
                                break;
                            case RozpoczecieObslugi:
                                log("Rozpoczecie obslugi: "+event.getRozpoczecieObslugi());
                                RozpoczecieObslugi roz= event.getRozpoczecieObslugi();

                                listSredniCzasOczekiwania.getbyIDKlient(roz.IDKlienta).CzasWyjsciaZKolejki=fedamb.federateTime;
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
        ogloszeniePunktuSynchronizacjiWyjscia(fedamb);
        osiagnieciePunktuSynchronizacjiWyjscia(fedamb);
        try
        {
            if(!this.isRunning)
                wypisanieStatystyk();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
      //  PotwierdzenieWyjscia();
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
        fedamb.subskrypcje.subscribeWejscieDoKolejki(rtiamb);
    }
}