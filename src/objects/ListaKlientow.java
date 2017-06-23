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
                tmp=klient;
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
