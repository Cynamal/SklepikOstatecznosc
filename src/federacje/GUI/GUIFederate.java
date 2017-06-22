package federacje.GUI;

import common.FederateAbstract;
import objects.Kasa;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Marcin on 22.06.2017.
 */
public class GUIFederate extends FederateAbstract {
    public static final String federateName = "KasaFederat1";
    public GUIapp GUI;
    public Lock CheckStartedLock = new ReentrantLock();
    public boolean Started=false;
    public LinkedList<Kasa> kasy = new LinkedList<>();
    private int IteratorKasy = 1;
    public GUIFederate(GUIapp GUI)
    {
        this.GUI=GUI;
    }
    public void runFederate(){

    }

}
