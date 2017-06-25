package statistic.Obj;

import Interactions.RozpocznijPrzerwe;

/**
 * Created by Karolina on 23.06.2017.
 */
public class EvRozpocznijPrzerwe {
    double czas;
    Interactions.RozpocznijPrzerwe rozpocznijPrzerwe;
    public EvRozpocznijPrzerwe(double czas, Interactions.RozpocznijPrzerwe rozpocznijPrzerwe)
    {
        this.czas=czas;
        this.rozpocznijPrzerwe=rozpocznijPrzerwe;
    }

    public double getCzas() {
        return czas;
    }

    public RozpocznijPrzerwe getRozpocznijPrzerwe() {
        return rozpocznijPrzerwe;
    }
}
