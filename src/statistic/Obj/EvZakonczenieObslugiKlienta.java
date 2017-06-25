package statistic.Obj;

import Interactions.ZakonczanieObslugiKlienta;

/**
 * Created by Marcin on 23.06.2017.
 */
public class EvZakonczenieObslugiKlienta {
    double czas;
    Interactions.ZakonczanieObslugiKlienta zakonczanieObslugiKlienta;
    public EvZakonczenieObslugiKlienta(double czas, Interactions.ZakonczanieObslugiKlienta zakonczanieObslugiKlienta)
    {
        this.czas=czas;
        this.zakonczanieObslugiKlienta=zakonczanieObslugiKlienta;
    }

    public double getCzas() {
        return czas;
    }

    public ZakonczanieObslugiKlienta getZakonczanieObslugiKlienta() {
        return zakonczanieObslugiKlienta;
    }

}
