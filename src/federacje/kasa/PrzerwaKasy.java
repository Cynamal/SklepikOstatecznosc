package federacje.kasa;

import objects.Kasa;

/**
 * Created by Marcin on 24.06.2017.
 */
public class PrzerwaKasy {
    public Kasa kasa;
    public double KoniecPrzerwy=-1;
    double czasPrzerwy;
    double poczatekPrzerwy;
    public PrzerwaKasy(Kasa kasa,double czasPrzerwy,double poczatekPrzerwy)
    {
        this.kasa=kasa;
        this.czasPrzerwy=czasPrzerwy;
        this.poczatekPrzerwy=poczatekPrzerwy;
    }
    public void zakonczenieObslugiOstatniego(double time)
    {
        KoniecPrzerwy=time+czasPrzerwy;
    }
    public boolean czyKoniecPrzerwy(double time)
    {
        String z= "false";
        if(time==KoniecPrzerwy)  z= "true";
        System.out.println("Debugff01 time:"+time+"Koniec przerwy "+KoniecPrzerwy+"Co zwroci:"+z);
        if(KoniecPrzerwy==-1)return false;
        return time>=KoniecPrzerwy;
    }
}
