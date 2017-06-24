package objects;

import java.util.LinkedList;

/**
 * Created by Marcin on 22.06.2017.
 */
public class ListaKlientow extends LinkedList<Klient> {
    public int WielkoscMax;

    public ListaKlientow(int WielkoscMax) {
        this.WielkoscMax = WielkoscMax;
    }

    public int GetplaceWithPropity(boolean przywilej) {
        if (this.size() < WielkoscMax) {
            int ret = 0;
            if (przywilej) {
                for (Klient kl : this) {
                    if (ret < kl.NumerWKolejce && kl.uprzywilejowany)
                        ret = kl.NumerWKolejce;
                }
                return ret + 1;
            } else {
                for (Klient kl : this) {
                    if (ret < kl.NumerWKolejce)
                        ret = kl.NumerWKolejce;
                }
                if (ret > WielkoscMax)
                    return ret + 1;
                else
                    return ret + 1 + WielkoscMax;
            }
        } else return -1;

    }
    public boolean addorChangeIfExist(Klient klient)
    {
        for(Klient tmp:this)
        {
            if(tmp.IDKlienta==klient.IDKlienta)
            {
                tmp.NumerKolejki=klient.NumerKolejki;
                tmp.Gotowka=klient.Gotowka;
                tmp.NumerWKolejce=klient.NumerWKolejce;
                tmp.uprzywilejowany=klient.uprzywilejowany;

              //  tmp=klient;
                return false;
            }
        }
        this.add(klient);
        return true;
    }
    public LinkedList<String> ListToStringTable() {
        LinkedList<String> ret = new LinkedList<>();
        for (Klient tmp : this
                ) {
            ret.add(tmp.toString());
        }
        return ret;
    }
    public int getIndexByID(int id)
    {
        for(int i=0;i<this.size();i++)
        {
            if(this.get(i).IDKlienta==id)
                return i;
        }
        return -1;
    }
    public ListaKlientow getIndexedKasaClientOnly(int IndexKasa)
    {
        ListaKlientow lis= new ListaKlientow(Integer.MAX_VALUE);
        for (Klient kl: this
             ) {
            if(kl.NumerKolejki==IndexKasa) lis.add(kl);
        }
        return lis;
    }
    public int mygetFirst()
    {
        if(this.size()==0) return -1;
        int index=0;
        int best=getFirst().NumerWKolejce;
        for (int i=0;i<this.size();i++)
        {
            if(get(i).NumerWKolejce<=best)
            {
                best=get(i).NumerWKolejce;
                index=i;
            }
        }

        return index;
    }
    public int mygetFirst(int IndexKasa)
    {
        ListaKlientow lis =this.getIndexedKasaClientOnly(IndexKasa);
        if(lis.size()==0) return -1;
        int index=0;
        int best=getFirst().NumerWKolejce;
        for (int i=0;i<this.size();i++)
        {
            if(get(i).NumerWKolejce<=best&&get(i).NumerKolejki==IndexKasa)
            {
                best=get(i).NumerWKolejce;
                index=i;
            }
        }

        return index;
    }
    public Klient GetAndRemove(int index)
    {
        Klient tmp=this.get(index);
        this.remove(tmp);
        return tmp;
    }
    @Override
    public String toString() {
        String ret="Kolejka:";
        for (String s : ListToStringTable()
                ) {
                ret+="("+s+"),";
        }
        return ret;
    }
}
