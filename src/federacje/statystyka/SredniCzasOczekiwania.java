package federacje.statystyka;

/**
 * Created by Marcin on 24.06.2017.
 */
public class SredniCzasOczekiwania {
   public int IDKlienta;
   public double CzasWejsciaDoKolejki;
   public double CzasWyjsciaZKolejki;
   public SredniCzasOczekiwania(int IDKlienta,double CzasWejsciaDoKolejki)
   {
       this.IDKlienta=IDKlienta;
       this.CzasWejsciaDoKolejki=CzasWejsciaDoKolejki;
   }
    public boolean CzyTenSamIndex(int IDKlienta)
    {
        return IDKlienta==this.IDKlienta;
    }
}
