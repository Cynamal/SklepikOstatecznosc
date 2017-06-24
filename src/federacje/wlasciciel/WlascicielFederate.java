package federacje.wlasciciel;

import Interactions.RozpocznijPrzerwe;
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
import java.util.Random;

/**
 * Created by Marcin on 22.06.2017.
 */
public class WlascicielFederate extends FederateAbstract {
    public static final String federateName = "WlascicielFederate";
    public AmbasadorAbstract fedamb;
    public double CzasWyslaniaNaPrzerwe=20;
    public boolean CzyWyslanoNaPrzerwe=false;
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
                            case ZakoczeniePrzerwy:
                                CzyWyslanoNaPrzerwe=false;
                                Random rand2 = new Random();
                                int nowyczas = 100 + rand2.nextInt(65);
                                CzasWyslaniaNaPrzerwe=fedamb.federateTime+nowyczas;
                                //CzasWyslaniaNaPrzerwe=fedamb.federateTime+30+(int)(Math.random() * 50);
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
            }

            if(this.isRunning&&!CzyWyslanoNaPrzerwe)CzyWyslanoNaPrzerwe=WyslijZadanieZamknieciaKasy();
            try {
                if(this.isRunning)
                advanceTime(1.0,fedamb);
            } catch (RTIexception rtIexception) {
                rtIexception.printStackTrace();
            }
        }
        System.out.print("Zamykanie");
        ogloszeniePunktuSynchronizacjiWyjscia(fedamb);
        osiagnieciePunktuSynchronizacjiWyjscia(fedamb);
     //   PotwierdzenieWyjscia();
    }
    public static void main(String[] args) {
        new WlascicielFederate().runFederate();
    }
    private  boolean WyslijZadanieZamknieciaKasy()
    {
        if(fedamb.federateTime>5.0)
        {
          //  if(CzasWyslaniaNaPrzerwe<fedamb.federateTime)
           //     CzasWyslaniaNaPrzerwe=fedamb.federateTime;
           if(CzasWyslaniaNaPrzerwe==fedamb.federateTime)
           {
               //if(Kasa.SprawdzCZyWszytkiePelne(kasy))
             //  {
              //     CzasWyslaniaNaPrzerwe= 1 + (int)(Math.random() * 13)+fedamb.federateTime;
             //  }
             //  else

                   try {
                       Kasa tmp=  Kasa.WesLosowaAktywnaKase(kasy);
                       RozpocznijPrzerwe przerwa= new RozpocznijPrzerwe(tmp.NumerKasy);
                       LogicalTime newTime  = convertTime(fedamb.federateTime + fedamb.federateLookahead);
                       LogicalTime time = newTime ;
                       SuppliedParameters attributes =
                               RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

                       byte[] NumerKasy = przerwa.getNumerKasyByte();

                       attributes.add(fedamb.publikacje.rozpocznijPrzerweHandler.NumerKasyHandler, NumerKasy);
                       rtiamb.sendInteraction(
                               fedamb.publikacje.rozpocznijPrzerweHandler.getRozpocznijPrzerweHandler(),
                               attributes,
                               "tag".getBytes(),
                               time
                       );
                       System.out.println(""+przerwa);
                       return true;
                   } catch (Exception e) {
                       e.printStackTrace();
                        return false;
                   }

           }
        }
        return false;
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
                    System.out.print("Wyslano zadanieDodania Nowej Kasy");
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