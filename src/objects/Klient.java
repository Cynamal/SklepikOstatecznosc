package objects;

import java.util.LinkedList;

/**
 * Created by Marcin on 22.06.2017.
 */
public class Klient {
    public  int IDKlienta;
    public boolean uprzywilejowany;
    public int NumerKolejki;
    public int NumerWKolejce;
    public int Gotowka;
    public Klient (int IDKlienta,boolean uprzywilejowany, int NumerKolejki,int NumerWKolejce,int Gotowka)
    {
        this.IDKlienta=IDKlienta;
        this.uprzywilejowany=uprzywilejowany;
        this.NumerKolejki=NumerKolejki;
        this.NumerWKolejce=NumerWKolejce;
        this.Gotowka=Gotowka;
    }
    public static int addWithPriority(Klient klient, LinkedList<Klient> list)
    {

        if(list.size()==0)
        {
            list.add(klient);
            return 0;
        }

        else
        {
            if(!klient.uprzywilejowany)
            {
                list.addLast(klient);
                return list.size()-1;
            }

            else
            {
                boolean dodany=false;
                for(int i=0;i<list.size()&&!dodany;i++)
                {

                    if(!list.get(i).uprzywilejowany)
                    {
                        dodany=true;
                        list.add(i,klient);
                        return i;
                    }
                }
                if(!dodany) {
                    list.addLast(klient);
                    return list.size()-1;
                }
            }
        }
        return -1;
    }

    public Klient()
    {
        this.IDKlienta=-1;
        this.NumerKolejki=-1;
        this.NumerWKolejce=-1;
        this.uprzywilejowany=true;
        this.Gotowka=-1;
    }

    @Override
    public String toString() {
        String tmp= " jest uprzywilejowany";

        if(!uprzywilejowany)
            tmp="";
        return "Klient o numerze:"+IDKlienta+" Jest w kolejce:"+NumerKolejki+"Na miejscu: "+NumerWKolejce+" Ma w portfelu:"+Gotowka+tmp;
    }

}
