package federacje.kasa;

import Interactions.WejscieDoKolejki;
import Interactions.ZakonczanieObslugiKlienta;
import common.AmbasadorAbstract;
import common.ExternalEventAbstract;
import common.FederateAbstract;
import hla.rti.LogicalTime;
import hla.rti.RTIexception;
import hla.rti.SuppliedAttributes;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.RtiFactoryFactory;
import objects.Kasa;
import objects.ListaKlientow;

import java.util.Collections;

/**
 * Created by Marcin on 22.06.2017.
 */
public class KasaFederate extends FederateAbstract {
    public int PoczatkowaLiczbaKas=0;
    public static final String federateName = "KasaFederate";
    public AmbasadorAbstract fedamb;

    public void runFederate(){
        fedamb = new AmbasadorAbstract();
        CommonrunFederate(federateName,fedamb);
        publishAndSubscribe();
        czekajNAGUI(fedamb);
        for(int i=0;i<PoczatkowaLiczbaKas;i++)
        {
            try {
            UruchomNowaKase();
            } catch (Exception e) {
                log("cos nie teges z kasami:"+e);
            }
        }
        while(this.isRunning)
        {
            if (fedamb.externalEvents.size() > 0) {
                Collections.sort(fedamb.externalEvents, new ExternalEventAbstract.ExternalEventComparator());
                for (ExternalEventAbstract event : fedamb.externalEvents) {
                    try {


                        // System.out.println("w for");
                        switch (event.getEventType()) {

                            case UruchomNowaKase:
                                log("Odebrano zadanie uruchomienia kasy");
                                UruchomNowaKase();
                                break;
                            case Klient:

                                if(kliencjiWSklepie.addorChangeIfExist(event.getKlient()))
                                {
                                    log("Odebrano nowego klienta: "+event.getKlient());
                                }
                                else
                                {
                                    log("Odebrano aktualizacje klienta: "+event.getKlient());
                                }
                                break;
                            case WejscieDoKolejki:
                                WejscieDoKolejki kolejk= event.getWejscieDoKolejki();
                                Kasa prawislowa= Kasa.FindbyID(kasy,kolejk.NumerKasy);
                                prawislowa.Dlugosc++;
                                if(prawislowa.Dlugosc==prawislowa.kolejka.WielkoscMax)
                                    prawislowa.CzyOtwarta=false;
                                sendKasaToRTI(prawislowa.hendKasa,prawislowa);
                                log("Klient:" +kolejk.IDKlienta+" wszedl do kasy "+kolejk.NumerKasy);
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

    /** Wysyla parametry kasy do rti Moze byc uzyta do inicjalizacji lub aktualizacji
     * @param kasaHandle hendler kasy stworzony przy registerKasa
     * @param kasa Obiekt kasy
     * @throws RTIexception
     */
    private void sendKasaToRTI(int kasaHandle, Kasa kasa) throws RTIexception {

        SuppliedAttributes attributes= kasa.getRTIAtributes(fedamb);
        LogicalTime time = convertTime(fedamb.federateTime + fedamb.federateLookahead);
        rtiamb.updateAttributeValues(kasaHandle,attributes, generateTag(), time);
        System.out.println("wyslano " + kasa +"czas:"+time );
    }
    private int registerKasa() throws RTIexception {
        return rtiamb.registerObjectInstance(fedamb.publikacje.kasaHandler.getKasaHandler(), "Kasa" + IteratorKasy);

    }
    private int IteratorKasy = 1;
    private void UruchomNowaKase() throws RTIexception {
        int hendKasa=registerKasa();
        Kasa tmp = new Kasa(IteratorKasy++, 0, false, true,hendKasa);
        kasy.add(tmp);
        sendKasaToRTI(hendKasa,tmp);

    }

    public static void main(String[] args) {
        new KasaFederate().runFederate();
    }

    private void publishAndSubscribe() {
        //obuekty
        fedamb.publikacje.publishKasa(rtiamb);
        //interakcje
        fedamb.publikacje.publishZakonczenieObslugiKlienta(rtiamb);
        fedamb.publikacje.publishZakoczeniePrzerwy(rtiamb);
        //obiekty
        fedamb.subskrypcje.subscribeKlient(rtiamb);
        //interakcje
        fedamb.subskrypcje.subscribeUruchomNowaKase(rtiamb);
        fedamb.subskrypcje.subscribeRozpocznijPrzerwe(rtiamb);
        fedamb.subskrypcje.subscribeRozpoczecieObslugi(rtiamb);
        fedamb.subskrypcje.subscribeWejscieDoKolejki(rtiamb);
        fedamb.subskrypcje.subscribeRozpoczecieSymulacji(rtiamb);
        fedamb.subskrypcje.subscribeZakoczenieSymulacji(rtiamb);
    }
    private void zakonczObslugeKlienta(ZakonczanieObslugiKlienta zakonczanieObslugiKlienta,double timeStep)throws RTIexception
    {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();
        byte[] ID=zakonczanieObslugiKlienta.getIDKlientaByte();
        byte[] CzasObslugi=zakonczanieObslugiKlienta.getCzasObslugiByte();

                parameters.add(fedamb.publikacje.zakonczenieObslugiKlientaHandler.IDKlientaHandler,ID);
                parameters.add(fedamb.publikacje.zakonczenieObslugiKlientaHandler.CzasObslugiHandler,CzasObslugi);
        LogicalTime time = convertTime( timeStep );
        rtiamb.sendInteraction(fedamb.publikacje.zakonczenieObslugiKlientaHandler.getZakonczenieObslugiKlientaHandler(), parameters, "zakonczObslugeKlienta".getBytes(), time );
    }
}
