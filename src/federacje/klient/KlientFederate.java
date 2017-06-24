package federacje.klient;

import Interactions.RozpoczecieObslugi;
import Interactions.WejscieDoKolejki;
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
import objects.Klient;
import objects.ListaKlientow;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by Marcin on 22.06.2017.
 */
public class KlientFederate extends FederateAbstract {
    public static final String federateName = "KlientFederate";
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

                            case Kasa:
                                if(Kasa.addorChangeIfExist(event.getKasa(),kasy))
                                    log("Odebrano nowa kase:"+event.getKasa());
                                else
                                    log("Zaktualizowano kase:"+event.getKasa());

                                break;
                            case RozpoczecieObslugi:
                                RozpoczecieObslugi rozpoczecieObslugi= event.getRozpoczecieObslugi();
                                UpdateQue(rozpoczecieObslugi.NumerKasy);
                                break;
                        }
                    } catch (Exception e) {

                    }
                }
                fedamb.externalEvents.clear();
            }
            dodajKlienta();
            wchodzenieDokolejki();
            try {
                advanceTime(1.0,fedamb);
            } catch (RTIexception rtIexception) {
                rtIexception.printStackTrace();
            }
        }
    }

    private void UpdateQue(int numerKasy) {
        try {
            Kasa kasa= Kasa.FindbyID(kasy,numerKasy);
           int idKlientDoUsuniecia= kasa.kolejka.mygetFirst();
           Klient klientDoUsuniecia=kasa.kolejka.get(idKlientDoUsuniecia);
            deleteObject(klientDoUsuniecia.hendler);
           kasa.kolejka.remove(klientDoUsuniecia);
            for (Klient kl: kasa.kolejka
                 ) {
                kl.NumerWKolejce--;
                UpdateKlienttoRTI(kl.hendler,kl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void wchodzenieDoKasi() {
        for (Klient kl: kliencjiWSklepie)
        {
            if(kl.czasZakonczeniaZakupow==fedamb.federateTime)
            {
                LinkedList<Kasa>aktywne= Kasa.getActiveOnlykasi(kasy);
                int indexKasi= Kasa.FindBestQiueKASI(kl.uprzywilejowany,aktywne);
                if(indexKasi==-1)
                {
                    kl.czasZakonczeniaZakupow+=3;
                }
                else
                {

                    aktywne.get(indexKasi).kolejkaDOKASI.add(kl);
                    kliencjiWSklepie.remove(kl);
                    try {
                        UpdateKlienttoRTI(kl.hendler,kl);
                        int czs= (int) Math.round( kl.czasZakonczeniaZakupow-kl.czasRozpoczeciaZakupow);
                        wyslijInterakcjeWejsciaDoKolejki(new WejscieDoKolejki(czs,aktywne.get(indexKasi).NumerKasy,kl.IDKlienta));
                    } catch (RTIexception rtIexception) {
                        rtIexception.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private void wchodzenieDokolejki() {
        ListaKlientow DoWywalenia= new ListaKlientow(Integer.MAX_VALUE);
        for (Klient kl: kliencjiWSklepie
             ) {
            if(kl.czasZakonczeniaZakupow==fedamb.federateTime)
            {
               LinkedList<Kasa>aktywne= Kasa.getActiveOnly(kasy);
                int nr= Kasa.FindBestQiue(kl.uprzywilejowany,aktywne);
                if(nr==-1)
                {
                    kl.czasZakonczeniaZakupow+=3;
                }
                else
                {
                    log("jezdem");
                    DoWywalenia.add(kl);

                    int miejsce=aktywne.get(nr).kolejka.GetplaceWithPropity(kl.uprzywilejowany);
                    kl.NumerWKolejce=miejsce;
                    kl.NumerKolejki=aktywne.get(nr).NumerKasy;
                    aktywne.get(nr).kolejka.add(kl);
                    try {
                        UpdateKlienttoRTI(kl.hendler,kl);
                       int czs= (int) Math.round( kl.czasZakonczeniaZakupow-kl.czasRozpoczeciaZakupow);
                        wyslijInterakcjeWejsciaDoKolejki(new WejscieDoKolejki(czs,aktywne.get(nr).NumerKasy,kl.IDKlienta));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }


        }
        //Wywalanie z listy
        for (Klient kl:DoWywalenia
             ) {
            kliencjiWSklepie.remove(kl);
        }
    }
    public void wyslijInterakcjeWejsciaDoKolejki(WejscieDoKolejki wej) throws Exception
    {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();
        byte[] ID=  wej.getIDKlientaByte();
        byte[] czas=wej.getCzasZakupowByte();
        byte[] NumerKasy=wej.getNumerKasyByte();

        parameters.add(fedamb.publikacje.wejscieDoKolejkiHandler.CzasZakupowHandler,czas);
        parameters.add(fedamb.publikacje.wejscieDoKolejkiHandler.NumerKasyHandler,NumerKasy);
        parameters.add(fedamb.publikacje.wejscieDoKolejkiHandler.IDKlientaHandler, ID);

        LogicalTime time = convertTime( fedamb.federateTime + fedamb.federateLookahead);
        rtiamb.sendInteraction(fedamb.publikacje.wejscieDoKolejkiHandler.getWejscieDoKolejkiHandler(), parameters, "tag".getBytes(), time );
    }
    public static void main(String[] args) {
        new KlientFederate().runFederate();
    }
    double czasDodaniaNowegoKlienta=6.0;
    int maximumTimeKlient=20;
    int maxCash=1000;
    private int KlientNextID=1;
    public void dodajKlienta()
    {
      if(czasDodaniaNowegoKlienta==fedamb.federateTime)
      {
          int randTIMe= 5 + (int)(Math.random() * maximumTimeKlient);
          czasDodaniaNowegoKlienta+=randTIMe;

          try {
              int gotowka=100 + (int)(Math.random() * maxCash);
              int klienti=  registerKlient();
              int czas=gotowka/10;
              Klient tmp=new Klient (KlientNextID++,false,-1,-1,gotowka,1+czas+fedamb.federateTime,klienti,fedamb.federateTime);
              kliencjiWSklepie.add(tmp);
              UpdateKlienttoRTI(klienti, tmp);


          } catch (Exception rtIexception) {
              rtIexception.printStackTrace();
          }
      }
    }
    private int registerKlient() throws RTIexception {
        return rtiamb.registerObjectInstance(this.fedamb.publikacje.klientHandler.getKlientHandler(), "Klient"+KlientNextID);
    }
    private void UpdateKlienttoRTI(int KlientHandle, Klient klient) throws RTIexception {
        SuppliedAttributes attributes=   klient.getRTIAtributes(fedamb);
        LogicalTime time = convertTime( fedamb.federateTime + fedamb.federateLookahead );
        rtiamb.updateAttributeValues( KlientHandle, attributes, generateTag(), time );
        System.out.println("wyslano"+fedamb.publikacje.klientHandler.getKlientHandler()+","+attributes+",");
    }
    private void publishAndSubscribe() {
        // obiekty
        fedamb.publikacje.publishKlient(rtiamb);
        //interakcje
        fedamb.publikacje.publishWejscieDoKolejki(rtiamb);

        // obiekty
        fedamb.subskrypcje.subscribeKasa(rtiamb);
        //interakcje
        fedamb.subskrypcje.subscribeZakonczanieObslugiKlienta(rtiamb);
        fedamb.subskrypcje.subscribeRozpoczecieSymulacji(rtiamb);
        fedamb.subskrypcje.subscribeZakoczenieSymulacji(rtiamb);
        fedamb.subskrypcje.subscribeRozpoczecieObslugi(rtiamb);
    }
}
