package federacje.klient;

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

    private void wchodzenieDokolejki() {
        ListaKlientow DoWywalenia= new ListaKlientow(Integer.MAX_VALUE);
        for (Klient kl: kliencjiWSklepie
             ) {
            if(kl.czasZakonczeniaZakupow==fedamb.federateTime)
            {
                int nr= Kasa.FindBestQiue(kl.uprzywilejowany,kasy);
                if(nr==-1)
                {
                    kl.czasZakonczeniaZakupow+=3;
                }
                else
                {
                    log("jezdem");
                    DoWywalenia.add(kl);

                    int miejsce=kasy.get(nr).kolejka.GetplaceWithPropity(kl.uprzywilejowany);
                    kl.NumerWKolejce=miejsce;
                    kl.NumerKolejki=nr;
                    kasy.get(nr).kolejka.add(kl);
                    try {
                        UpdateKlienttoRTI(kl.hendler,kl);
                       int czs= (int) Math.round( kl.czasZakonczeniaZakupow-kl.czasRozpoczeciaZakupow);
                        wyslijInterakcjeWejsciaDoKolejki(new WejscieDoKolejki(czs,nr,kl.IDKlienta));
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
        fedamb.publikacje.publishRozpoczecieObslugi(rtiamb);
        // obiekty
        fedamb.subskrypcje.subscribeKasa(rtiamb);
        //interakcje
        fedamb.subskrypcje.subscribeZakonczanieObslugiKlienta(rtiamb);
        fedamb.subskrypcje.subscribeRozpoczecieSymulacji(rtiamb);
        fedamb.subskrypcje.subscribeZakoczenieSymulacji(rtiamb);

    }
}
