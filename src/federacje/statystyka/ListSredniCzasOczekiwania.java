package federacje.statystyka;

import java.util.LinkedList;

/**
 * Created by Marcin on 24.06.2017.
 */
public class ListSredniCzasOczekiwania extends LinkedList<SredniCzasOczekiwania>{
    public SredniCzasOczekiwania getbyIDKlient(int IDKlient) throws Exception {
        for (SredniCzasOczekiwania sr: this
             ) {
            if(sr.IDKlienta==IDKlient)return sr;
        }
        throw new Exception("Nie znaleziono wpisu");
    }
    public double ObliczSredni()
    {
        int suma=0;
        int iter=0;
        for (SredniCzasOczekiwania sr: this
                ) {
            if(sr.CzasWyjsciaZKolejki!=-1.0)
            {
                suma+=sr.CzasWyjsciaZKolejki-sr.CzasWejsciaDoKolejki;
                iter++;
            }

        }
       // System.out.println("sume: "+suma+" iter: "+iter+" Rozmiar listy: "+this.size());
        return 1.0*suma/(1.0*iter);
    }
}
