package Interactions;

import hla.rti.jlc.EncodingHelpers;

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
}
