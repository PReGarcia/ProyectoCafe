package factory;

import pipeline.Slot;
import tareas.Replicator;
import tareas.Task;

public class ReplicatorFactory extends TaskFactory{
    @Override
    public Task createTask() {
        return null;
    }

    public Task createTask(Slot input, Slot... output) {
        return new Replicator(input, output);
    }

}
