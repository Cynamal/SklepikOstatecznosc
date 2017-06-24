package federacje.wlasciciel;

import common.AmbasadorAbstract;
import common.ExternalEventAbstract;
import common.FederateAbstract;
import federacje.kasa.KasaFederate;
import hla.rti.LogicalTime;
import hla.rti.RTIexception;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.RtiFactoryFactory;
import objects.Kasa;
import objects.Klient;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by Marcin on 22.06.2017.
 */
public class WlascicielFederate extends FederateAbstract {
    public static final String federateName = "WlascicielFederate";
    public AmbasadorAbstract fedamb;

    public void runFederate(){
        boolean wyslanoZadanieDodaniaNowej=false;
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
                            case Kasa:
                                if(Kasa.addorChangeIfExist(event.getKasa(),kasy))
                                {
                                    log("Odebrano nowa kase:"+event.getKasa());
                                    wyslanoZadanieDodaniaNowej=false;
                                }

                                else
                                    log("Zaktualizowano kase:"+event.getKasa());
                                break;
                            case ZakoczenieSymulacji:
                                this.isRunning=false;
                                break;

                        }
                    } catch (Exception e) {

                    }
                }
                fedamb.externalEvents.clear();
            }
            if(this.isRunning)
            if(!wyslanoZadanieDodaniaNowej)
            {
                wyslanoZadanieDodaniaNowej=OtworzKaseJezeliKonieczne();

                System.out.print("Wyslano zadanieDodania Nowej Kasy");
            }


            try {
                if(this.isRunning)
                advanceTime(1.0,fedamb);
            } catch (RTIexception rtIexception) {
                rtIexception.printStackTrace();
            }
        }
        System.out.print("Zamykanie");
    }
    public static void main(String[] args) {
        new WlascicielFederate().runFederate();
    }
    private boolean OtworzKaseJezeliKonieczne()
    {
        if(fedamb.federateTime>5.0)
        {
          //  LinkedList<Kasa> kasyAktywne= Kasa.getActiveOnly(kasy);
            if(Kasa.SprawdzCZyWszytkiePelne(kasy))
            {
                System.out.println("Nie znaleziono aktywnych kas. Wysylanie zadania dodania nowej");
                try {
                    WyslijZadanieUruchomieniaKasy(2.0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;

    }
    private void WyslijZadanieUruchomieniaKasy(double timeStep) throws Exception
    {
        System.out.println("bycbyc");
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

        //interakcje
        fedamb.subskrypcje.subscribeRozpoczecieSymulacji(rtiamb);
        fedamb.subskrypcje.subscribeZakoczenieSymulacji(rtiamb);


        fedamb.subskrypcje.subscribeZakoczeniePrzerwy(rtiamb);
    }
}