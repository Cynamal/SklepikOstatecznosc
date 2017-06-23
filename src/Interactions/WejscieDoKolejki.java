package Interactions;

import hla.rti.jlc.EncodingHelpers;

/**
 * Created by Karolina on 23.06.2017.
 */
public class WejscieDoKolejki {

    public int CzasZakupow;
    public int NumerKasy;
    public int IDKlienta;

    public WejscieDoKolejki(int CzasZakupow, int NumerKasy, int IDKlienta){
        this.CzasZakupow = CzasZakupow;
        this.NumerKasy = NumerKasy;
        this.IDKlienta=IDKlienta;
    }

    public byte[] getIDKlientaByte()
    {
        return EncodingHelpers.encodeInt(IDKlienta);
    }

    public byte[] getCzasZakupowByte() {
        return EncodingHelpers.encodeInt(CzasZakupow);
    }

    public byte[] getNumerKasyByte() {
        return EncodingHelpers.encodeInt(NumerKasy);
    }

    public String toString() {
        return "Wejscie do kolejki kasy "+NumerKasy+" klienta o ID "+IDKlienta+" czas zakupow to "+CzasZakupow;
    }
}
