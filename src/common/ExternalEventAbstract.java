package common;

import Interactions.*;
import objects.Kasa;
import objects.Klient;

import java.util.Comparator;

/**
 * Created by Marcin on 22.06.2017.
 */
public class ExternalEventAbstract {
    public enum EventType {Kasa, Klient,ZakonczanieObslugiKlienta}

    private Klient k;
    private Kasa kas;
    private ZakonczanieObslugiKlienta zako;
    private EventType eventType;
    private Double time;
    public void ZakonczanieObslugiKlientaEvent(ZakonczanieObslugiKlienta zako, Double time)
    {
        this.zako = zako;
        this.eventType = EventType.ZakonczanieObslugiKlienta;
        this.time = time;
    }

    public ZakonczanieObslugiKlienta getZakonczanieObslugiKlienta() {
        return zako;
    }

    public void KasaEvent(Kasa kas, Double time) {
        this.kas = kas;
        this.eventType = EventType.Kasa;
        this.time = time;
    }
    public void KlientEvent(Klient k, EventType eventType, Double time) {
        this.k = k;
        this.eventType = eventType;
        this.time = time;
    }
    public void KlientEvent(Klient k, Double time) {
        this.k = k;
        this.eventType = EventType.Klient;
        this.time = time;
    }
    public void KlientEvent(int IDKlienta,boolean uprzywilejowany, int NumerKolejki,int NumerWKolejce,int Gotowka, EventType eventType, Double time) {
        this.k = new Klient(IDKlienta,uprzywilejowany,NumerKolejki,NumerWKolejce,Gotowka);
        this.eventType = eventType;
        this.time = time;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Klient getKlient()
    {
        return k;
    }

    public double getTime() {
        return time;
    }
    public Kasa getKasa() {return kas;}
    public static class ExternalEventComparator implements Comparator<ExternalEventAbstract> {

        @Override
        public int compare(ExternalEventAbstract o1, ExternalEventAbstract o2) {
            return o1.time.compareTo(o2.time);
        }
    }
}

