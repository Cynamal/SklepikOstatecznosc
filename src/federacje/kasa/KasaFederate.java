package federacje.kasa;

import Interactions.ZakonczanieObslugiKlienta;
import common.AmbasadorAbstract;
import common.ExternalEventAbstract;
import common.FederateAbstract;
import hla.rti.LogicalTime;
import hla.rti.RTIexception;
import hla.rti.SuppliedAttributes;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;
import objects.Kasa;

import java.util.Collections;

/**
 * Created by Marcin on 22.06.2017.
 */
public class KasaFederate extends FederateAbstract {
    public static final String federateName = "KasaFederate";
    public AmbasadorAbstract fedamb;

    public void runFederate(){
        fedamb = new AmbasadorAbstract();
        CommonrunFederate(federateName,fedamb);
        publishAndSubscribe();
        czekajNAGUI(fedamb);
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
    private void initKasa(int kasaHandle, Kasa kasa) throws RTIexception {
        SuppliedAttributes attributes =
                RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();

        byte[] NumerKasy = EncodingHelpers.encodeInt(kasa.NumerKasy);
        byte[] Dlugosc = EncodingHelpers.encodeInt(kasa.Dlugosc);
        byte[] CzyPelna = EncodingHelpers.encodeBoolean(kasa.CzyPelna);
        byte[] CzyOtwarta = EncodingHelpers.encodeBoolean(kasa.CzyOtwarta);

        attributes.add(fedamb.publikacje.kasaHandler.NumerKasyHandler, NumerKasy);
        attributes.add(fedamb.publikacje.kasaHandler.DlugoscHandler, Dlugosc);
        attributes.add(fedamb.publikacje.kasaHandler.CzyPelnaHandler, CzyPelna);
        attributes.add(fedamb.publikacje.kasaHandler.CzyOtwartaHandler, CzyOtwarta);

        LogicalTime time = convertTime(fedamb.federateTime + fedamb.federateLookahead);
        rtiamb.updateAttributeValues(kasaHandle, attributes, generateTag(), time);
        System.out.println("wyslano" + fedamb.publikacje.kasaHandler.getKasaHandler() + "," + attributes + ",");
    }
    private int registerKasa() throws RTIexception {
        return rtiamb.registerObjectInstance(fedamb.publikacje.kasaHandler.getKasaHandler(), "Kasa" + IteratorKasy);

    }
    private int IteratorKasy = 1;
    private void UruchomNowaKase() throws RTIexception {
        int hendKasa=registerKasa();
        Kasa tmp = new Kasa(IteratorKasy++, 0, false, true);
        kasy.add(tmp);
        initKasa(hendKasa,tmp);

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
