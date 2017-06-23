package Interactions;

import hla.rti.jlc.EncodingHelpers;

/**
 * Created by Marcin on 23.06.2017.
 */
public class ZakonczanieObslugiKlienta {
    public int IDKlienta;
    public int CzasObslugi;
    public ZakonczanieObslugiKlienta(int IDKlienta,int CzasObslugi)
    {
        this.IDKlienta=IDKlienta;
        this.CzasObslugi=CzasObslugi;
    }
    public byte[] getIDKlientaByte()
    {
        return EncodingHelpers.encodeInt(IDKlienta);
    }
    public byte[] getCzasObslugiByte()
    {
        return EncodingHelpers.encodeInt(CzasObslugi);
    }

    @Override
    public String toString() {
        return "Zakonczenie obslugi kliena "+IDKlienta+" czas obslugi to "+CzasObslugi;
    }
}
