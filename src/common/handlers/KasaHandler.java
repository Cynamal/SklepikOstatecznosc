package common.handlers;

import hla.rti.*;
import hla.rti.jlc.RtiFactoryFactory;

/**
 * Created by Karolina on 22.06.2017.
 */
public class KasaHandler {

    public int KasaHandler;
    public int NumerKasyHandler;
    public int DlugoscHandler;
    public int CzyOtwartaHandler;
    public int CzyPelnaHandler;
    public AttributeHandleSet attributes;

    public KasaHandler(RTIambassador rtiamb) throws NameNotFound, FederateNotExecutionMember, RTIinternalError, ObjectClassNotDefined, AttributeNotDefined {
        KasaHandler = rtiamb.getObjectClassHandle( "HLAobjectRoot.Kasa" );
        NumerKasyHandler = rtiamb.getAttributeHandle("NumerKasy", KasaHandler);
        DlugoscHandler = rtiamb.getAttributeHandle("Dlugosc", KasaHandler);
        CzyOtwartaHandler = rtiamb.getAttributeHandle("CzyOtwarta", KasaHandler);
        CzyPelnaHandler = rtiamb.getAttributeHandle("CzyPelna", KasaHandler);
        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(NumerKasyHandler);
        attributes.add(DlugoscHandler);
        attributes.add(CzyOtwartaHandler);
        attributes.add(CzyPelnaHandler);
    }

    public int getKasaHandler(){
        return KasaHandler;
    }

    public AttributeHandleSet getAttributeHandleSet(){
        return attributes;
    }
}
