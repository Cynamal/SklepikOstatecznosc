package statistic.Obj;

import Interactions.RozpoczecieObslugi;

/**
 * Created by Karolina on 23.06.2017.
 */
public class EvRozpoczecieObslugi {
    double czas;
    Interactions.RozpoczecieObslugi rozpoczecieObslugi;
    public EvRozpoczecieObslugi(double czas, Interactions.RozpoczecieObslugi rozpoczecieObslugi)
    {
        this.czas=czas;
        this.rozpoczecieObslugi=rozpoczecieObslugi;
    }

    public double getCzas() {
        return czas;
    }

    public RozpoczecieObslugi getRozpoczecieObslugi() {
        return rozpoczecieObslugi;
    }
}
