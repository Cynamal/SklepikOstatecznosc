package federacje.kasa;

import Interactions.*;

import Interactions.ZakoczeniePrzerwy;

import common.AmbasadorAbstract;
import common.ExternalEventAbstract;
import common.FederateAbstract;
import hla.rti.LogicalTime;
import hla.rti.RTIexception;
import hla.rti.SuppliedAttributes;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.RtiFactoryFactory;
import objects.Kasa;
import objects.Klient;
import objects.ListaKlientow;
import statistic.Obj.EvRozpoczecieObslugi;

import java.util.Collections;
import java.util.Random;

/**
 * Created by Marcin on 22.06.2017.
 */
public class KasaFederate extends FederateAbstract {
    public int PoczatkowaLiczbaKas=2;
    public static final String federateName = "KasaFederate";
    public AmbasadorAbstract fedamb;
    public PrzerwaKasy przerwaKasy;
    public boolean czasNaPrzerwe = false;
    public void runFederate(){
        fedamb = new AmbasadorAbstract();
        CommonrunFederate(federateName,fedamb);
        publishAndSubscribe();
        czekajNAGUI(fedamb);
        for(int i=1;i<=PoczatkowaLiczbaKas;i++)
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
                                for (Klient kl:kliencjiWSklepie
                                        ) {
                                    System.out.print("kl: "+kl);
                                }
                                System.out.println();
                                break;
                            case WejscieDoKolejki:
                                WejscieDoKolejki kolejk= event.getWejscieDoKolejki();
                                Kasa prawislowa= Kasa.FindbyID(kasy,kolejk.NumerKasy);
                                prawislowa.Dlugosc++;
                                if(prawislowa.Dlugosc==prawislowa.kolejkaDOKASI.WielkoscMax)
                                    prawislowa.CzyPelna=true;
                                sendKasaToRTI(prawislowa.hendKasa,prawislowa);
                                Klient temp = kliencjiWSklepie.get(kliencjiWSklepie.getIndexByID(kolejk.IDKlienta));
                                prawislowa.kolejkaDOKASI.addKlientKasa(temp);
                                System.out.println("Klient ID "+temp.IDKlienta+" numer kolejki "+temp.NumerKolejki+" numer w kolejce "+temp.NumerWKolejce);
                                log("Klient:" +kolejk.IDKlienta+" wszedl do kasy "+kolejk.NumerKasy);
                                break;
                            case ZakoczenieSymulacji:
                                this.isRunning=false;
                                break;
                            case RozpocznijPrzerwe:
                                RozpocznijPrzerwe przewe=event.getRozpocznijPrzerwe();
                                Kasa kasaNAPrzerwe= Kasa.FindbyID(kasy,przewe.NumerKasy);
                                kasaNAPrzerwe.CzyOtwarta=false;
                                Random rand2 = new Random();
                                int czasprzer = 50 + rand2.nextInt(65);
                                //przerwaKasy=new PrzerwaKasy(kasaNAPrzerwe,20 + (int)(Math.random() * 40),fedamb.federateTime);
                                przerwaKasy=new PrzerwaKasy(kasaNAPrzerwe,czasprzer,fedamb.federateTime);
                                czasNaPrzerwe=true;
                                sendKasaToRTI(kasaNAPrzerwe.hendKasa,kasaNAPrzerwe);
                                break;
                        }
                    } catch (Exception e) {

                    }
                }
                fedamb.externalEvents.clear();
            }
            if(czasNaPrzerwe)
            {
                if(przerwaKasy.kasa.Dlugosc==0&&przerwaKasy.KoniecPrzerwy==-1)
                    przerwaKasy.zakonczenieObslugiOstatniego(fedamb.federateTime);
                if(przerwaKasy.czyKoniecPrzerwy(fedamb.federateTime))
                {
                    try {
                        przerwaKasy.kasa.CzyOtwarta=true;
                        sendKasaToRTI(przerwaKasy.kasa.hendKasa,przerwaKasy.kasa);
                        sendZakonczeniePrzerwy(przerwaKasy);
                        przerwaKasy=null;
                        czasNaPrzerwe=false;
                    } catch (RTIexception rtIexception) {
                        rtIexception.printStackTrace();
                    }

                }
            }
            try {
                if(this.isRunning)
                RozpocznijObslugelubZakoncz();
            } catch (RTIexception rtIexception) {
                rtIexception.printStackTrace();
            }

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
      //  PotwierdzenieWyjscia();
    }
    private void sendZakonczeniePrzerwy(PrzerwaKasy przerwaKasy)throws RTIexception
    {
        //ZakoczeniePrzerwy zakoczeniePrzerwy= new ZakoczeniePrzerwy((int) Math.round((przerwaKasy.KoniecPrzerwy-przerwaKasy.poczatekPrzerwy)),przerwaKasy.kasa.NumerKasy);
        ZakoczeniePrzerwy zakoczeniePrzerwy= new ZakoczeniePrzerwy((int) Math.round(przerwaKasy.czasPrzerwy),przerwaKasy.kasa.NumerKasy);
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();
        byte[] czasPrzerwy=zakoczeniePrzerwy.getCzasPrzerwyByte();
        byte[] NumerKasy=zakoczeniePrzerwy.getNumerKasyByte();

        parameters.add(fedamb.publikacje.zakoczeniePrzerwyHandler.CzasPrzerwyHandler,czasPrzerwy);
        parameters.add(fedamb.publikacje.zakoczeniePrzerwyHandler.NumerKasyHandler,NumerKasy);

        LogicalTime time = convertTime( fedamb.federateTime+1.0 );
        rtiamb.sendInteraction(fedamb.publikacje.zakoczeniePrzerwyHandler.getZakoczeniePrzerwyHandler(), parameters, "zakonczenie obslugi".getBytes(), time );

    }
    private void rozpocznijObslugeKlienta(RozpoczecieObslugi rozp,double timeStep)throws RTIexception
    {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();
        byte[] NumerKasy=rozp.getNumerKasyByte();
        byte[] CzasObslugi=rozp.getCzasOczekiwaniaByte();
        byte[] ID=rozp.getIDKlientaByte();

        parameters.add(fedamb.publikacje.rozpoczecieObslugiHandler.NumerKasyHandler,NumerKasy);
        parameters.add(fedamb.publikacje.rozpoczecieObslugiHandler.CzasOczekiwaniaHandler,CzasObslugi);
        parameters.add(fedamb.publikacje.rozpoczecieObslugiHandler.IDKlientaHandler,ID);

        LogicalTime time = convertTime( timeStep );
        rtiamb.sendInteraction(fedamb.publikacje.rozpoczecieObslugiHandler.getRozpoczecieObslugiHandler(), parameters, "rozpocznijObsluge".getBytes(), time );
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

    private void RozpocznijObslugelubZakoncz() throws RTIexception{

        for (Kasa kas: kasy
             ) {
            if(!kas.czyObsluguje) {
                if (!kas.kolejkaDOKASI.CzyPusta()) {
                    kas.czyObsluguje = true;
                    Random random = new Random();
                    int czasObslugi = 100 + random.nextInt(25);
                    Klient pierwszy = kas.kolejkaDOKASI.getAndDeleteFirst();
                    kas.Dlugosc--;
                    kas.CzyPelna=false;
                    kas.czasRozpoczeciaObslugi = fedamb.federateTime;
                    kas.czasZakonczeniaObslugi = fedamb.federateTime + czasObslugi;
                    //RozpoczecieObslugi rozp = new RozpoczecieObslugi(czasObslugi,kas.NumerKasy,pierwszy.IDKlienta);
                    //SuppliedParameters parameters = RtiFactoryFactory.getRtiFactory().createSuppliedParameters();
                    //byte[] NumerKasy=rozp.getNumerKasyByte();
                    //byte[] CzasOczekiwana=rozp.getCzasOczekiwaniaByte();
                    //byte[] ID=rozp.getIDKlientaByte();
                    //parameters.add(fedamb.publikacje.zakonczenieObslugiKlientaHandler.CzasObslugiHandler,NumerKasy);
                    //parameters.add(fedamb.publikacje.zakonczenieObslugiKlientaHandler.CzasObslugiHandler,CzasOczekiwana);
                    //parameters.add(fedamb.publikacje.zakonczenieObslugiKlientaHandler.IDKlientaHandler,ID);
                    //LogicalTime time = convertTime(fedamb.federateTime + fedamb.federateLookahead);
                    kas.idKlientaOblugiwanego=pierwszy.IDKlienta;
                    rozpocznijObslugeKlienta(new RozpoczecieObslugi(czasObslugi,kas.NumerKasy,pierwszy.IDKlienta),(fedamb.federateTime + fedamb.federateLookahead));
                    sendKasaToRTI(kas.hendKasa,kas);
                    //rtiamb.sendInteraction(fedamb.publikacje.rozpoczecieObslugiHandler.getRozpoczecieObslugiHandler(), parameters, "rozpoczecieObslugi".getBytes(), time );
                    //usuniecie klienta kolejki
                    log("Rozpoczeto obsluge:"+pierwszy.IDKlienta+" kasa:"+kas.NumerKasy);
                }
            }else{
                if(kas.czasZakonczeniaObslugi==fedamb.federateTime){
                    int czas = (int) Math.round(kas.czasZakonczeniaObslugi-kas.czasRozpoczeciaObslugi);
                    log("Zakonczono obsluge:"+kas.idKlientaOblugiwanego+" kasa:"+kas.NumerKasy+" czas obslugi:"+czas);
                    kas.czyObsluguje=false;
                    zakonczObslugeKlienta(new ZakonczanieObslugiKlienta(kas.idKlientaOblugiwanego, czas),(fedamb.federateTime + fedamb.federateLookahead));
                }
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
    private int IteratorKasy = 0;
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
        fedamb.publikacje.publishRozpoczecieObslugi(rtiamb);
        //obiekty
        fedamb.subskrypcje.subscribeKlient(rtiamb);
        //interakcje
        fedamb.subskrypcje.subscribeUruchomNowaKase(rtiamb);
        fedamb.subskrypcje.subscribeRozpocznijPrzerwe(rtiamb);

        fedamb.subskrypcje.subscribeWejscieDoKolejki(rtiamb);
        fedamb.subskrypcje.subscribeRozpoczecieSymulacji(rtiamb);
        fedamb.subskrypcje.subscribeZakoczenieSymulacji(rtiamb);
    }
}
