package objects;

import common.AmbasadorAbstract;
import hla.rti.RTIinternalError;
import hla.rti.SuppliedAttributes;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

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
    public int hendKasa;
    public int idKlientaOblugiwanego= -1;
    public boolean czyObsluguje=false;
    public double czasRozpoczeciaObslugi;
    public double czasZakonczeniaObslugi;
    public Kasa(int NumerKasy,int Dlugosc, boolean CzyPelna,boolean CzyOtwarta,int hendKasa)
    {
        this.hendKasa=hendKasa;
        this.NumerKasy=NumerKasy;
        this.Dlugosc=Dlugosc;
        this.CzyPelna=CzyPelna;
        this.CzyOtwarta=CzyOtwarta;
    }
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
    public SuppliedAttributes getRTIAtributes(AmbasadorAbstract fedamb) throws RTIinternalError {
        SuppliedAttributes attributes =
                RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();

        byte[] NumerKasy = EncodingHelpers.encodeInt(this.NumerKasy);
        byte[] Dlugosc = EncodingHelpers.encodeInt(this.Dlugosc);
        byte[] CzyPelna = EncodingHelpers.encodeBoolean(this.CzyPelna);
        byte[] CzyOtwarta = EncodingHelpers.encodeBoolean(this.CzyOtwarta);

        attributes.add(fedamb.publikacje.kasaHandler.NumerKasyHandler, NumerKasy);
        attributes.add(fedamb.publikacje.kasaHandler.DlugoscHandler, Dlugosc);
        attributes.add(fedamb.publikacje.kasaHandler.CzyPelnaHandler, CzyPelna);
        attributes.add(fedamb.publikacje.kasaHandler.CzyOtwartaHandler, CzyOtwarta);
        return attributes;
    }

    /** Dodaje klienta lub aktualizuje jesli juz istnieje w liscie kas lub w licie klientow w sklepie
     * @param klient klient otrzymany z rti
     * @param list lista kas
     * @param klienciWSklepie
     * @return jeli klient zostal dodany to true
     */
public static boolean addorChangeIfExistClientToListOfKasaorKlientList(Klient klient,LinkedList<Kasa> list,ListaKlientow klienciWSklepie)
{
   int czyRobiZakupy= klienciWSklepie.getIndexByID(klient.IDKlienta);
   if(czyRobiZakupy!=-1)
   {
       if(klient.NumerWKolejce==-1)
       return klienciWSklepie.addorChangeIfExist(klient);
       else
       {
           try {
           klienciWSklepie.remove(klient);

               return addorChangeIfExistClientToListOfKasa(klient,list);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }
   else
   {
       try {
           return addorChangeIfExistClientToListOfKasa(klient,list);
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
            if(!tmp.CzyPelna&&MaxSizeofQiue>tmp.kolejka.size()&&MaxSizeofQiue>tmp.Dlugosc&&tmp.CzyOtwarta)
            {
              //  System.out.println("max"+MaxSizeofQiue+"czypelne"+tmp.CzyPelna+"NumerKasy:"+tmp.NumerKasy+"W kolejce:"+tmp.kolejka.size()+" RTI size:"+tmp.Dlugosc);
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
            int ret=-1;
            for(KasaIndex=0;kasy.size()>KasaIndex;KasaIndex++)
            {System.out.println("tu"+kasy.size()+"best "+best+"asda "+kasy.get(KasaIndex).kolejka.size()+"index"+KasaIndex);
                if (best>=kasy.get(KasaIndex).kolejka.size())
                {
                   // System.out.println("jest"+kasy.size());
                    best=kasy.get(KasaIndex).kolejka.size();
                    ret=KasaIndex;
                }
            }
           // System.out.println("przedret"+kasy.size());
            return ret;
        }
        else
        {
            int best=kasy.getFirst().kolejka.size();
            int KasaIndex=-1;
            int ret=-1;
            for(KasaIndex=0;kasy.size()>KasaIndex;KasaIndex++)
            {
                if (best>=kasy.get(KasaIndex).kolejka.size())
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
                tmp.CzyOtwarta=kasa.CzyOtwarta;
               // tmp.kolejka=kasa.kolejka;
                tmp.Dlugosc=kasa.Dlugosc;
                tmp.CzyPelna=kasa.CzyPelna;
                //tmp=kasa;
                return false;

            }
        }
        list.add(kasa);
        return true;
    }
    @Override
    public String toString() {
        String pelna= " i pelana";
        String otwarta="zamknieta";
        if(CzyOtwarta)
            otwarta="otwarta";

        if(!CzyPelna)
            pelna="";
        return "Kasa o numerze:"+NumerKasy+" Dlugos kolejki:"+Dlugosc+" jest "+otwarta+pelna;
    }
    public static Kasa FindbyID(LinkedList<Kasa> kasy,int numerKasy) throws Exception
    {
        for (Kasa ret:kasy
             ) {
            if(ret.NumerKasy==numerKasy) return ret;
        }
        throw new Exception("Nie znaleziono kasy");
    }
}
