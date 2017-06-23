package federacje.wlasciciel;

import common.AmbasadorAbstract;
import common.FederateAbstract;
import federacje.kasa.KasaFederate;

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

        }
    }
    public static void main(String[] args) {
        new WlascicielFederate().runFederate();
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