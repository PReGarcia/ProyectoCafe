package factory;

import pipeline.Slot;
import tareas.Aggregator;
import tareas.Task;

public class AggregatorFactory extends TaskFactory {
    @Override
    public Task createTask() {
        return null;
    }

    public Task createTask(String xpath, Slot entrada, Slot salida) {
        return new Aggregator(xpath, entrada, salida);
    }
}