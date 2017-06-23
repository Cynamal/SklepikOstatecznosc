package federacje.klient;

import common.AmbasadorAbstract;
import common.ExternalEventAbstract;
import common.FederateAbstract;
import federacje.kasa.KasaFederate;
import hla.rti.RTIexception;
import objects.Kasa;

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

            try {
                advanceTime(1.0,fedamb);
            } catch (RTIexception rtIexception) {
                rtIexception.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        new KlientFederate().runFederate();
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
