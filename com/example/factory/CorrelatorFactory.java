package factory;

import tareas.Correlator;
import tareas.Task;

public class CorrelatorFactory extends TaskFactory{
    @Override
    public Task createTask() {
        return new Correlator();
    }
}