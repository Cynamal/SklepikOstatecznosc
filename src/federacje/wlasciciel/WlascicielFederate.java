package federacje.wlasciciel;

import common.AmbasadorAbstract;
import common.FederateAbstract;
import federacje.kasa.KasaFederate;
import hla.rti.LogicalTime;
import hla.rti.RTIexception;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.RtiFactoryFactory;
import objects.Kasa;
import objects.Klient;

import java.util.LinkedList;

/**
 * Created by Marcin on 22.06.2017.
 */
public class WlascicielFederate extends FederateAbstract {
    public static final String federateName = "WlascicielFederate";
    public AmbasadorAbstract fedamb;

    public void runFederate(){
        fedamb = new AmbasadorAbstract();
        CommonrunFederate(federateName,fedamb);
        publishAndSubscribe();
        czekajNAGUI(fedamb);
        while(this.isRunning)
        {
            OtworzKaseJezeliKonieczne();

            try {
                advanceTime(1.0,fedamb);
            } catch (RTIexception rtIexception) {
                rtIexception.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        new WlascicielFederate().runFederate();
    }
    private void OtworzKaseJezeliKonieczne()
    {
       LinkedList<Kasa> kasyAktywne= Kasa.getActiveOnly(kasy);
       if(kasyAktywne.size()==0)
       {
           System.out.println("Nie znaleziono aktywnych kas. Wysylanie zadania dodania nowej");
           try {
               WyslijZadanieUruchomieniaKasy(1.0);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    }
    private void WyslijZadanieUruchomieniaKasy(double timeStep) throws Exception
    {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();


        LogicalTime newTime  = convertTime(fedamb.federateTime + timeStep);
        LogicalTime time = newTime ;
        rtiamb.sendInteraction( fedamb.publikacje.uruchomNowaKaseHandler.getUruchomNowaKaseHandler(), parameters, "tag".getBytes(), time );
        System.out.println("Wyslano zadanie");
    }
    private void publishAndSubscribe() {
        //obiekty

        //interakcje
        fedamb.publikacje.publishUruchomNowaKase(rtiamb);
        fedamb.publikacje.publishRozpocznijPrzerwe(rtiamb);
        //obiekty
        fedamb.subskrypcje.subscribeKasa(rtiamb);
        fedamb.subskrypcje.subscribeKlient(rtiamb);
        //interakcje
        fedamb.subskrypcje.subscribeRozpoczecieSymulacji(rtiamb);
        fedamb.subskrypcje.subscribeZakoczenieSymulacji(rtiamb);


        fedamb.subskrypcje.subscribeZakoczeniePrzerwy(rtiamb);
    }
}