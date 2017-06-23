package federacje.statystyka;

import common.AmbasadorAbstract;
import common.FederateAbstract;
import federacje.kasa.KasaFederate;

/**
 * Created by Marcin on 22.06.2017.
 */
public class StatystykaFederate extends FederateAbstract {
    public static final String federateName = "StatystykaFederate";
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
        new StatystykaFederate().runFederate();
    }

    private void publishAndSubscribe() {
        //obiekty

        //interakcje

        //obiekty
        fedamb.subskrypcje.subscribeKasa(rtiamb);
        fedamb.subskrypcje.subscribeKlient(rtiamb);
        //interakcje
        fedamb.subskrypcje.subscribeRozpoczecieSymulacji(rtiamb);
        fedamb.subskrypcje.subscribeZakoczenieSymulacji(rtiamb);
        fedamb.subskrypcje.subscribeUruchomNowaKase(rtiamb);
        fedamb.subskrypcje.subscribeRozpocznijPrzerwe(rtiamb);
        fedamb.subskrypcje.subscribeRozpoczecieObslugi(rtiamb);
        fedamb.subskrypcje.subscribeZakonczanieObslugiKlienta(rtiamb);
        fedamb.subskrypcje.subscribeZakoczeniePrzerwy(rtiamb);
    }
}