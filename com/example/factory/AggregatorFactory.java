package factory;

import tareas.Aggregator;
import tareas.Task;

public class AggregatorFactory extends TaskFactory {
    @Override
    public Task createTask() {
        return new Aggregator();
    }
}