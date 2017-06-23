package Interactions;

import hla.rti.jlc.EncodingHelpers;

/**
 * Created by Karolina on 23.06.2017.
 */
public class RozpoczecieObslugi {
    public int CzasOczekiwania;
    public int NumerKasy;
    public int IDKlienta;

    public RozpoczecieObslugi(int CzasOczekiwania, int NumerKasy, int IDKlienta){
        this.CzasOczekiwania = CzasOczekiwania;
        this.NumerKasy = NumerKasy;
        this.IDKlienta=IDKlienta;
    }

    public byte[] getIDKlientaByte()
    {
        return EncodingHelpers.encodeInt(IDKlienta);
    }

    public byte[] getCzasOczekiwaniaByte() {
        return EncodingHelpers.encodeInt(CzasOczekiwania);
    }

    public byte[] getNumerKasyByte() {
        return EncodingHelpers.encodeInt(NumerKasy);
    }

    public String toString() {
        return "Kasa "+NumerKasy+" rozpoczela obsluge klienta o ID "+IDKlienta+" czas oczekiwania to "+CzasOczekiwania;
    }
}
