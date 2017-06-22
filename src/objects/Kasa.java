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

    /** Dodaje klienta lub aktualizuje jesli juz istnieje w liscie kas lub w licie klientow w sklepie
     * @param klient klient otrzymany z rti
     * @param list lista kas
     * @param klienciWSklepie
     * @return jeli klient zostal dodany to true
     */
public static boolean addorChangeIfExistClientToListOfKasaorKlientList(Klient klient,LinkedList<Kasa> list,ListaKlientow klienciWSklepie)
{
    if(klient.NumerKolejki==-1)
    {
     return  klienciWSklepie.addorChangeIfExist(klient);
    }
    else
    {
        try {
          return  Kasa.addorChangeIfExistClientToListOfKasa(klient,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    return false;
}
    /** Dodaje klienta lub aktualizuje jesli juz istnieje w liscie kas
     * @param klient klient otrzymany z rti
     * @param list lista kas
     * @return jeli klient zostal dodany to true
     * @throws Exception jeli nie udalo sie znalesc klienta
     */
    private static boolean addorChangeIfExistClientToListOfKasa(Klient klient,LinkedList<Kasa> list) throws Exception {
        int numerKolejki= klient.NumerKolejki;
        for (Kasa ka:list
                ) {
            if(ka.NumerKasy==numerKolejki) return ka.kolejka.addorChangeIfExist(klient);
        }

        throw new Exception("Nie znaleziono odpowiedniej kasy ");
    }

    /** Zwraca liste stringow opisujacych wszystkie kasy w liscie
     * @param list
     * @return
     */
    public static LinkedList<String> ToStringList(LinkedList<Kasa> list)
    {
        LinkedList<String> ret= new LinkedList<>();
        for (Kasa tmp: list
                ) {
            ret.add(tmp.toString());
        }
        return ret;
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

    /** Dodaje kase lub aktualizuje jesli taka kasa isnieje (uzywac do pobierania danych od rti)
     * @param kasa Kasa ktora zostala odebrana
     * @param list lista kas
     * @return jezeli kasa dodana true jezeli zaktualizowana false
     */
    public static boolean addorChangeIfExist(Kasa kasa,LinkedList<Kasa> list)
    {
        for(Kasa tmp:list)
        {
            if(tmp.NumerKasy==kasa.NumerKasy)
            {
                tmp=kasa;
                return false;
            }
        }
        list.add(kasa);
        return true;
    }

}
