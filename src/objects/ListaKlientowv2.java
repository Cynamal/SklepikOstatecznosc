package objects;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by Marcin on 24.06.2017.
 */
public class ListaKlientowv2 extends LinkedList<Klient> {
    public int WielkoscMax;
    public int iteratorKlienta=0;
    public int NumerKolejki;
    public ListaKlientowv2(int WielkoscMax,int NumerKolejki) {
        this.WielkoscMax = WielkoscMax;
        this.NumerKolejki=NumerKolejki;
    }
    public void addKlientKasa(Klient klient)
    {
        this.addKlient(klient);
    }
    public void addKlient(Klient klient)
    {
        klient.NumerWKolejce=iteratorKlienta++;
        klient.NumerKolejki=this.NumerKolejki;
        this.add(klient);
    }
    public boolean CzySaUprzywilejowani()
    {
        for (Klient kl:this
             ) {
            if(kl.uprzywilejowany) return true;
        }
        return false;
    }
    public boolean CzyPusta()
    {
        return this.size()==0;
    }
    public Klient getAndDeleteFirst()
    {

        if(CzySaUprzywilejowani())
        {
            for(int i=0;i<size();i++)
            {
                if(get(i).uprzywilejowany)
                {
                    Klient ret= get(i);
                    remove(i);
                    return ret;
                }
            }
        }
        return removeFirst();
    }
    public boolean CzyPelna()
    {
        return size()>=WielkoscMax;
    }
    public int ileLudziowZPriorytetem()
    {
        int ret=0;
        for (Klient kl: this
             ) {
            if(kl.uprzywilejowany)ret++;
        }
        return ret;
    }
}
