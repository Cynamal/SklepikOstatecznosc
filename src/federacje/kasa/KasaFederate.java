package federacje.kasa;

import Interactions.ZakonczanieObslugiKlienta;
import common.AmbasadorAbstract;
import common.FederateAbstract;
import hla.rti.LogicalTime;
import hla.rti.RTIexception;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

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
        try {
            zakonczObslugeKlienta(new ZakonczanieObslugiKlienta(2,10),1.0);
        } catch (RTIexception rtIexception) {
            rtIexception.printStackTrace();
        }
        try {
            advanceTime(1.0,fedamb);
        } catch (RTIexception rtIexception) {
            rtIexception.printStackTrace();
        }
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
