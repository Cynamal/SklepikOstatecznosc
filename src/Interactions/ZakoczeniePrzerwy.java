package Interactions;

import hla.rti.jlc.EncodingHelpers;

/**
 * Created by Karolina on 23.06.2017.
 */
public class ZakoczeniePrzerwy {

    public int CzasPrzerwy;
    public int NumerKasy;

    public ZakoczeniePrzerwy(int CzasPrzerwy, int NumerKasy){
        this.CzasPrzerwy = CzasPrzerwy;
        this.NumerKasy = NumerKasy;
    }

    public byte[] getCzasPrzerwyByte(){
        return EncodingHelpers.encodeInt(CzasPrzerwy);
    }

    public byte[] getNumerKasyByte() {
        return EncodingHelpers.encodeInt(NumerKasy);
    }

    public String toString() {
        return "Zakonczenie przerwy kasy "+NumerKasy+" czas przerwy to "+CzasPrzerwy;
    }
}
