package federacje.klient;

/**
 * Created by Marcin on 24.06.2017.
 */
public class MaszynaLosujacaUprzywilejowanych {
    public static int WspoczynnikWiecejBezPriorytetu=4;
    public static boolean Los()
    {
       int randomNum =  (int)(Math.random() * WspoczynnikWiecejBezPriorytetu);
       return randomNum==0;
    }
}
