package Interactions;

import common.AmbasadorAbstract;
import hla.rti.RTIinternalError;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

/**
 * Created by Karolina on 23.06.2017.
 */
public class RozpocznijPrzerwe {

    public int NumerKasy;

    public RozpocznijPrzerwe(int NumerKasy){
        this.NumerKasy = NumerKasy;
    }

    public byte[] getNumerKasyByte() {
        return EncodingHelpers.encodeInt(NumerKasy);
    }

    public String toString() {
        return "Oznaczono kase "+NumerKasy+" do zrobienia przerwy";
    }

    public SuppliedParameters getRTIAtributes(AmbasadorAbstract fedamb) throws RTIinternalError {
        SuppliedParameters attributes =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        byte[] NumerKasy = getNumerKasyByte();

        attributes.add(fedamb.publikacje.rozpocznijPrzerweHandler.getRozpocznijPrzerweHandler(), NumerKasy);
        return attributes;
    }
}
