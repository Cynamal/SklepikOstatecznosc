package statistic.Obj;

import objects.Kasa;

/**
 * Created by Marcin on 23.06.2017.
 */
public class UpdateKasa {
    double time;
    Kasa kasa;
    public UpdateKasa(double time,Kasa kasa)
    {
        this.time=time;
        this.kasa=kasa;
    }

    public double getTime() {
        return time;
    }

    public Kasa getKasa() {
        return kasa;
    }
}
