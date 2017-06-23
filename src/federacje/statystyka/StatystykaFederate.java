package federacje.statystyka;

import common.AmbasadorAbstract;
import common.ExternalEventAbstract;
import common.FederateAbstract;
import federacje.kasa.KasaFederate;
import hla.rti.RTIexception;
import statistic.Colections.*;

import statistic.Obj.EvZakonczenieObslugiKlienta;
import statistic.Obj.UpdateKasa;
import statistic.Obj.ZadanieUruchomieniaKasy;

import java.util.Collections;

/**
 * Created by Marcin on 22.06.2017.
 */
public class StatystykaFederate extends FederateAbstract {
    //Statystyki----------
    ZadaniaUruchomieniaKasy zadaniaUruchomieniaKasy=new ZadaniaUruchomieniaKasy();
    ZakonczeniaObslugiKliena zakonczeniaObslugiKliena=new ZakonczeniaObslugiKliena();
    UpdateKasaList updateKasaList=new UpdateKasaList();
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
                        }
                    } catch (Exception e) {

                    }
                }

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