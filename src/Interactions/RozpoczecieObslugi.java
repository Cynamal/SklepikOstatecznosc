package Interactions;

import common.AmbasadorAbstract;
import hla.rti.RTIinternalError;
import hla.rti.SuppliedAttributes;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

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

    public SuppliedParameters getRTIAtributes(AmbasadorAbstract fedamb) throws RTIinternalError {
        SuppliedParameters attributes =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        byte[] CzasOczekiwania = getIDKlientaByte();
        byte[] NumerKasy = getCzasOczekiwaniaByte();
        byte[] IDKlienta = getNumerKasyByte();

        attributes.add(fedamb.publikacje.rozpoczecieObslugiHandler.NumerKasyHandler, NumerKasy);
        attributes.add(fedamb.publikacje.rozpoczecieObslugiHandler.CzasOczekiwaniaHandler, CzasOczekiwania);
        attributes.add(fedamb.publikacje.rozpoczecieObslugiHandler.IDKlientaHandler, IDKlienta);

        return attributes;
    }
    public String toString() {
        return "Kasa "+NumerKasy+" rozpoczela obsluge klienta o ID "+IDKlienta+" czas oczekiwania to "+CzasOczekiwania;
    }
}
