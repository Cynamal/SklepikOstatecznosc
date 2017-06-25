package statistic.Obj;

import Interactions.ZakoczeniePrzerwy;

/**
 * Created by Karolina on 23.06.2017.
 */
public class EvZakoczeniePrzerwy {
    double czas;
    Interactions.ZakoczeniePrzerwy zakoczeniePrzerwy;
    public EvZakoczeniePrzerwy(double czas, Interactions.ZakoczeniePrzerwy zakoczeniePrzerwy)
    {
        this.czas=czas;
        this.zakoczeniePrzerwy=zakoczeniePrzerwy;
    }

    public double getCzas() {
        return czas;
    }

    public ZakoczeniePrzerwy getZakoczeniePrzerwy() {
        return zakoczeniePrzerwy;
    }
}
