package factory;

import pipeline.Slot;
import tareas.Correlator;

public class CorrelatorFactory extends TaskFactory{

    public Correlator createTask(Slot[] entradas, Slot... salidas) {

        return new Correlator(entradas, salidas);
    }
}