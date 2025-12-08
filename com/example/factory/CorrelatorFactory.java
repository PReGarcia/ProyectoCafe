package factory;

import pipeline.Slot;
import tareas.Correlator;
import tareas.Task;

public class CorrelatorFactory extends TaskFactory{
    @Override
    public Task createTask() {
        return null;
    }

    public Correlator createTask(Slot[] entradas, Slot... salidas) {
        return new Correlator(entradas, salidas);
    }
}