package statistic.Obj;

import objects.Klient;

/**
 * Created by Karolina on 23.06.2017.
 */
public class UpdateKlient {
    double time;
    Klient klient;
    public UpdateKlient(double time,Klient klient)
    {
        this.time=time;
        this.klient=klient;
    }

    public double getTime() {
        return time;
    }

    public Klient getKlient() {
        return klient;
    }
}
