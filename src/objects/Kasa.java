package objects;

import java.util.LinkedList;

/**
 * Created by Marcin on 22.06.2017.
 */
public class Kasa {
    public ListaKlientow kolejka=new ListaKlientow(MaxSizeofQiue);
    public int NumerKasy;
    public int Dlugosc;
    public boolean CzyPelna;
    public boolean CzyOtwarta;
    public static int MaxSizeofQiue=10;
    public Kasa(int NumerKasy,int Dlugosc, boolean CzyPelna,boolean CzyOtwarta)
    {
        this.NumerKasy=NumerKasy;
        this.Dlugosc=Dlugosc;
        this.CzyPelna=CzyPelna;
        this.CzyOtwarta=CzyOtwarta;
    }
    public Kasa()
    {
        this.NumerKasy=-1;
        this.Dlugosc=-1;
        this.CzyPelna=true;
        this.CzyOtwarta=false;
    }

    /**
     * Pobiera tylko klasy aktywne z listy kas
     * @param kasy Lista wszystkich kas w systemie
     * @return Lista aktywnych kas
     */
    public static LinkedList<Kasa> getActiveOnly(LinkedList<Kasa> kasy)
    {
        LinkedList<Kasa> ret=new LinkedList<>();
        for (Kasa tmp: kasy
                ) {
            if(!tmp.CzyPelna&&MaxSizeofQiue>=tmp.kolejka.size())
            {
                ret.add(tmp);
            }
        }
        return ret;
    }

    /** odnajduje najlepsza kolejke dla danego klienta
     * @param przywilej czy klient jest uprzywilejowany
     * @param kasy lista aktywnych kas
     * @return
     */
    public static int FindBestQiue(boolean przywilej,LinkedList<Kasa> kasy)
    {
        if(kasy.size()==0) return -1;
        if(!przywilej)
        {
            int best=kasy.getFirst().kolejka.size();
            int KasaIndex=-1;
            int ret=0;
            for(KasaIndex=0;kasy.size()>KasaIndex;KasaIndex++)
            {
                if (best>kasy.get(KasaIndex).kolejka.size())
                {
                    best=kasy.get(KasaIndex).kolejka.size();
                    ret=KasaIndex;
                }
            }
            return ret;
        }
        else
        {
            int best=kasy.getFirst().kolejka.size();
            int KasaIndex=-1;
            int ret=0;
            for(KasaIndex=0;kasy.size()>KasaIndex;KasaIndex++)
            {
                if (best>kasy.get(KasaIndex).kolejka.size())
                {
                    best=kasy.get(KasaIndex).kolejka.size();
                    ret=KasaIndex;
                }
            }
            return ret;
        }

    }


}
