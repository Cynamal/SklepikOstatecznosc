package common.handlers;

import hla.rti.*;
import hla.rti.jlc.RtiFactoryFactory;

/**
 * Created by Marcin on 22.06.2017.
 */
public class KlientHandler {
   public int KlientHandler;
   public int IDKlientaHandlerOb;
   public int uprzywilejowanyHandler;
   public int NumerKolejkiHandler;
   public int NumerWKolejceHandler;
   public int GotowkaHandler;
   public AttributeHandleSet attributes;

    public KlientHandler(RTIambassador rtiamb) throws NameNotFound, FederateNotExecutionMember, RTIinternalError, ObjectClassNotDefined, AttributeNotDefined {
        KlientHandler = rtiamb.getObjectClassHandle("HLAobjectRoot.Klient");
        IDKlientaHandlerOb = rtiamb.getAttributeHandle("IDKlienta", KlientHandler);
        uprzywilejowanyHandler = rtiamb.getAttributeHandle("uprzywilejowany", KlientHandler);
        NumerKolejkiHandler = rtiamb.getAttributeHandle("NumerKolejki", KlientHandler);
        NumerWKolejceHandler = rtiamb.getAttributeHandle("NumerWKolejce", KlientHandler);
        GotowkaHandler = rtiamb.getAttributeHandle("Gotowka", KlientHandler);
        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add(IDKlientaHandlerOb);
        attributes.add(uprzywilejowanyHandler);
        attributes.add(NumerKolejkiHandler);
        attributes.add(NumerWKolejceHandler);
        attributes.add(GotowkaHandler);
    }

    public AttributeHandleSet getAttributeHandleSet(){
        return attributes;
    }

    public int getKlientHandler(){
        return KlientHandler;
    }

}
