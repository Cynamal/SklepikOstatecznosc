package statistic.Obj;

import Interactions.WejscieDoKolejki;

/**
 * Created by Karolina on 23.06.2017.
 */
public class EvWejscieDoKolejki {
    double czas;
    Interactions.WejscieDoKolejki wejscieDoKolejki;
    public EvWejscieDoKolejki(double czas, Interactions.WejscieDoKolejki wejscieDoKolejki)
    {
        this.czas=czas;
        this.wejscieDoKolejki=wejscieDoKolejki;
    }

    public double getCzas() {
        return czas;
    }

    public WejscieDoKolejki getWejscieDoKolejki() {
        return wejscieDoKolejki;
    }
}
