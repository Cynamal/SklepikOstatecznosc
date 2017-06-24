package Interactions;

import common.AmbasadorAbstract;
import hla.rti.RTIinternalError;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

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


    public SuppliedParameters getRTIAtributes(AmbasadorAbstract fedamb) throws RTIinternalError {
        SuppliedParameters attributes =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        byte[] CzasOczekiwania = getIDKlientaByte();
        byte[] CzasObslugi = getCzasObslugiByte();

        attributes.add(fedamb.publikacje.zakonczenieObslugiKlientaHandler.IDKlientaHandler, CzasOczekiwania);
        attributes.add(fedamb.publikacje.zakonczenieObslugiKlientaHandler.CzasObslugiHandler, CzasObslugi);


        return attributes;
    }
}
